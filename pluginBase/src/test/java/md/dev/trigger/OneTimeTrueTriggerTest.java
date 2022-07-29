package md.dev.trigger;


import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import plugin.base.trigger.OneTimeTrueTrigger;

public class OneTimeTrueTriggerTest extends BaseTriggerUnitTest {

    @Before
    public void init() {
        super.init();
        trigger = new OneTimeTrueTrigger(triggerOutput);
    }

    @Test
    public void shouldTriggerController() {
        trigger.loadOptions(options);
        trigger.linkPipeline(pipeline);
        trigger.startListening();
        Mockito.verify(pipeline, Mockito.timeout(threadStartWaitTime)).process(Mockito.any());
    }

    @Test
    public void shouldTriggerControllerWithoutLoadOptions() {
        trigger.linkPipeline(pipeline);
        trigger.startListening();
        Mockito.verify(pipeline, Mockito.timeout(threadStartWaitTime)).process(Mockito.any());
    }

    @Test
    public void shouldTriggerControllerOneTimeOnly() {
        trigger.loadOptions(options);
        trigger.linkPipeline(pipeline);
        trigger.startListening();
        Mockito.verify(pipeline, Mockito.after(threadStartWaitTime * 2).times(1)).process(Mockito.any());
    }

    @Test
    public void shouldTriggerControllerAfterDelay() {
        insertInMockedOptions("delay", 3);
        trigger.loadOptions(options);
        trigger.linkPipeline(pipeline);
        trigger.startListening();
        Mockito.verify(pipeline, Mockito.after(2000).times(0)).process(Mockito.any());
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
        trigger.startListening();
    }
}
