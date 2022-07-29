package plugin.base.webapi;

import md.dev.connector.Connector;
import md.dev.connector.GetConnector;
import md.dev.response.Response;
import md.dev.webapi.AbstractWebApi;
import md.dev.webapi.configuration.WebApiConfiguration;
import md.dev.webapi.configuration.WebApiResponse;

import java.io.IOException;

public class SimpleHttpGetWebApi extends AbstractWebApi {
    private Connector connector;

    @Override
    public WebApiResponse perform() {
        try {
            Response response = connector.send();
            return new WebApiResponse(response.getCode(), response.getResponse());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void configure(WebApiConfiguration configuration) {
        connector = new GetConnector((String) configuration.get("url"));
    }
}
