package md.dev.action;

import md.dev.options.description.OptionsLoadableEntity;
import md.dev.pipeline.TriggerOutputProcessor;
import md.dev.trigger.output.TriggerOutput;

public interface Action<T> extends TriggerOutputProcessor<T, T>, OptionsLoadableEntity {
    void doAction(TriggerOutput<T> triggerOutput);
}
