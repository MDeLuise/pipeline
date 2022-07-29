package plugin.telegram.action;

import md.dev.action.Action;
import md.dev.log.LoggerHandler;
import md.dev.options.Options;
import md.dev.options.exception.OptionMismatchTypeException;
import md.dev.pipeline.TriggerOutputProcessor;
import md.dev.trigger.output.TriggerOutput;
import md.dev.webapi.WebApi;
import md.dev.webapi.configuration.WebApiConfiguration;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.HashSet;
import java.util.Set;

public class SendBotMessageActionTest {
    Action<String> action;
    TriggerOutput<String> triggerOutput;
    Set<String> keys;
    Options options;
    WebApi webApi;
    final String EXISTING_GLOBAL_VAR = "JAVA_HOME";

    @Before
    public void setUp() {
        triggerOutput = Mockito.mock(TriggerOutput.class);
        options = Mockito.mock(Options.class);
        webApi = Mockito.mock(WebApi.class);
        keys = new HashSet<>();
        action = new SendBotMessageAction(webApi);
        LoggerHandler.disableLog();
    }

    @Test
    public void shouldProcessNextInChain() {
        TriggerOutputProcessor triggerOutputProcessor = Mockito.mock(TriggerOutputProcessor.class);
        action.setNext(triggerOutputProcessor);
        insertInMockedOptions("tokenVar", EXISTING_GLOBAL_VAR);
        insertInMockedOptions("chatIdVar", EXISTING_GLOBAL_VAR);
        action.loadOptions(options);
        action.process(triggerOutput);
        Mockito.verify(
                triggerOutputProcessor,
                Mockito.times(1)
        ).process(triggerOutput);
    }

    @Test
    public void shouldAcceptUnknownOption() {
        insertInMockedOptions("foo", "bar");
        insertInMockedOptions("tokenVar", EXISTING_GLOBAL_VAR);
        insertInMockedOptions("chatIdVar", EXISTING_GLOBAL_VAR);
        action.loadOptions(options);
    }

    @Test(expected = OptionMismatchTypeException.class)
    public void shouldThrowExceptionIfWrongOptionType() {
        insertInMockedOptions("tokenVar", false);
        action.loadOptions(options);
    }

    @Test
    public void shouldTriggerCorrectly() {
        String triggerOutputValue = "42";
        insertInMockedOptions("tokenVar", EXISTING_GLOBAL_VAR);
        insertInMockedOptions("chatIdVar", EXISTING_GLOBAL_VAR);
        action.loadOptions(options);
        Mockito.when(triggerOutput.getValue()).thenAnswer(foo -> triggerOutputValue);
        action.doAction(triggerOutput);

        ArgumentCaptor<WebApiConfiguration> argument =
                ArgumentCaptor.forClass(WebApiConfiguration.class);
        Mockito.verify(webApi, Mockito.times(1)).
                configure(argument.capture());
        Assert.assertEquals(triggerOutputValue, argument.getValue().get("text"));
    }


    private void insertInMockedOptions(String key, Object value) {
        keys.add(key);
        Mockito.when(options.get(key)).thenAnswer(foo -> value);
        Mockito.when(options.getString(key)).thenAnswer(foo -> value);
        Mockito.when(options.has(key)).thenAnswer(foo -> true);
        Mockito.when(options.getKeys()).thenAnswer(foo -> keys);
        Mockito.when(options.size()).thenAnswer(foo -> keys.size());
    }
}