package ${package}.modifier.transformer;

import md.dev.log.LoggerHandler;
import md.dev.options.Options;
import md.dev.trigger.output.TriggerOutput;
import md.dev.modifier.transformer.Transformer;

import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

abstract class BaseTransformerTest {
    protected Transformer transformer;
    protected TriggerOutput triggerOutput;
    protected Options options;
    protected ArgumentCaptor<TriggerOutput> argumentCaptor;
    protected List<String> keys;


    public void init() {
        triggerOutput = Mockito.mock(TriggerOutput.class);
        argumentCaptor = ArgumentCaptor.forClass(TriggerOutput.class);
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

    protected void insertInMockedOptionsString(String key, String value) {
        keys.add(key);
        Mockito.when(options.getKeys()).thenAnswer(foo -> keys);
        Mockito.when(options.getString(key)).thenAnswer(foo -> value);
        Mockito.when(options.get(key)).thenAnswer(foo -> value);
        Mockito.when(options.has(key)).thenAnswer(foo -> true);
        Mockito.when(options.size()).thenAnswer(foo -> keys.size());
    }

    protected void insertInMockedOptionsInt(String key, int value) {
        keys.add(key);
        Mockito.when(options.getKeys()).thenAnswer(foo -> keys);
        Mockito.when(options.getInt(key)).thenAnswer(foo -> value);
        Mockito.when(options.get(key)).thenAnswer(foo -> value);
        Mockito.when(options.has(key)).thenAnswer(foo -> true);
        Mockito.when(options.size()).thenAnswer(foo -> keys.size());
    }

    protected void insertInMockedTriggerOutput(Object value) {
        Mockito.when(triggerOutput.getValue()).thenAnswer(foo -> value);
    }
}
