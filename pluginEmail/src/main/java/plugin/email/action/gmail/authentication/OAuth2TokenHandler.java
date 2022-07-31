package plugin.email.action.gmail.authentication;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.Setter;
import md.dev.pipeline.exception.PipelineGenericException;
import md.dev.response.Response;
import md.dev.state.StateHandler;
import md.dev.state.exception.HandleStateException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Date;
import java.util.Scanner;

@SuppressWarnings("checkstyle:AbbreviationAsWordInName")
public class OAuth2TokenHandler {
    @Data
    static class Token {
        String accessToken;
        long expiration;
        String refreshToken;
    }

    private static OAuth2TokenHandler singleton;
    @Setter
    private String clientId;
    @Setter
    private String clientSecret;
    private Token token;
    private String secretKey;
    private final String stateId = this.getClass().getName();
    private final String salt = "" + this.getClass().getName().hashCode();
    private final Logger log = LoggerFactory.getLogger(OAuth2TokenHandler.class);


    private OAuth2TokenHandler() {
    }


    public static OAuth2TokenHandler getInstance() {
        if (singleton == null) {
            singleton = new OAuth2TokenHandler();
        }
        return singleton;
    }


    public String getToken() {
        if (token == null) {
            log.info("token needs to be initialized");
            initializeToken();
        }
        if (isTokenExpired()) {
            log.info("token is expired");
            refreshToken();
        }
        return token.accessToken;
    }


    public boolean isInitialized() {
        return token != null;
    }


    private void initializeToken() {
        if (StateHandler.hasProperty(stateId, "token")) {
            log.info("found local token for oauth");
            secretKey = printAndReadStdinSensitive("insert chosen password: ");

            String savedToken;
            try {
                savedToken = StateHandler.getSensitivePropertyFromState(
                    secretKey,
                    salt,
                    stateId,
                    "token"
                );
            } catch (HandleStateException e) {
                log.error("error while reading previous state", e);
                throw e;
            }

            ObjectMapper objectMapper = new ObjectMapper();
            try {
                this.token = objectMapper.readValue(savedToken, Token.class);
            } catch (JsonProcessingException e) {
                log.warn("error while loading oauth saved token", e);
            }
        } else {
            log.info("local token for oauth not found");
            System.out.println("go to: " + OAuth2GmailTokensConnector.getPermissionUrl(clientId));

            String authCode = printAndReadStdinSensitive("insert auth code: ");
            Response response;
            try {
                response = OAuth2GmailTokensConnector.exchangeAuthCode(
                    clientId, clientSecret, authCode
                );
            } catch (IOException | InterruptedException e) {
                log.error("error while exchange authorization code");
                throw new PipelineGenericException(e.getMessage());
            }

            if (response != null) {
                JSONObject jsonResponse = new JSONObject(response.getResponse());
                this.token = new Token();
                this.token.accessToken = jsonResponse.getString("access_token");
                this.token.refreshToken = jsonResponse.getString("refresh_token");
                this.token.expiration = new Date().toInstant().plusSeconds(jsonResponse.getLong(
                    "expires_in")).toEpochMilli();
                saveToken();
            }
        }
    }


    private void refreshToken() {
        Response response = null;
        try {
            response = OAuth2GmailTokensConnector.refreshToken(clientId, clientSecret,
                token.refreshToken
            );
        } catch (IOException | InterruptedException e) {
            log.error("error while refreshing token", e);
        }

        if (response != null) {
            JSONObject jsonResponse = new JSONObject(response.getResponse());
            token.accessToken = jsonResponse.getString("access_token");
            token.expiration = new Date().toInstant().plusSeconds(jsonResponse.getLong(
                "expires_in")).toEpochMilli();
            saveToken();
        }
    }


    private void saveToken() {
        if (secretKey == null) {
            secretKey = printAndReadStdinSensitive("chose a password: ");
        }
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            StateHandler.saveSensitivePropertyInState(
                secretKey,
                salt,
                stateId,
                "token",
                objectMapper.writeValueAsString(token)
            );
        } catch (JsonProcessingException e) {
            log.warn("error while saving token for oath", e);
        } catch (HandleStateException e) {
            log.warn("error while saving token for oath", e);
            throw e;
        }
    }


    private boolean isTokenExpired() {
        return token.expiration <= ((new Date()).toInstant().toEpochMilli());
    }


    private String printAndReadStdin(String message) {
        System.out.print(message);
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }


    private String printAndReadStdinSensitive(String message) {
        System.out.println(message);
        return new String(System.console().readPassword());
    }
}
