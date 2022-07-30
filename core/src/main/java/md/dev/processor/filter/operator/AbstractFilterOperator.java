package md.dev.processor.filter.operator;

import lombok.Getter;
import lombok.Setter;
import md.dev.pipeline.TriggerOutputProcessor;
import md.dev.trigger.output.TriggerOutput;

abstract class AbstractFilterOperator<T> implements FilterOperator<T> {
    @Getter
    @Setter
    protected TriggerOutputProcessor<T, ?> next;

    @Override
    public void process(TriggerOutput<T> triggerOutput) {
        if (filterAll(triggerOutput)) {
            processNext(triggerOutput);
        }
    }

    @Override
    public void processNext(TriggerOutput<T> triggerOutput) {
        next.process(triggerOutput);
    }

}
