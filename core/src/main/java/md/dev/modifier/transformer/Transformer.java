package md.dev.modifier.transformer;

import md.dev.options.description.OptionDescription;
import md.dev.options.description.OptionsLoadableEntity;
import md.dev.pipeline.TriggerOutputProcessor;
import md.dev.trigger.output.TriggerOutput;

import java.util.Collection;

public interface Transformer<T, E> extends TriggerOutputProcessor<T, E>, OptionsLoadableEntity {
    TriggerOutput<E> transform(TriggerOutput<T> triggerOutput);

    Collection<OptionDescription> acceptedOptions();
}
