package md.dev.modifier.filter;

import md.dev.log.LoggerHandler;
import md.dev.options.Options;
import md.dev.trigger.output.TriggerOutput;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;


abstract class BaseFilterTest {
    Filter filter;
    TriggerOutput triggerOutput;
    Options options;
    List<String> keys;


    public void init() {
        triggerOutput = Mockito.mock(TriggerOutput.class);
        options = Mockito.mock(Options.class);
        keys = new ArrayList<>();
        LoggerHandler.disableLog();
    }

    protected void insertInMockedOptions(String key, Object value) {
        keys.add(key);
        Mockito.when(options.getKeys()).thenAnswer(foo -> keys);
        Mockito.when(options.get(key)).thenAnswer(foo -> value);
        Mockito.when(options.has(key)).thenAnswer(foo -> true);
        Mockito.when(options.size()).thenAnswer(foo -> keys.size());
    }
}
