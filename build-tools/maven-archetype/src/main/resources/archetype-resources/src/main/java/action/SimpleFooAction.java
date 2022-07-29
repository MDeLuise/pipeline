package ${package}.action;

import md.dev.action.AbstractAction;
import md.dev.options.Options;
import md.dev.options.description.OptionDescription;
import md.dev.trigger.output.TriggerOutput;
import md.dev.plugin.annotation.Action;

import java.io.PrintStream;
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
 * This is an example of Action.
 * "extends AbstractAction<T>" means that:
 *      - this action accept a triggerOutput of type T,
 *      - this is NOT a web action (i.e. does NOT use WebApi object to perform web requests).
 */
@Action(
        id = "actFoo0",
        inputType = Object.class,
        description = "..."
)
public class SimpleFooAction extends AbstractAction<Object> {
    private String prepend;
    private PrintStream outputStream;

    /**
     * List which options are loaded by the action.
     * @return collection of used options.
     */
    @Override
    public Collection<OptionDescription> acceptedClassOptions() {
        return new ArrayList<>(Arrays.asList(
                new OptionDescription(
                        "prepend", // name of the option
                        "Text to prepend to all printed messages.", // description of the option
                        Object.class, // type of the option
                        "", // default value of the option
                        false // is option mandatory?
                )
        ));
    }

    /**
     * Initialize options with default values.
     */
    @Override
    public void initializeClassOptions() {
        this.prepend = "";
        this.outputStream = System.out;
    }

    /**
     * Load used options.
     * @param options: options to load
     */
    @Override
    public void loadInstanceOptions(Options options) {
        if (options != null && options.has("stream")) {
            outputStream = (PrintStream) options.get("stream");
        }

        if (options != null && options.has("prepend")) {
            prepend = (String) options.get("prepend");
        }
    }

    /**
     * Perform the action.
     * @param triggerOutput: data from the pipeline.
     */
    @Override
    public void doAction(TriggerOutput<Object> triggerOutput) {
        String message = "this is the default printed message";
        if (triggerOutput != null && triggerOutput.getValue() != null) {
            message = triggerOutput.getValue().toString();
        }
        outputStream.println(prepend + message);
    }


}