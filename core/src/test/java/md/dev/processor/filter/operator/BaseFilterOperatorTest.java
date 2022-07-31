package md.dev.processor.filter.operator;

import md.dev.log.LoggerHandler;
import md.dev.processor.filter.Filter;
import md.dev.trigger.output.TriggerOutput;
import org.mockito.Mockito;

abstract class BaseFilterOperatorTest {
    Filter filter;
    TriggerOutput triggerOutput;
    FilterOperator filterOperator;


    public void init() {
        filter = Mockito.mock(Filter.class);
        triggerOutput = Mockito.mock(TriggerOutput.class);
        LoggerHandler.disableLog();
    }
}
