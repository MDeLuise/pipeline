package plugin.telegram.webapi;

import md.dev.connector.Connector;
import md.dev.connector.GetConnector;
import md.dev.connector.PostConnector;
import md.dev.response.Response;
import md.dev.webapi.AbstractWebApi;
import md.dev.webapi.configuration.WebApiConfiguration;
import md.dev.webapi.configuration.WebApiResponse;

import java.io.IOException;

public class TelegramWebApi extends AbstractWebApi {

    public enum TelegramWebApiAction {
        SEND, RECEIVE
    }

    private final String readEndpoint = "https://api.telegram.org/bot%s/getUpdates";
    private final String sendEndpoint =
            "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s";
    private String token;
    private String chatId;
    private String text;
    private TelegramWebApiAction action;
    private long lastUpdateId;

    @Override
    public void configure(WebApiConfiguration configuration) {
        if (configuration.contains("token")) {
            token = (String) configuration.get("token");
        }
        if (configuration.contains("chatId")) {
            chatId = (String) configuration.get("chatId");
        }
        if (configuration.contains("text")) {
            text = (String) configuration.get("text");
        }
        if (configuration.contains("action")) {
            action = (TelegramWebApiAction) configuration.get("action");
        }
        if (configuration.contains("lastUpdateId")) {
            lastUpdateId = (long) configuration.get("lastUpdateId");
        }
    }

    @Override
    public WebApiResponse perform() {
        String formattedEndpoint;
        Connector connector;
        if (action == TelegramWebApiAction.RECEIVE) {
            formattedEndpoint = String.format(readEndpoint, token);
            if (lastUpdateId != 0) {
                formattedEndpoint += "?offset=" + lastUpdateId;
            }
            connector = new GetConnector(formattedEndpoint);
        } else {
            formattedEndpoint = String.format(sendEndpoint, token, chatId, text);
            connector = new PostConnector(formattedEndpoint);
        }
        return performAction(connector);
    }


    private WebApiResponse performAction(Connector connector) {
        Response response = null;
        try {
            response = connector.send();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return new WebApiResponse(200, response.getResponse());
    }
}
