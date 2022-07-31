package md.dev.processor.filter.operator;

import md.dev.trigger.output.TriggerOutput;

public class NotFilterOperator<T> extends AbstractComposeFilterOperator<T> {

    @Override
    public void insertOperator(FilterOperator<T> filterOperator) {
        if (filters.size() == 0) {
            filters.add(filterOperator);
        } else {
            throw new UnsupportedOperationException();
        }
    }


    @Override
    public boolean filterAll(TriggerOutput<T> triggerOutput) {
        return !filters.get(0).filterAll(triggerOutput);
    }
}
