package md.dev.modifier.transformer;

import md.dev.options.exception.OptionMismatchTypeException;
import md.dev.pipeline.TriggerOutputProcessor;
import md.dev.trigger.output.TriggerOutput;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import plugin.base.modifier.transformer.IntThresholdTransformer;

public class IntThresholdTransformerTest extends BaseTransformerTest {
    final String defaultLessThanStr = "value %s less than threshold";
    final String defaultGreaterThanStr = "value %s greater than threshold";
    final String defaultEqualThanStr = "value %s equals than threshold";

    @Before
    public void init() {
        super.init();
        transformer = new IntThresholdTransformer();
    }

    @Test
    public void shouldCallNextInPipelineCorrectly() {
        transformer.loadOptions(options);
        insertInMockedTriggerOutput(42);

        TriggerOutputProcessor triggerOutputProcessor = Mockito.mock(TriggerOutputProcessor.class);
        transformer.setNext(triggerOutputProcessor);

        transformer.process(triggerOutput);
        Mockito.verify(triggerOutputProcessor).process(Mockito.any());
    }

    @Test
    public void shouldTransformValueCorrectlyLess() {
        int triggerOutputValue = 41;
        insertInMockedOptionsInt("compareValue", triggerOutputValue + 1);
        transformer.loadOptions(options);
        insertInMockedTriggerOutput(triggerOutputValue);

        TriggerOutput<String> transformedTriggerOutput = transformer.transform(triggerOutput);
        Assert.assertEquals(String.format(defaultLessThanStr, triggerOutputValue),
                transformedTriggerOutput.getValue());
    }

    @Test
    public void shouldTransformValueCorrectlyEquals() {
        int triggerOutputValue = 42;
        insertInMockedOptionsInt("compareValue", triggerOutputValue);
        transformer.loadOptions(options);
        insertInMockedTriggerOutput(triggerOutputValue);

        TriggerOutput<String> transformedTriggerOutput = transformer.transform(triggerOutput);
        Assert.assertEquals(String.format(defaultEqualThanStr, triggerOutputValue),
                transformedTriggerOutput.getValue());
    }

    @Test
    public void shouldTransformValueCorrectlyGreater() {
        int triggerOutputValue = 43;
        insertInMockedOptionsInt("compareValue", triggerOutputValue - 1);
        transformer.loadOptions(options);
        insertInMockedTriggerOutput(triggerOutputValue);

        TriggerOutput<String> transformedTriggerOutput = transformer.transform(triggerOutput);
        Assert.assertEquals(String.format(defaultGreaterThanStr, triggerOutputValue),
                transformedTriggerOutput.getValue());
    }

    @Test
    public void shouldTransformValueCorrectlyLessWithOptions() {
        String value = "foo";
        int triggerOutputValue = 41;
        insertInMockedOptionsInt("compareValue", triggerOutputValue + 1);
        insertInMockedOptionsString("lt", value);
        transformer.loadOptions(options);
        insertInMockedTriggerOutput(triggerOutputValue);

        TriggerOutput<String> transformedTriggerOutput = transformer.transform(triggerOutput);
        Assert.assertEquals(value, transformedTriggerOutput.getValue());
    }

    @Test
    public void shouldTransformValueCorrectlyEqualsWithOptions() {
        String value = "foo";
        int triggerOutputValue = 42;
        insertInMockedOptionsInt("compareValue", triggerOutputValue);
        insertInMockedOptionsString("eq", value);
        transformer.loadOptions(options);
        insertInMockedTriggerOutput(triggerOutputValue);

        TriggerOutput<String> transformedTriggerOutput = transformer.transform(triggerOutput);
        Assert.assertEquals(value, transformedTriggerOutput.getValue());
    }

    @Test
    public void shouldTransformValueCorrectlyGreaterWithOptions() {
        String value = "foo";
        int triggerOutputValue = 43;
        insertInMockedOptionsInt("compareValue", triggerOutputValue - 1);
        insertInMockedOptionsString("gt", value);
        transformer.loadOptions(options);
        insertInMockedTriggerOutput(triggerOutputValue);

        TriggerOutput<String> transformedTriggerOutput = transformer.transform(triggerOutput);
        Assert.assertEquals(value, transformedTriggerOutput.getValue());
    }

    @Test
    public void shouldTransformValueCorrectlyLessWithFormat() {
        String value = "foo %s";
        int triggerOutputValue = 41;
        insertInMockedOptionsInt("compareValue", triggerOutputValue + 1);
        insertInMockedOptionsString("lt", value);
        transformer.loadOptions(options);
        insertInMockedTriggerOutput(triggerOutputValue);

        TriggerOutput<String> transformedTriggerOutput = transformer.transform(triggerOutput);
        Assert.assertEquals(
                String.format(value, triggerOutputValue),
                transformedTriggerOutput.getValue()
        );
    }

    @Test
    public void shouldTransformValueCorrectlyEqualsWithFormat() {
        String value = "foo %s";
        int triggerOutputValue = 42;
        insertInMockedOptionsInt("compareValue", triggerOutputValue);
        insertInMockedOptionsString("eq", value);
        transformer.loadOptions(options);
        insertInMockedTriggerOutput(triggerOutputValue);

        TriggerOutput<String> transformedTriggerOutput = transformer.transform(triggerOutput);
        Assert.assertEquals(
                String.format(value, triggerOutputValue),
                transformedTriggerOutput.getValue()
        );
    }

    @Test
    public void shouldTransformValueCorrectlyGreaterWithFormat() {
        String value = "foo %s";
        int triggerOutputValue = 43;
        insertInMockedOptionsInt("compareValue", triggerOutputValue - 1);
        insertInMockedOptionsString("gt", value);
        transformer.loadOptions(options);
        insertInMockedTriggerOutput(triggerOutputValue);

        TriggerOutput<String> transformedTriggerOutput = transformer.transform(triggerOutput);
        Assert.assertEquals(
                String.format(value, triggerOutputValue),
                transformedTriggerOutput.getValue()
        );
    }

    @Test
    public void shouldAcceptUnknownOption() {
        insertInMockedOptions("foo", "bar");
        transformer.loadOptions(options);
    }

    @Test(expected = OptionMismatchTypeException.class)
    public void shouldThrowExceptionIfWrongOptionType() {
        insertInMockedOptions("compareValue", true);
        transformer.loadOptions(options);
    }
}
