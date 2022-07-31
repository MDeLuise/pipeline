/*
 * This class implements the authentication flow provided by google endpoints for OAuth 2.0:
 * https://developers.google.com/identity/protocols/oauth2/native-app
 */
package plugin.email.action.gmail.authentication;

import md.dev.connector.Connector;
import md.dev.connector.PostConnector;
import md.dev.response.Response;

import java.io.IOException;

public class OAuth2GmailTokensConnector {
    private static final String AUTH_ENDPOINT = "https://accounts.google.com/o/oauth2/v2/auth";
    private static final String CODE_EXCHANGE_ENDPOINT = "https://oauth2.googleapis.com/token";
    private static final String REDIRECT_URI = "urn:ietf:wg:oauth:2.0:oob";


    public static String getPermissionUrl(String clientId) {
        return AUTH_ENDPOINT +
            "?scope=https%3A%2F%2Fmail.google.com%2F&response_type=code&redirect_uri=" +
            REDIRECT_URI + "&client_id=" + clientId;
    }


    public static Response exchangeAuthCode(String clientId, String clientSecret, String authCode)
    throws IOException, InterruptedException {
        Connector connector = new PostConnector(CODE_EXCHANGE_ENDPOINT);
        connector.addPostParam(String.format("""
            {
                client_id: '%s',
                client_secret: '%s',
                code: '%s',
                redirect_uri: '%s',
                grant_type: "authorization_code"
            }
            """, clientId, clientSecret, authCode, REDIRECT_URI)
        );
        return connector.send();
    }


    public static Response refreshToken(String clientId, String clientSecret, String refreshToken)
    throws IOException, InterruptedException {
        Connector connector = new PostConnector(CODE_EXCHANGE_ENDPOINT);
        connector.addPostParam(String.format("""
            {
                client_id: '%s',
                client_secret: '%s',
                refresh_token: '%s',
                redirect_uri: '%s',
                grant_type: "authorization_code"
            }
            """, clientId, clientSecret, refreshToken, REDIRECT_URI)
        );
        return connector.send();
    }

}
