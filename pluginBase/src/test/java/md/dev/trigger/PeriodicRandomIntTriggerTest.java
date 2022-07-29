package md.dev.trigger;

import md.dev.options.exception.OptionMismatchTypeException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import plugin.base.trigger.PeriodicRandomIntTrigger;

public class PeriodicRandomIntTriggerTest extends BaseTriggerUnitTest {

    @Before
    public void init() {
        super.init();
        trigger = new PeriodicRandomIntTrigger(triggerOutput);
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
        Mockito.verify(pipeline, Mockito.timeout(threadStartWaitTime).times(1)).process(Mockito.any());
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
        int max = 2;
        int min = 0;
        insertInMockedOptions("min", min);
        insertInMockedOptions("max", max);
        trigger.loadOptions(options);

        trigger.startListening();

        Mockito.verify(triggerOutput, Mockito.timeout(threadStartWaitTime)).setValue(
                Mockito.argThat((Integer passedInt) -> passedInt >= min && passedInt <= max));
    }

    @Test
    public void shouldAcceptUnknownOption() {
        insertInMockedOptions("foo", "bar");
        trigger.loadOptions(options);
    }

    @Test(expected = OptionMismatchTypeException.class)
    public void shouldThrowExceptionIfWrongOptionType() {
        insertInMockedOptions("min", true);
        trigger.loadOptions(options);
    }
}
