package ${package}.action;

import md.dev.action.Action;
import md.dev.log.LoggerHandler;
import md.dev.options.Options;
import md.dev.trigger.output.TriggerOutput;
import md.dev.webapi.WebApi;
import md.dev.webapi.configuration.WebApiResponse;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;


public class BaseActionUnitTest {
    protected Options options;
    protected TriggerOutput triggerOutput;
    protected static List<String> keys;
    protected Action action;
    protected WebApi webApi;
    protected WebApiResponse webApiResponse;


    protected void init() {
        options = Mockito.mock(Options.class);
        triggerOutput = Mockito.mock(TriggerOutput.class);
        keys = new ArrayList<>();
        webApi = Mockito.mock(WebApi.class);
        webApiResponse = Mockito.mock(WebApiResponse.class);
        Mockito.when(webApi.perform()).thenAnswer(foo -> webApiResponse);
        LoggerHandler.disableLog();
    }


    protected void insertInMockedOptions(String key, Object value) {
        keys.add(key);
        Mockito.when(options.get(key)).thenAnswer(foo -> value);
        Mockito.when(options.getString(key)).thenAnswer(foo -> value);
        Mockito.when(options.has(key)).thenAnswer(foo -> true);
        Mockito.when(options.getKeys()).thenAnswer(foo -> keys);
        Mockito.when(options.size()).thenAnswer(foo -> keys.size());
    }


    protected void insertInMockedTriggerOutput(Object value) {
        Mockito.when(triggerOutput.getValue()).thenAnswer(foo -> value);
    }
}
