package md.dev.processor.filter.operator;

import md.dev.processor.filter.Filter;
import md.dev.pipeline.TriggerOutputProcessor;
import md.dev.trigger.output.TriggerOutput;

import java.util.List;

public interface FilterOperator<T> extends TriggerOutputProcessor<T, T> {
    void insertOperator(FilterOperator<T> filterOperator);

    void insertFilter(Filter<T> filter);

    boolean filterAll(TriggerOutput<T> triggerOutput);

    List<FilterOperator<T>> getOperators();

    Filter<T> getFilter();
}
