package md.dev.trigger.output;

import org.junit.Assert;
import org.junit.Test;

public class TriggerOutputTest {
    @Test
    public void shouldAddStringValueCorrectly() {
        TriggerOutput<String> triggerOutput = new TriggerOutputImpl<>();
        String value = "bar";
        triggerOutput.setValue(value);
        Assert.assertEquals(triggerOutput.getValue(), value);
    }


    @Test
    public void shouldOverrideStringValueCorrectly() {
        TriggerOutput<String> triggerOutput = new TriggerOutputImpl<>();
        String value = "bar";
        String newValue = "new";
        triggerOutput.setValue(value);
        triggerOutput.setValue(newValue);
        Assert.assertEquals(triggerOutput.getValue(), newValue);
    }


    @Test
    public void shouldAddIntegerValueCorrectly() {
        TriggerOutput<Integer> triggerOutput = new TriggerOutputImpl<>();
        int value = 42;
        triggerOutput.setValue(value);
        Assert.assertEquals(triggerOutput.getValue().intValue(), value);
    }


    @Test
    public void shouldOverrideIntegerValueCorrectly() {
        TriggerOutput<Integer> triggerOutput = new TriggerOutputImpl<>();
        int value = 10;
        int newValue = 42;
        triggerOutput.setValue(value);
        triggerOutput.setValue(newValue);
        Assert.assertEquals(triggerOutput.getValue().intValue(), newValue);
    }
}
