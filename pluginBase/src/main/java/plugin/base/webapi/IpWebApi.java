package plugin.base.webapi;

import md.dev.connector.Connector;
import md.dev.connector.GetConnector;
import md.dev.response.Response;
import md.dev.webapi.AbstractWebApi;
import md.dev.webapi.configuration.WebApiConfiguration;
import md.dev.webapi.configuration.WebApiResponse;

import java.io.IOException;

public class IpWebApi extends AbstractWebApi {
    private final String urlToCheckIp = "https://api64.ipify.org";


    @SuppressWarnings("checkstyle:MagicNumber")
    @Override
    public WebApiResponse perform() {
        Connector connector = new GetConnector(urlToCheckIp);
        Response response = null;
        try {
            response = connector.send();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return new WebApiResponse(200, response.getResponse());
    }


    @Override
    public void configure(WebApiConfiguration configuration) {

    }
}
