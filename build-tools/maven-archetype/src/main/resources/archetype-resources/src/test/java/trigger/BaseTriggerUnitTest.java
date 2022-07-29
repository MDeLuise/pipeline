package ${package}.trigger;

import md.dev.log.LoggerHandler;
import md.dev.options.Options;
import md.dev.pipeline.Pipeline;

import md.dev.trigger.output.TriggerOutput;
import md.dev.trigger.Trigger;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

abstract class BaseTriggerUnitTest {
    protected int threadStartWaitTime = 1500;
    protected Options options;
    protected TriggerOutput triggerOutput;
    protected Pipeline pipeline;
    protected Trigger trigger;
    private List<String> keys;


    protected void init() {
        options = Mockito.mock(Options.class);
        triggerOutput = Mockito.mock(TriggerOutput.class);
        pipeline = Mockito.mock(Pipeline.class);
        keys = new ArrayList<>();
        LoggerHandler.disableLog();
    }

    protected void insertInMockedOptions(String key, Object value) {
        keys.add(key);
        Mockito.when(options.getKeys()).thenAnswer(foo -> keys);
        Mockito.when(options.get(key)).thenAnswer(foo -> value);
        Mockito.when(options.getString(key)).thenAnswer(foo -> value);
        Mockito.when(options.has(key)).thenAnswer(foo -> true);
        Mockito.when(options.size()).thenAnswer(foo -> keys.size());
    }
}
