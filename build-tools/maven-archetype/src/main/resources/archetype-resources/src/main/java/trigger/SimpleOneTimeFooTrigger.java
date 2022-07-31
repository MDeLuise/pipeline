package ${package}.trigger;

import md.dev.options.Options;
import md.dev.options.description.OptionDescription;
import md.dev.trigger.AbstractOneTimeTrigger;
import md.dev.trigger.output.TriggerOutput;
import md.dev.plugin.annotation.Trigger;


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
 * This is an example of Trigger.
 * "extends AbstractOneTimeTrigger<T>" means that this trigger:
 *      - it's one time (i.e. it's called only once),
 *      - send a value of type T in the pipeline.
 */
@Trigger(
    id = "triFoo0",
    outputType = Object.class,
    description = "..."
)
public class SimpleOneTimeFooTrigger extends AbstractOneTimeTrigger<Object> {
    private String echo;


    public SimpleOneTimeFooTrigger(TriggerOutput<Object> triggerOutputToUse) {
        super(triggerOutputToUse);
    }


    /**
     * Display which options are loaded by the action.
     *
     * @return collection of used options.
     */
    @Override
    protected Collection<? extends OptionDescription> acceptedClassOptions() {
        return new ArrayList<>(Arrays.asList(
            new OptionDescription(
                "echo", // name of the option
                "value to echo when triggered.", // description of the option
                String.class, // type of the option
                "default echo message", // default value of the option
                false // is option mandatory?
            )));
    }


    /**
     * Initialize options with default values.
     */
    @Override
    public void initializeClassOptions() {
        echo = "default echo message";
    }


    /**
     * Load used options.
     *
     * @param options: options to load
     */
    @Override
    protected void loadInstanceOptions(Options options) {
        if (options.has("echo")) {
            echo = (String) options.get("echo");
        }
    }


    /**
     * Perform the purpose of the trigger. Nota bene:
     * - the value sent to the pipeline must be inserted in the triggerOutput object
     * (don't create a new one),
     * - after that the triggerPipelines() must be called.
     */
    @Override
    protected void listen() {
        triggerOutput.setValue(echo);
        triggerPipelines();
        // call saveState() if you want to save the state of the trigger
    }


    /**
     * Used in order to save the state of the trigger.
     */
    @Override
    public void saveState() {
        setProperty("echo", echo);
    }


    /**
     * Used in order to load the previous state of the trigger.
     */
    @Override
    public void loadState() {
        String savedEcho = getProperty("echo");
        if (savedEcho != null) {
            echo = getProperty("echo");
        }
    }
}