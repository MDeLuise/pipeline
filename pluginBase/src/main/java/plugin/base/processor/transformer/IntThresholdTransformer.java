package plugin.base.processor.transformer;

import md.dev.processor.transformer.AbstractTransformer;
import md.dev.options.Options;
import md.dev.options.description.OptionDescription;
import md.dev.plugin.annotation.Transformer;
import md.dev.trigger.output.TriggerOutput;
import md.dev.trigger.output.TriggerOutputImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

@Transformer(
        id = "intThr",
        inputType = Integer.class,
        outputType = String.class,
        description = "Transform an Integer into a String, depending on the value of the integer."
)
public class IntThresholdTransformer extends AbstractTransformer<Integer, String> {
    private int compareValue;
    private String lessThanStr;
    private String greaterThanStr;
    private String equalThanStr;

    @Override
    protected Collection<? extends OptionDescription> acceptedClassOptions() {
        return new ArrayList<>(Arrays.asList(
                new OptionDescription(
                        "compareValue",
                        "Value to compare with.",
                        java.lang.Integer.class,
                        "0",
                        false
                ),
                new OptionDescription(
                        "lt",
                        "String to use if value is less than compareValue.",
                        String.class,
                        "value %s less than threshold",
                        false
                ),
                new OptionDescription(
                        "gt",
                        "String to use if value is greater than compareValue.",
                        String.class,
                        "value %s greater than threshold",
                        false
                ),
                new OptionDescription(
                        "eq",
                        "String to use if value is equals than compareValue.",
                        String.class,
                        "value %s equals than threshold",
                        false
                )
        ));
    }

    @Override
    protected void loadInstanceOptions(Options options) {
        if (options != null && options.has("compareValue")) {
            compareValue = options.getInt("compareValue");
        }
        if (options != null && options.has("lt")) {
            lessThanStr = options.getString("lt");
        }
        if (options != null && options.has("gt")) {
            greaterThanStr = options.getString("gt");
        }
        if (options != null && options.has("eq")) {
            equalThanStr = options.getString("eq");
        }
    }

    @Override
    public TriggerOutput<String> transform(TriggerOutput<Integer> triggerOutput) {
        TriggerOutput<String> transformedTriggerOutput = new TriggerOutputImpl<>();
        int intTriggerOutputValue = triggerOutput.getValue();
        switch (Integer.compare(compareValue, intTriggerOutputValue)) {
            case 0 -> transformedTriggerOutput.setValue(String.format(
                    equalThanStr, intTriggerOutputValue)
            );
            case 1 -> transformedTriggerOutput.setValue(String.format(
                    lessThanStr, intTriggerOutputValue)
            );
            default -> transformedTriggerOutput.setValue(String.format(
                    greaterThanStr, intTriggerOutputValue)
            );
        }
        return transformedTriggerOutput;
    }

    @Override
    public void initializeClassOptions() {
        compareValue = 0;
        lessThanStr = "value %s less than threshold";
        greaterThanStr = "value %s greater than threshold";
        equalThanStr = "value %s equals than threshold";
    }
}
