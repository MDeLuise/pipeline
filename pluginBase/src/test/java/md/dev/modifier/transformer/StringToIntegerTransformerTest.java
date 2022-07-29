package md.dev.modifier.transformer;

import md.dev.modifier.transformer.exception.TransformationException;
import md.dev.pipeline.TriggerOutputProcessor;
import md.dev.trigger.output.TriggerOutput;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import plugin.base.modifier.transformer.StringToIntegerTransformer;

public class StringToIntegerTransformerTest extends BaseTransformerTest {

    @Before
    public void init() {
        super.init();
        transformer = new StringToIntegerTransformer();
    }

    @Test
    public void shouldCallNextInPipelineCorrectly() {
        transformer.loadOptions(options);

        TriggerOutputProcessor triggerOutputProcessor = Mockito.mock(TriggerOutputProcessor.class);
        transformer.setNext(triggerOutputProcessor);
        insertInMockedTriggerOutput("12");

        transformer.process(triggerOutput);
        Mockito.verify(triggerOutputProcessor).process(Mockito.any());
    }

    @Test
    public void shouldTransformCorrectlyPositive() {
        String all = "42";
        insertInMockedTriggerOutput(all);
        TriggerOutput transformedTriggerOutput = transformer.transform(triggerOutput);
        Assert.assertEquals(42, transformedTriggerOutput.getValue());
    }

    @Test
    public void shouldTransformCorrectlyNegative() {
        String all = "-42";
        insertInMockedTriggerOutput(all);
        TriggerOutput transformedTriggerOutput = transformer.transform(triggerOutput);
        Assert.assertEquals(-42, transformedTriggerOutput.getValue());
    }

    @Test(expected = TransformationException.class)
    public void shouldErrorWhileParsing() {
        String all = "42a";
        insertInMockedTriggerOutput(all);
        transformer.transform(triggerOutput);
    }

    @Test
    public void shouldAcceptUnknownOption() {
        insertInMockedOptions("foo", "bar");
        transformer.loadOptions(options);
    }
}
