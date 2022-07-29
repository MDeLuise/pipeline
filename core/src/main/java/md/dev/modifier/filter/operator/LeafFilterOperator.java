package md.dev.modifier.filter.operator;

import md.dev.modifier.filter.Filter;
import md.dev.trigger.output.TriggerOutput;

import java.util.List;

public class LeafFilterOperator<T> extends AbstractFilterOperator<T> {
    private Filter<T> filter;

    @Override
    public void insertOperator(FilterOperator<T> filterOperator) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void insertFilter(Filter<T> filter) {
        this.filter = filter;
    }

    @Override
    public boolean filterAll(TriggerOutput<T> triggerOutput) {
        return filter.filter(triggerOutput);
    }

    @Override
    public List<FilterOperator<T>> getOperators() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Filter<T> getFilter() {
        return filter;
    }

    @Override
    public Class getInputType() {
        return getType();
    }

    @Override
    public Class getOutputType() {
        return getType();
    }


    private Class getType() {
        return filter.getClass().getAnnotation(md.dev.plugin.annotation.Filter.class).inputType();
    }

}
