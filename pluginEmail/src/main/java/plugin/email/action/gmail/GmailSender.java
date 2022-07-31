package plugin.email.action.gmail;

import md.dev.options.Options;
import md.dev.options.description.OptionDescription;
import md.dev.pipeline.RuntimeConfigurableEntity;
import md.dev.pipeline.exception.PipelineGenericException;
import md.dev.plugin.annotation.Action;
import md.dev.plugin.exception.OptionException;
import md.dev.trigger.output.TriggerOutput;
import md.dev.webapi.WebApi;
import md.dev.webapi.configuration.WebApiConfiguration;
import md.dev.webapi.configuration.WebApiResponse;
import plugin.email.action.AbstractMailSender;
import plugin.email.action.gmail.authentication.OAuth2TokenHandler;
import plugin.email.webapi.SendGmailWebApi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

@Action(
        id = "gSend",
        inputType = String.class,
        webApi = SendGmailWebApi.class,
        description = "Send an email using Gmail (see <a href='src/main/resources/gmail-config" +
                ".md'>configuration</a>)."
)
public class GmailSender extends AbstractMailSender implements RuntimeConfigurableEntity {
    private String clientId;
    private String clientSecret;
    private OAuth2TokenHandler tokenHandler;


    public GmailSender(WebApi webApi) {
        super(webApi);
    }

    @Override
    protected void initializeImplementationOptions() {
        tokenHandler = OAuth2TokenHandler.getInstance();
    }

    @Override
    protected void loadImplementationOptions(Options options) {
        if (options.has("clientIdVar")) {
            clientId = System.getenv(options.getString("clientIdVar"));
        }
        if (options.has("clientSecretVar")) {
            clientSecret = System.getenv(options.getString("clientSecretVar"));
        }

        if (clientId == null) {
            throw new OptionException(
                    "tokenVar",
                    String.format(
                            "cannot find global variable '%s'", options.getString("clientIdVar")
                    )
            );
        }

        if (clientSecret == null) {
            throw new OptionException(
                    "chatIdVar",
                    String.format(
                            "cannot find global variable '%s'", options.getString("clientSecretVar")
                    )
            );
        }
    }

    @Override
    protected Collection<OptionDescription> acceptedImplementationOptions() {
        return new ArrayList<>(Arrays.asList(
                new OptionDescription(
                        "clientIdVar",
                        "Global var containing the client id of the application.",
                        String.class,
                        "null",
                        true
                ),
                new OptionDescription(
                        "clientSecretVar",
                        "Global var containing the client secret of the application.",
                        String.class,
                        "null",
                        true
                )
        ));
    }

    @Override
    public void doAction(TriggerOutput<String> triggerOutput) {
        log.debug("[doAction] variable 'to': {}", to);
        for (String toAddress : to.split(",")) {
            WebApiConfiguration webApiConfiguration = new WebApiConfiguration();
            webApiConfiguration.insert("clientId", clientId);
            webApiConfiguration.insert("clientSecret", clientSecret);
            webApiConfiguration.insert("to", toAddress);
            webApiConfiguration.insert("from", from);
            webApiConfiguration.insert("subject", subject);
            webApiConfiguration.insert(
                    "text",
                    text.isEmpty() ?
                            triggerOutput.getValue() :
                            String.format(text.get(), triggerOutput.getValue())
            );
            webApiConfiguration.insert("tokenHandler", tokenHandler);

            webApi.configure(webApiConfiguration);
            WebApiResponse webApiResponse;
            try {
                webApiResponse = webApi.perform();
            } catch (Exception e) {
                throw new PipelineGenericException(e.getMessage());
            }

            if (!webApiResponse.isOk()) {
                throw new PipelineGenericException(webApiResponse.getResponse());
            }
        }
    }

    @Override
    public boolean isConfigured() {
        return tokenHandler.isInitialized();
    }

    @Override
    public void configure() {
        tokenHandler.setClientId(clientId);
        tokenHandler.setClientSecret(clientSecret);
        tokenHandler.getToken();
    }
}
