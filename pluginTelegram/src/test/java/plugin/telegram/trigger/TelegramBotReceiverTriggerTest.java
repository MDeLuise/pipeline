package plugin.telegram.trigger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import md.dev.log.LoggerHandler;
import md.dev.options.Options;
import md.dev.options.exception.MissingMandatoryOptionsException;
import md.dev.options.exception.OptionMismatchTypeException;
import md.dev.pipeline.Pipeline;
import md.dev.trigger.Trigger;
import md.dev.trigger.output.TriggerOutput;
import md.dev.webapi.WebApi;
import md.dev.webapi.configuration.WebApiResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import plugin.telegram.model.TelegramUpdate;

import java.util.ArrayList;
import java.util.List;

public class TelegramBotReceiverTriggerTest {
    private int threadStartWaitTime = 1500;
    private Options options;
    private TriggerOutput triggerOutput;
    private Pipeline pipeline;
    private Trigger trigger;
    private WebApi webApi;
    private WebApiResponse webApiResponse;
    private List<String> keys;
    private final String EXISTING_GLOBAL_VAR = "JAVA_HOME";
    private final String dummyResponse = createDummyTelegramResponse();

    @Before
    public void setUp() {
        options = Mockito.mock(Options.class);
        triggerOutput = Mockito.mock(TriggerOutput.class);
        pipeline = Mockito.mock(Pipeline.class);
        keys = new ArrayList<>();
        webApi = Mockito.mock(WebApi.class);
        webApiResponse = Mockito.mock(WebApiResponse.class);
        Mockito.when(webApiResponse.getResponse()).thenReturn(
                String.format("{result: [%s]}", dummyResponse));
        Mockito.when(webApi.perform()).thenAnswer(foo -> webApiResponse);
        trigger = new TelegramBotReceiverTrigger(triggerOutput, webApi);
        LoggerHandler.disableLog();
    }

    @Test
    public void shouldTriggerPipeline() {
        insertInMockedOptions("tokenVar", EXISTING_GLOBAL_VAR);
        trigger.loadOptions(options);
        trigger.linkPipeline(pipeline);
        trigger.startListening();
        Mockito.verify(pipeline, Mockito.timeout(threadStartWaitTime)).process(Mockito.any());
    }

    @Test
    public void shouldTriggerPipelineOneTimeOnly() {
        insertInMockedOptions("repeat", 1);
        insertInMockedOptions("tokenVar", EXISTING_GLOBAL_VAR);
        trigger.loadOptions(options);
        trigger.linkPipeline(pipeline);
        trigger.startListening();
        Mockito.verify(
                pipeline,
                Mockito.timeout(threadStartWaitTime).times(1)
        ).process(Mockito.any());
    }

    @Test
    public void shouldTriggerPipelineSpecifiedTimes() {
        int specifiedTimes = 5;
        insertInMockedOptions("repeat", specifiedTimes);
        insertInMockedOptions("period", 1);
        insertInMockedOptions("tokenVar", EXISTING_GLOBAL_VAR);
        trigger.loadOptions(options);
        trigger.linkPipeline(pipeline);
        trigger.startListening();
        Mockito.verify(
                pipeline,
                Mockito.timeout(10000).times(specifiedTimes)
        ).process(Mockito.any());
    }

    @Test
    public void shouldLoadDelayOption() {
        insertInMockedOptions("delay", 10);
        insertInMockedOptions("tokenVar", EXISTING_GLOBAL_VAR);
        trigger.loadOptions(options);
        trigger.linkPipeline(pipeline);
        trigger.startListening();
        Mockito.verify(
                pipeline,
                Mockito.timeout(7000).times(0)
        ).process(Mockito.any());
    }

    @Test
    public void shouldLoadPeriodOption() {
        insertInMockedOptions("period", 10);
        insertInMockedOptions("tokenVar", EXISTING_GLOBAL_VAR);
        trigger.loadOptions(options);
        trigger.linkPipeline(pipeline);
        trigger.startListening();
        Mockito.verify(
                pipeline,
                Mockito.timeout(8000).times(1)
        ).process(Mockito.any());
    }

    @Test
    public void shouldTriggerWithCorrectValue() throws JsonProcessingException {
        insertInMockedOptions("tokenVar", EXISTING_GLOBAL_VAR);
        trigger.loadOptions(options);
        trigger.startListening();
        ArgumentCaptor<TelegramUpdate> argument =
                ArgumentCaptor.forClass(TelegramUpdate.class);
        Mockito.verify(triggerOutput, Mockito.timeout(2000)).setValue(argument.capture());
        areTheSame(createDummyTelegramResponse(), argument.getValue());
    }

    @Test
    public void shouldFilterOutMessagesByChatId() {
        insertInMockedOptions("tokenVar", EXISTING_GLOBAL_VAR);
        insertInMockedOptions("chatId", 1l);
        trigger.loadOptions(options);
        trigger.linkPipeline(pipeline);
        trigger.startListening();
        Mockito.verify(pipeline, Mockito.timeout(threadStartWaitTime).times(0))
                .process(Mockito.any());
    }

    @Test
    public void shouldFilterInMessagesByChatId() {
        insertInMockedOptions("tokenVar", EXISTING_GLOBAL_VAR);
        insertInMockedOptions("chatId", 0l);
        trigger.loadOptions(options);
        trigger.linkPipeline(pipeline);
        trigger.startListening();
        Mockito.verify(pipeline, Mockito.timeout(threadStartWaitTime)).process(Mockito.any());
    }

    @Test
    public void shouldAcceptUnknownOption() {
        insertInMockedOptions("foo", "bar");
        insertInMockedOptions("tokenVar", EXISTING_GLOBAL_VAR);
        trigger.loadOptions(options);
    }

    @Test(expected = OptionMismatchTypeException.class)
    public void shouldThrowExceptionIfWrongOptionType() {
        insertInMockedOptions("tokenVar", true);
        trigger.loadOptions(options);
    }

    @Test(expected = MissingMandatoryOptionsException.class)
    public void shouldThrowExceptionIfMissingMandatoryOption() {
        trigger.loadOptions(options);
    }


    private void insertInMockedOptions(String key, Object value) {
        keys.add(key);
        Mockito.when(options.getKeys()).thenAnswer(foo -> keys);
        Mockito.when(options.get(key)).thenAnswer(foo -> value);
        Mockito.when(options.getString(key)).thenAnswer(foo -> value);
        Mockito.when(options.has(key)).thenAnswer(foo -> true);
        Mockito.when(options.size()).thenAnswer(foo -> keys.size());
    }

    private String createDummyTelegramResponse() {
        return """
                {
                "update_id" : 0,
                  "message" : {
                    "message_id" : 0,
                    "from" : {
                      "id" : 0,
                      "is_bot" : "false",
                      "first_name" : "Foo",
                      "username" : "foo",
                      "language_code" : "en"
                    },
                    "chat" : {
                      "id" : 0,
                      "first_name" : "Foo",
                      "username" : "foo",
                      "type" : "private"
                    },
                    "date" : 0,
                    "text" : "42"
                  }
                }""";
    }

    private void areTheSame(String jsonStr, TelegramUpdate telegramUpdate)
            throws JsonProcessingException {
        ObjectMapper objMapper = new ObjectMapper();
        TelegramUpdate telegramUpdateToCompareWith =
                objMapper.readValue(jsonStr, TelegramUpdate.class);
        Assert.assertEquals(telegramUpdateToCompareWith, telegramUpdate);
    }
}