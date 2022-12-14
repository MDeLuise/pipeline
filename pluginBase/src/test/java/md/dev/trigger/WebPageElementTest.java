package md.dev.trigger;

import md.dev.options.exception.MissingMandatoryOptionsException;
import md.dev.options.exception.OptionMismatchTypeException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import plugin.base.trigger.WebPageElementTrigger;

public class WebPageElementTest extends BaseWebTriggerUnitTest {

    @Before
    public void init() {
        super.init();
        trigger = new WebPageElementTrigger(triggerOutput, webApi);
    }

    @Test
    public void shouldTriggerPipeline() {
        setWebResponseValue("foo");
        insertInMockedOptions("url", "foo");
        trigger.loadOptions(options);
        trigger.linkPipeline(pipeline);
        trigger.startListening();
        Mockito.verify(pipeline, Mockito.timeout(threadStartWaitTime)).process(Mockito.any());
    }

    @Test(expected = MissingMandatoryOptionsException.class)
    public void shouldNotTriggerPipelineWithoutLoadOptions() {
        setWebResponseValue("foo");
        trigger.linkPipeline(pipeline);
        insertInMockedOptions("url", "foo");
        trigger.startListening();
        Mockito.verify(pipeline, Mockito.timeout(threadStartWaitTime)).process(Mockito.any());
    }

    @Test
    public void shouldTriggerPipelineOneTimeOnly() {
        setWebResponseValue("foo");
        insertInMockedOptions("repeat", 1);
        insertInMockedOptions("url", "foo");
        trigger.loadOptions(options);
        trigger.linkPipeline(pipeline);
        trigger.startListening();
        Mockito.verify(
                pipeline, Mockito.timeout(threadStartWaitTime).times(1)
        ).process(Mockito.any());
    }

    @Test
    public void shouldTriggerPipelineSpecifiedTimes() {
        int wantedTimes = 5;
        insertInMockedOptions("repeat", wantedTimes);
        insertInMockedOptions("period", 1);
        insertInMockedOptions("onChange", false);
        insertInMockedOptions("url", "foo");
        setWebResponseValue("foo");

        trigger.loadOptions(options);
        trigger.linkPipeline(pipeline);
        trigger.startListening();
        Mockito.verify(
                pipeline, Mockito.timeout(wantedTimes * 1000 * 2).times(wantedTimes)
        ).process(Mockito.any());
    }

    @Test
    public void shouldLoadDelayOption() {
        insertInMockedOptions("delay", 10);
        insertInMockedOptions("url", "foo");
        trigger.loadOptions(options);
        trigger.linkPipeline(pipeline);
        setWebResponseValue("foo");
        trigger.startListening();
        Mockito.verify(
                pipeline, Mockito.after(8000).times(0)
        ).process(Mockito.any());
    }

    @Test
    public void shouldLoadPeriodOption() {
        insertInMockedOptions("period", 10);
        insertInMockedOptions("onChange", false);
        setWebResponseValue("foo");
        insertInMockedOptions("url", "foo");
        trigger.loadOptions(options);
        trigger.linkPipeline(pipeline);
        trigger.startListening();
        Mockito.verify(
                pipeline, Mockito.after(9000).times(1)
        ).process(Mockito.any());
    }

    @Test
    public void shouldLoadOnChangeOption() {
        insertInMockedOptions("onChange", true);
        insertInMockedOptions("period", 1);
        setWebResponseValue("foo");
        insertInMockedOptions("url", "foo");
        trigger.loadOptions(options);
        trigger.linkPipeline(pipeline);
        trigger.startListening();
        Mockito.verify(
                pipeline, Mockito.after(5000).times(1)
        ).process(Mockito.any());
    }

    @Test
    public void shouldTriggerWithCorrectValue() {
        String value = "42";
        insertInMockedOptions("onChange", false);
        insertInMockedOptions("url", "foo");
        setWebResponseValue(value);
        trigger.loadOptions(options);
        trigger.startListening();
        Mockito.verify(triggerOutput, Mockito.timeout(threadStartWaitTime)).setValue(value);
    }

    @Test
    public void shouldAcceptUnknownOption() {
        insertInMockedOptions("foo", "bar");
        insertInMockedOptions("url", "foo");
        trigger.loadOptions(options);
    }

    @Test(expected = OptionMismatchTypeException.class)
    public void shouldThrowExceptionIfWrongOptionType() {
        insertInMockedOptions("onChange", 10);
        insertInMockedOptions("url", "foo");
        trigger.loadOptions(options);
    }

    @Test(expected = MissingMandatoryOptionsException.class)
    public void shouldThrowExceptionIfMissingMandatoryOption() {
        trigger.loadOptions(options);
    }

}
