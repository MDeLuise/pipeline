package md.dev.webapi;

import md.dev.webapi.configuration.WebApiConfiguration;
import md.dev.webapi.configuration.WebApiResponse;

public interface WebApi {
    WebApiResponse perform();

    void configure(WebApiConfiguration configuration);
}
