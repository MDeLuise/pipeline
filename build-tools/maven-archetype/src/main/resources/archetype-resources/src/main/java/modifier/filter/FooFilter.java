package ${package}.processor.filter;

import md.dev.processor.filter.AbstractFilter;
import md.dev.options.Options;
import md.dev.options.description.OptionDescription;
import md.dev.plugin.annotation.Filter;
import md.dev.trigger.output.TriggerOutput;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
 * This is an example of a filter
 * "extends AbstractFilter<T>" means that this filter:
 *      - accept a triggerOutput of type T,
 */
@Filter(
    id = "filFoo",
    inputType = Comparable.class,
    description = "..."
)
public class FooFilter extends AbstractFilter<Comparable> {
    private Comparable compareValue;


    /**
     * Load used options.
     *
     * @param options: options to load.
     */
    @Override
    protected void loadInstanceOptions(Options options) {
        if (options != null && options.has("compareValue")) {
            compareValue = (Comparable) options.get("compareValue");
        }
    }


    /**
     * List which options are loaded by the filter.
     *
     * @return collection of used options.
     */
    @Override
    public List<OptionDescription> acceptedClassOptions() {
        List<OptionDescription> acceptedOptions = new ArrayList<>(Arrays.asList(
            new OptionDescription(
                "compareValue", // name of the option
                "Value to compare the filter with.", // description of the option
                Comparable.class, // type of the option
                "null", // default value of the option
                true // is option mandatory?
            )));
        return acceptedOptions;
    }


    /**
     * Initialize options with default values.
     */
    @Override
    public void initializeClassOptions() {

    }


    /**
     * Perform the purpose of the filter.
     */
    @Override
    public boolean filter(TriggerOutput<Comparable> triggerOutput) {
        return this.compareValue.compareTo(triggerOutput.getValue()) == 0;
    }
}
