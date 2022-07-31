package plugin.base.processor.filter;

import md.dev.options.Options;
import md.dev.options.description.OptionDescription;
import md.dev.plugin.annotation.Filter;
import md.dev.trigger.output.TriggerOutput;

import java.util.Collection;
import java.util.Collections;

@Filter(
    id = "eq",
    inputType = Comparable.class,
    description = "Filter out triggerOutput with value not equals to the given one."
)
public class EqualsFilter<T extends Comparable<T>> extends AbstractCompareFilter<T> {
    @Override
    protected void loadImplementationOptions(Options options) {
    }


    @Override
    protected Collection<? extends OptionDescription> acceptedImplementationOptions() {
        return Collections.emptyList();
    }


    @Override
    public void initializeClassOptions() {

    }


    @Override
    public boolean filter(TriggerOutput<T> triggerOutput) {
        return this.compareValue.compareTo(triggerOutput.getValue()) == 0;
    }
}
