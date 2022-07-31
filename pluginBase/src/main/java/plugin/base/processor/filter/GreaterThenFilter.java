package plugin.base.processor.filter;


import md.dev.options.Options;
import md.dev.options.description.OptionDescription;
import md.dev.plugin.annotation.Filter;
import md.dev.trigger.output.TriggerOutput;

import java.util.ArrayList;
import java.util.Collection;

@Filter(
    id = "gt",
    inputType = Comparable.class,
    description = "Filter out triggerOutput with value less then the given one."
)
public class GreaterThenFilter<T extends Comparable<T>> extends AbstractCompareFilter<T> {

    @Override
    protected void loadImplementationOptions(Options options) {

    }


    @Override
    protected Collection<? extends OptionDescription> acceptedImplementationOptions() {
        return new ArrayList<>();
    }


    @Override
    public void initializeClassOptions() {

    }


    @Override
    public boolean filter(TriggerOutput<T> triggerOutput) {
        return this.compareValue.compareTo(triggerOutput.getValue()) < 0;
    }
}
