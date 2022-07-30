package md.dev.processor.filter.operator;

import md.dev.trigger.output.TriggerOutput;

public class NotFilterOperator<T> extends AbstractComposeFilterOperator<T> {

    @Override
    public void insertOperator(FilterOperator<T> filterOperator) {
        if (FILTERS.size() == 0) {
            FILTERS.add(filterOperator);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public boolean filterAll(TriggerOutput<T> triggerOutput) {
        return !FILTERS.get(0).filterAll(triggerOutput);
    }
}
