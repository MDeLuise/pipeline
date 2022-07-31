package md.dev.processor.filter.operator;

import md.dev.trigger.output.TriggerOutput;

public class AndFilterOperator<T> extends AbstractComposeFilterOperator<T> {

    @Override
    public boolean filterAll(TriggerOutput<T> triggerOutput) {
        return filters.stream().allMatch(filters -> filters.filterAll(triggerOutput));
    }
}
