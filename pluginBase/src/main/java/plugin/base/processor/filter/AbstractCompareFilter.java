package plugin.base.processor.filter;

import md.dev.processor.filter.AbstractFilter;
import md.dev.options.Options;
import md.dev.options.description.OptionDescription;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

abstract class AbstractCompareFilter<T> extends AbstractFilter<T> {
    protected Options options;
    protected T compareValue;


    @Override
    protected void loadInstanceOptions(Options options) {
        if (options != null && options.has("compareValue")) {
            compareValue = (T) options.get("compareValue");
        }
        loadImplementationOptions(options);
    }


    @Override
    public List<OptionDescription> acceptedClassOptions() {
        List<OptionDescription> acceptedOptions = new ArrayList<>(Arrays.asList(
            new OptionDescription(
                "compareValue",
                "Value to compare the filter with.",
                Comparable.class,
                "null",
                true
            )));
        acceptedOptions.addAll(acceptedImplementationOptions());
        return acceptedOptions;
    }


    protected abstract void loadImplementationOptions(Options options);

    protected abstract Collection<? extends OptionDescription> acceptedImplementationOptions();
}
