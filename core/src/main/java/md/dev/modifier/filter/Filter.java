package md.dev.modifier.filter;

import md.dev.options.description.OptionDescription;
import md.dev.options.description.OptionsLoadableEntity;
import md.dev.pipeline.TriggerOutputProcessor;
import md.dev.trigger.output.TriggerOutput;

import java.util.Collection;

public interface Filter<T> extends OptionsLoadableEntity, TriggerOutputProcessor<T, T> {
    boolean filter(TriggerOutput<T> triggerOutput);

    Collection<OptionDescription> acceptedOptions();
}
