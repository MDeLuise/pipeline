package md.dev.processor.filter.operator;


import md.dev.factory.FactoryConfiguration;
import md.dev.factory.FilterOperatorFactory;
import md.dev.pipeline.TriggerOutputProcessor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class NotFilterOperatorTest extends BaseCompositeFilterOperatorTest {

    @Before
    public void init() {
        super.init();
        FilterOperatorFactory filterOperatorFactory = new FilterOperatorFactory();
        FactoryConfiguration factoryConfiguration = new FactoryConfiguration();
        factoryConfiguration.put("name", "not");
        filterOperator = filterOperatorFactory.build(factoryConfiguration);
    }

    @Test
    public void shouldFilterTrue() {
        Mockito.when(filterOperator1.filterAll(triggerOutput)).thenAnswer(foo -> false);
        Mockito.when(filterOperator1.getInputType()).thenAnswer(foo -> Object.class);
        filterOperator.insertOperator(filterOperator1);
        Assert.assertTrue(filterOperator.filterAll(triggerOutput));
    }

    @Test
    public void shouldFilterFalse() {
        Mockito.when(filterOperator1.filterAll(triggerOutput)).thenAnswer(foo -> true);
        Mockito.when(filterOperator1.getInputType()).thenAnswer(foo -> Object.class);
        filterOperator.insertOperator(filterOperator1);
        Assert.assertFalse(filterOperator.filterAll(triggerOutput));
    }

    @Test
    public void shouldProcessNextInChain() {
        Mockito.when(filterOperator1.filterAll(triggerOutput)).thenAnswer(foo -> false);
        Mockito.when(filterOperator1.getInputType()).thenAnswer(foo -> Object.class);
        filterOperator.insertOperator(filterOperator1);

        TriggerOutputProcessor nextElement = Mockito.mock(TriggerOutputProcessor.class);

        filterOperator.setNext(nextElement);
        filterOperator.process(triggerOutput);
        Mockito.verify(nextElement, Mockito.times(1)).process(triggerOutput);
    }

    @Test
    public void shouldNotProcessNextInChain() {
        Mockito.when(filterOperator1.filterAll(triggerOutput)).thenAnswer(foo -> true);
        Mockito.when(filterOperator1.getInputType()).thenAnswer(foo -> Object.class);
        filterOperator.insertOperator(filterOperator1);

        TriggerOutputProcessor nextElement = Mockito.mock(TriggerOutputProcessor.class);

        filterOperator.setNext(nextElement);
        filterOperator.process(triggerOutput);
        Mockito.verify(nextElement, Mockito.times(0)).process(triggerOutput);
    }
}
