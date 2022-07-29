package md.dev.modifier.filter.operator;

import md.dev.modifier.filter.Filter;
import md.dev.pipeline.exception.IncompatibleTypeException;

import java.util.ArrayList;
import java.util.List;

abstract class AbstractComposeFilterOperator<T> extends AbstractFilterOperator<T> {
    protected final List<FilterOperator<T>> FILTERS = new ArrayList<>();


    @Override
    public void insertOperator(FilterOperator<T> filterOperator) {
        checkTypeCompatibility(filterOperator);
        FILTERS.add(filterOperator);
    }

    @Override
    public void insertFilter(Filter<T> filter) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<FilterOperator<T>> getOperators() {
        return FILTERS;
    }

    @Override
    public Filter<T> getFilter() {
        throw new UnsupportedOperationException();
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
        return FILTERS.get(0).getInputType();
    }

    private void checkTypeCompatibility(FilterOperator<T> filterOperator) {
        if (FILTERS.size() != 0) {
            FilterOperator<T> firstFilterOperator = FILTERS.get(0);
            Class<T> firstFilterOperatorType = firstFilterOperator.getInputType();
            if (!firstFilterOperatorType.isAssignableFrom(filterOperator.getInputType())) {
                throw new IncompatibleTypeException(
                        firstFilterOperator.getClass(),
                        firstFilterOperatorType,
                        filterOperator.getClass(),
                        filterOperator.getInputType()
                );
            }
        }
    }
}
