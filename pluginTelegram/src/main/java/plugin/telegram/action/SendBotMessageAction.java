package plugin.telegram.action;

import md.dev.action.AbstractWebAction;
import md.dev.options.Options;
import md.dev.options.description.OptionDescription;
import md.dev.pipeline.exception.PipelineGenericException;
import md.dev.plugin.annotation.Action;
import md.dev.plugin.exception.OptionException;
import md.dev.trigger.output.TriggerOutput;
import md.dev.webapi.WebApi;
import md.dev.webapi.configuration.WebApiConfiguration;
import plugin.telegram.webapi.TelegramWebApi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

@Action(
        id = "send",
        webApi = TelegramWebApi.class,
        inputType = String.class,
        description = "Send telegram messages via bot (see <a href='src/main/resources/config" +
                ".md'>configuration</a>)."
)
public class SendBotMessageAction extends AbstractWebAction<String> {
    private String accessToken;
    private String chatId;


    public SendBotMessageAction(WebApi webApi) {
        super(webApi);
    }

    @Override
    protected void loadInstanceOptions(Options options) {
        if (options != null && options.has("tokenVar")) {
            accessToken = System.getenv(options.getString("tokenVar"));
        }
        if (options != null && options.has("chatIdVar")) {
            chatId = System.getenv(options.getString("chatIdVar"));
        }

        if (accessToken == null) {
            throw new OptionException(
                    "tokenVar",
                    String.format("cannot find global variable '%s'", options.getString("tokenVar"))
            );
        }

        if (chatId == null) {
            throw new OptionException(
                    "chatIdVar",
                    String.format(
                            "cannot find global variable '%s'", options.getString("chatIdVar")
                    )
            );
        }
    }

    @SuppressWarnings("checkstyle:LineLength")
    @Override
    protected Collection<OptionDescription> acceptedClassOptions() {
        return new ArrayList<>(Arrays.asList(
                new OptionDescription(
                        "tokenVar",
                        "Name of the global var containing the bot access token.",
                        String.class,
                        "null",
                        true
                ),
                new OptionDescription(
                        "chatIdVar",
                        "Name of the global var containing the chat id to whom the bot will sent " +
                                "messages.",
                        String.class,
                        "null",
                        true
                )
        ));
    }

    @Override
    public void doAction(TriggerOutput<String> triggerOutput) {
        WebApiConfiguration webApiConfiguration = new WebApiConfiguration();
        webApiConfiguration.insert("action", TelegramWebApi.TelegramWebApiAction.SEND);
        webApiConfiguration.insert("token", accessToken);
        webApiConfiguration.insert("chatId", chatId);
        webApiConfiguration.insert("text", triggerOutput.getValue());
        webApi.configure(webApiConfiguration);
        try {
            webApi.perform();
        } catch (Exception e) {
            LOG.error("error while executing webapi", e);
            throw new PipelineGenericException(e.getMessage());
        }
    }

    @Override
    public void initializeClassOptions() {

    }
}
