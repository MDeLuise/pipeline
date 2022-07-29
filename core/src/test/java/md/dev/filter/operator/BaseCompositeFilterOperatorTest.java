package md.dev.filter.operator;

import md.dev.log.LoggerHandler;
import md.dev.modifier.filter.operator.FilterOperator;
import md.dev.trigger.output.TriggerOutput;
import org.mockito.Mockito;

abstract class BaseCompositeFilterOperatorTest {
    FilterOperator filterOperator1;
    FilterOperator filterOperator2;
    FilterOperator filterOperator3;
    TriggerOutput triggerOutput;
    FilterOperator filterOperator;


    public void init() {
        filterOperator1 = Mockito.mock(FilterOperator.class);
        filterOperator2 = Mockito.mock(FilterOperator.class);
        filterOperator3 = Mockito.mock(FilterOperator.class);
        triggerOutput = Mockito.mock(TriggerOutput.class);
        LoggerHandler.disableLog();
    }
}
