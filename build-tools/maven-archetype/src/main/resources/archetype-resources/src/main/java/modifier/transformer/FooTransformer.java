package ${package}.processor.transformer;

import md.dev.processor.transformer.AbstractTransformer;
import md.dev.options.Options;
import md.dev.options.description.OptionDescription;
import md.dev.plugin.annotation.Transformer;
import md.dev.trigger.output.TriggerOutput;
import md.dev.trigger.output.TriggerOutputImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

@SuppressWarnings({
      "checkstyle:NonEmptyAtclauseDescription",
      "checkstyle:JavadocStyle",
      "checkstyle:JavadocTagContinuationIndentation",
      "checkstyle:JavadocParagraph",
      "checkstyle:InvalidJavadocPosition",
      "checkstyle:AtclauseOrder",
      "checkstyle:SingleLineJavadoc"
})
/**
 * This is an example of Transformer.
 */
@Transformer(
        id = "traFoo",
        inputType = Integer.class,
        outputType = String.class,
        description = "..."
)
public class FooTransformer extends AbstractTransformer<Integer, String> {
    private int compareValue;
    private String lessThanStr;
    private String greaterThanStr;
    private String equalThanStr;

    /**
     * List which options are loaded by the trigger.
     * @return collection of used options.
     */
    @Override
    protected Collection<? extends OptionDescription> acceptedClassOptions() {
        return new ArrayList<>(Arrays.asList(
                new OptionDescription(
                        "compareValue", // name of the option
                        "Value to compare with.", // description of the option
                        java.lang.Integer.class, // type of the option
                        "0", // default value of the option
                        false // is option mandatory?
                ),
                new OptionDescription(
                        "lt", // name of the option
                        "String to use if value is less than compareValue.",
                        // description of the option
                        String.class, // type of the option
                        "value %s less than threshold", // default value of the option
                        false // is option mandatory?
                ),
                new OptionDescription(
                        "gt", // name of the option
                        "String to use if value is greater than compareValue.",
                        // description of the option
                        String.class, // type of the option
                        "value %s greater than threshold", // default value of the option
                        false // is option mandatory?
                ),
                new OptionDescription(
                        "eq", // name of the option
                        "String to use if value is equals than compareValue.",
                        // description of the option
                        String.class, // type of the option
                        "value %s equals than threshold", // default value of the option
                        false // is option mandatory?
                )
        ));
    }

    /**
     * Load used options.
     * @param options: options to load.
     */
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

    /**
     * Perform the purpose of the transformer.
     */
    @Override
    public TriggerOutput<String> transform(TriggerOutput<Integer> triggerOutput) {
        TriggerOutput<String> transformedTriggerOutput = new TriggerOutputImpl<>();
        int intTriggerOutputValue = triggerOutput.getValue();
        String valueToSet;
        int compare = Integer.compare(compareValue, intTriggerOutputValue);
        if (compare == 0) {
            valueToSet = String.format(equalThanStr, intTriggerOutputValue);
        } else if (compare == 1) {
            valueToSet = String.format(lessThanStr, intTriggerOutputValue);
        } else {
            valueToSet = String.format(greaterThanStr, intTriggerOutputValue);
        }
        transformedTriggerOutput.setValue(valueToSet);
        return transformedTriggerOutput;
    }

    /**
     * Initialize options with default values.
     */
    @Override
    public void initializeClassOptions() {
        compareValue = 0;
        lessThanStr = "value %s less than threshold";
        greaterThanStr = "value %s greater than threshold";
        equalThanStr = "value %s equals than threshold";
    }
}
