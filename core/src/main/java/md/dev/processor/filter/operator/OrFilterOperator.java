package md.dev.processor.filter.operator;

import md.dev.trigger.output.TriggerOutput;

public class OrFilterOperator<T> extends AbstractComposeFilterOperator<T> {
    @Override
    public boolean filterAll(TriggerOutput<T> triggerOutput) {
        return filters.stream().anyMatch(filters -> filters.filterAll(triggerOutput));
    }
}
