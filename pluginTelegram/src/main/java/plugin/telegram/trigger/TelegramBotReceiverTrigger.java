package plugin.telegram.trigger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import md.dev.options.Options;
import md.dev.options.description.OptionDescription;
import md.dev.pipeline.exception.PipelineGenericException;
import md.dev.plugin.annotation.Trigger;
import md.dev.plugin.exception.OptionException;
import md.dev.trigger.AbstractPeriodicWebTrigger;
import md.dev.trigger.output.TriggerOutput;
import md.dev.webapi.WebApi;
import md.dev.webapi.configuration.WebApiConfiguration;
import md.dev.webapi.configuration.WebApiResponse;
import org.json.JSONObject;
import plugin.telegram.model.TelegramUpdate;
import plugin.telegram.webapi.TelegramWebApi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;


@Trigger(
    id = "receive",
    webApi = TelegramWebApi.class,
    outputType = TelegramUpdate.class,
    description = "Send in the pipeline telegram messages received by the bot (see <a " +
        "href='src/main/resources/config.md'>configuration</a>)."
)
public class TelegramBotReceiverTrigger extends AbstractPeriodicWebTrigger<TelegramUpdate> {
    private String accessToken;
    private Optional<Long> filterChatId;
    private long lastUpdateId;


    public TelegramBotReceiverTrigger(
        TriggerOutput<TelegramUpdate> triggerOutputToUse, WebApi webApi) {
        super(triggerOutputToUse, webApi);
    }


    @Override
    public void loadState() {
        String savedLastUpdateId = getProperty("lastUpdateId");
        if (savedLastUpdateId != null) {
            this.lastUpdateId = Long.parseLong(savedLastUpdateId);
        }
    }


    @Override
    public void saveState() {
        setProperty("lastUpdateId", "" + lastUpdateId);
    }


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
                "chatId",
                "Filter messages only sent by this chatId.",
                Long.class,
                "null",
                false
            )
        ));
    }


    @Override
    protected void loadInstanceOptions(Options options) {
        if (options != null && options.has("tokenVar")) {
            accessToken = System.getenv(options.getString("tokenVar"));
        }
        if (options != null && options.has("chatId")) {
            filterChatId = Optional.of((long) options.get("chatId"));
        }

        if (accessToken == null) {
            throw new OptionException(
                "tokenVar",
                String.format("cannot find global variable '%s'", options.getString("tokenVar"))
            );
        }
    }


    @Override
    protected void initializeClassOptions() {
        filterChatId = Optional.empty();
    }


    @Override
    protected void listen() {
        WebApiConfiguration webApiConfiguration = new WebApiConfiguration();
        webApiConfiguration.insert("token", accessToken);
        webApiConfiguration.insert("action", TelegramWebApi.TelegramWebApiAction.RECEIVE);
        if (lastUpdateId != 0) {
            webApiConfiguration.insert("lastUpdateId", lastUpdateId);
        }
        webApi.configure(webApiConfiguration);

        WebApiResponse webApiResponse;
        try {
            webApiResponse = webApi.perform();
        } catch (Exception e) {
            log.error("error while performing webapi action", e);
            throw new PipelineGenericException(e.getMessage());
        }
        JSONObject jsonResponse = new JSONObject(webApiResponse.getResponse());
        for (Object jsonMessages : jsonResponse.getJSONArray("result")) {
            ObjectMapper mapper = new ObjectMapper();
            TelegramUpdate telegramUpdate;
            try {
                telegramUpdate = mapper.readValue(jsonMessages.toString(), TelegramUpdate.class);
            } catch (JsonProcessingException e) {
                log.error("error while writing update to string", e);
                throw new PipelineGenericException(e.getMessage());
            }

            if (filterChatId.isPresent() &&
                !filterChatId.get().equals(telegramUpdate.getMessage().getFrom().getId())) {
                continue;
            }

            triggerOutput.setValue(telegramUpdate);
            triggerPipelines();

            lastUpdateId = telegramUpdate.getUpdate_id();
        }
        saveState();
    }
}
