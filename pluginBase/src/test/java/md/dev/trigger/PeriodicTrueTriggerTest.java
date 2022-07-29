package md.dev.trigger;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import plugin.base.trigger.PeriodicTrueTrigger;

public class PeriodicTrueTriggerTest extends BaseTriggerUnitTest {

    @Before
    public void init() {
        super.init();
        trigger = new PeriodicTrueTrigger(triggerOutput);
    }

    @Test
    public void shouldTriggerPipeline() {
        trigger.loadOptions(options);
        trigger.linkPipeline(pipeline);
        trigger.startListening();
        Mockito.verify(pipeline, Mockito.timeout(threadStartWaitTime)).process(Mockito.any());
    }

    @Test
    public void shouldTriggerPipelineWithoutLoadOptions() {
        trigger.linkPipeline(pipeline);
        trigger.startListening();
        Mockito.verify(pipeline, Mockito.timeout(threadStartWaitTime)).process(Mockito.any());
    }

    @Test
    public void shouldTriggerPipelineOneTimeOnly() {
        insertInMockedOptions("repeat", 1);
        trigger.loadOptions(options);
        trigger.linkPipeline(pipeline);
        trigger.startListening();
        Mockito.verify(pipeline, Mockito.timeout(5000).times(1)).process(Mockito.any());
    }

    @Test
    public void shouldTriggerPipelineSpecifiedTimes() {
        int specifiedTimes = 5;
        insertInMockedOptions("repeat", specifiedTimes);
        insertInMockedOptions("period", 1);
        trigger.loadOptions(options);
        trigger.linkPipeline(pipeline);
        trigger.startListening();
        Mockito.verify(pipeline, Mockito.timeout(10000).times(specifiedTimes)).process(Mockito.any());
    }

    @Test
    public void shouldLoadDelayOption() {
        insertInMockedOptions("delay", 10);
        trigger.loadOptions(options);
        trigger.linkPipeline(pipeline);
        trigger.startListening();
        Mockito.verify(pipeline, Mockito.timeout(7000).times(0)).process(Mockito.any());
    }

    @Test
    public void shouldLoadPeriodOption() {
        insertInMockedOptions("period", 10);
        trigger.loadOptions(options);
        trigger.linkPipeline(pipeline);
        trigger.startListening();
        Mockito.verify(pipeline, Mockito.timeout(8000).times(1)).process(Mockito.any());
    }

    @Test
    public void shouldTriggerWithCorrectValue() {
        trigger.loadOptions(options);
        trigger.startListening();
        Mockito.verify(triggerOutput, Mockito.timeout(threadStartWaitTime)).setValue(Boolean.TRUE);
    }

    @Test
    public void shouldAcceptUnknownOption() {
        insertInMockedOptions("foo", "bar");
        trigger.loadOptions(options);
    }
}
