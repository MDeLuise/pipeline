package ${package}.webapi;

import md.dev.connector.Connector;
import md.dev.connector.GetConnector;
import md.dev.response.Response;
import md.dev.webapi.AbstractWebApi;
import md.dev.webapi.configuration.WebApiConfiguration;
import md.dev.webapi.configuration.WebApiResponse;

import java.io.IOException;

@SuppressWarnings({
      "checkstyle:NonEmptyAtclauseDescription",
      "checkstyle:JavadocStyle",
      "checkstyle:JavadocTagContinuationIndentation",
      "checkstyle:JavadocParagraph",
      "checkstyle:InvalidJavadocPosition",
      "checkstyle:AtclauseOrder",
      "checkstyle:SingleLineJavadoc"
})
/**
 * This is an example of WebAction.
 * This FooWebApi send a GET request to a given endpoints.
 */
public class FooWebApi0 extends AbstractWebApi {
    private Connector connector;


    /**
     * Perform the purpose of the WebApi.
     * @return response of the performed request.
     */
    @Override
    public WebApiResponse perform() {
        try {
            Response response = connector.send();
            return new WebApiResponse(response.getCode(), response.getResponse());
        } catch (IOException | InterruptedException e) {
            LOG.error("error while performing web api action", e);
            return new WebApiResponse(500, e.getMessage());
        }
    }

    /**
     * Load used options.
     * @param webApiConfiguration: webApiConfiguration to load
     */
    @Override
    public void configure(WebApiConfiguration webApiConfiguration) {
        connector = new GetConnector((String) webApiConfiguration.get("url"));
    }
}
