package ${package}.webapi;

import md.dev.connector.Connector;
import md.dev.connector.GetConnector;
import md.dev.connector.PostConnector;
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
 * This FooWebApi sento a GET request or a POST request to different endpoints.
 * A simpler WebApi whould do only one request type, thus FooWebApiAction whould not be required.
 */
public class FooWebApi1 extends AbstractWebApi {

    public enum FooWebApiAction {
        SEND, RECEIVE
    }

    private String token;
    private String text;
    private FooWebApiAction action;
    private final String readEndpoint = "https://endpoint1.com";
    private final String sendEndpoint =
        "https://endpoint2.com?token=%s&text=%s";


    /**
     * Load used options.
     * @param webApiConfiguration: webApiConfiguration to load
     */
    @Override
    public void configure(WebApiConfiguration webApiConfiguration) {
        if (webApiConfiguration.contains("text")) {
            text = (String) webApiConfiguration.get("text");
        }
        if (webApiConfiguration.contains("action")) {
            token = (String) webApiConfiguration.get("token");
        }
        if (webApiConfiguration.contains("action")) {
            action = (FooWebApiAction) webApiConfiguration.get("action");
        }
    }

    /**
     * Perform the purpose of the WebApi.
     * @return response of the performed request.
     */
    @Override
    public WebApiResponse perform() {
        String formattedEndpoint;
        Connector connector;
        if (action == FooWebApiAction.RECEIVE) {
            formattedEndpoint = String.format(readEndpoint);
            connector = new GetConnector(formattedEndpoint);
        } else {
            formattedEndpoint = String.format(sendEndpoint, token, text);
            connector = new PostConnector(formattedEndpoint);
        }
        return performAction(connector);
    }


    private WebApiResponse performAction(Connector connector) {
        Response response = null;
        try {
            response = connector.send();
        } catch (IOException | InterruptedException e) {
            LOG.error("error while performing web api action", e);
            return new WebApiResponse(500, e.getMessage());
        }
        return new WebApiResponse(response.getCode(), response.getResponse());
    }
}
