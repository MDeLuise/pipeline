package ${package}.trigger;

import md.dev.options.Options;
import md.dev.options.description.OptionDescription;
import md.dev.trigger.AbstractPeriodicTrigger;
import md.dev.trigger.output.TriggerOutput;
import md.dev.plugin.annotation.Trigger;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Random;


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
 * This is an example of Trigger.
 * "extends AbstractPeriodicTrigger<T>" means that this trigger:
 *      - it's periodic (i.e. it's called periodically according to the
 *        "period" and "repeat" options passed),
 *      - send a value of type T in the pipeline.
 */
@Trigger(
    id = "triFoo1",
    outputType = Integer.class,
    description = "..."
)
public class SimplePeriodicFooTrigger extends AbstractPeriodicTrigger<Integer> {
    private int minimum;
    private int maximum;


    public SimplePeriodicFooTrigger(TriggerOutput<Integer> triggerOutputToUse) {
        super(triggerOutputToUse);
    }


    /**
     * List which options are loaded by the trigger.
     *
     * @return collection of used options.
     */
    @Override
    protected Collection<OptionDescription> acceptedClassOptions() {
        return new ArrayList<>(Arrays.asList(
            new OptionDescription(
                "min", // name of the option
                "minimum random value.", // description of the option
                java.lang.Integer.class, // type of the option
                "0", // default value of the option
                false // is option mandatory?
            ),
            new OptionDescription(
                "max", // name of the option
                "maximum random value.", // description of the option
                java.lang.Integer.class, // type of the option
                "100", // default value of the option
                false // is option mandatory?
            )
        ));
    }


    /**
     * Load used options.
     *
     * @param options: options to load.
     */
    @Override
    protected void loadInstanceOptions(Options options) {
        if (options.has("min")) {
            minimum = (int) options.get("min");
        }

        if (options.has("max")) {
            maximum = (int) options.get("max");
        }
    }


    /**
     * Initialize options with default values.
     */
    @SuppressWarnings("checkstyle:MagicNumber")
    @Override
    protected void initializeClassOptions() {
        minimum = 0;
        maximum = 100;
    }


    /**
     * Perform the purpose of the trigger. Notice that:
     * - the value sent to the pipeline must be inserted in the triggerOutput object
     * (don't create a new one),
     * - after that the triggerPipelines() must be called.
     */
    @Override
    protected void listen() {
        Random random = new Random();
        triggerOutput.setValue(random.nextInt(minimum, maximum));
        triggerPipelines();
        // call saveState() if you want to save the state of the trigger
    }


    /**
     * Used in order to save the state of the trigger.
     */
    @Override
    public void saveState() {
        setProperty("min", "" + minimum);
        setProperty("max", "" + maximum);
    }


    /**
     * Used in order to load the previous state of the trigger.
     */
    @Override
    public void loadState() {
        String savedMin = getProperty("min");
        if (savedMin != null) {
            minimum = Integer.parseInt(getProperty("min"));
        }
        String savedMax = getProperty("max");
        if (savedMax != null) {
            maximum = Integer.parseInt(getProperty("max"));
        }
    }
}