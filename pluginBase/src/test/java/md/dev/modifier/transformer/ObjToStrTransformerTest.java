package md.dev.modifier.transformer;

import md.dev.pipeline.TriggerOutputProcessor;
import md.dev.trigger.output.TriggerOutput;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import plugin.base.modifier.transformer.ObjectToStringTransformer;

public class ObjToStrTransformerTest extends BaseTransformerTest {

    @Before
    public void init() {
        super.init();
        transformer = new ObjectToStringTransformer();
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
    public void shouldTransformCorrectlyBoolean() {
        boolean value = true;
        insertInMockedTriggerOutput(value);
        TriggerOutput transformedTriggerOutput = transformer.transform(triggerOutput);
        Assert.assertEquals("true", transformedTriggerOutput.getValue());
    }

    @Test
    public void shouldTransformCorrectlyBooleanObj() {
        boolean value = Boolean.TRUE;
        insertInMockedTriggerOutput(value);
        TriggerOutput transformedTriggerOutput = transformer.transform(triggerOutput);
        Assert.assertEquals(Boolean.TRUE.toString(), transformedTriggerOutput.getValue());
    }

    @Test
    public void shouldTransformCorrectlyString() {
        String all = "42";
        insertInMockedTriggerOutput(all);
        TriggerOutput transformedTriggerOutput = transformer.transform(triggerOutput);
        Assert.assertEquals(all, transformedTriggerOutput.getValue());
    }

    @Test
    public void shouldTransformCorrectlyInteger() {
        int value = 42;
        insertInMockedTriggerOutput(value);
        TriggerOutput transformedTriggerOutput = transformer.transform(triggerOutput);
        Assert.assertEquals("42", transformedTriggerOutput.getValue());
    }

    @Test
    public void shouldAcceptUnknownOption() {
        insertInMockedOptions("foo", "bar");
        transformer.loadOptions(options);
    }
}
