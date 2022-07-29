package md.dev.pipeline;

import md.dev.trigger.output.TriggerOutput;

public interface TriggerOutputProcessor<T, E> extends PipelineEntity {
    void process(TriggerOutput<T> triggerOutput);

    void setNext(TriggerOutputProcessor<E, ?> nextTriggerOutputProcessor);

    TriggerOutputProcessor<E, ?> getNext();

    default void processNext(TriggerOutput<E> triggerOutput) {
        TriggerOutputProcessor<E, ?> next = getNext();
        if (next != null) {
            getNext().process(triggerOutput);
        }
    }
}
