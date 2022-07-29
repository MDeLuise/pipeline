package md.dev.pipeline;

import md.dev.trigger.Trigger;
import md.dev.trigger.output.TriggerOutput;

public interface Pipeline extends Iterable<TriggerOutputProcessor<?, ?>> {
    <T> void addTrigger(Trigger<T> trigger);

    void removeTrigger(Trigger<?> trigger);

    <T> void addElement(TriggerOutputProcessor<T, ?> triggerOutputProcessor);

    <T> void removeElement(TriggerOutputProcessor<T, ?> triggerOutputProcessor);

    <T> void process(TriggerOutput<T> triggerOutput);
}
