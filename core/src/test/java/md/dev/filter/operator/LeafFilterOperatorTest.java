package md.dev.filter.operator;


import md.dev.factory.FactoryConfiguration;
import md.dev.factory.FilterOperatorFactory;
import md.dev.pipeline.TriggerOutputProcessor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class LeafFilterOperatorTest extends BaseFilterOperatorTest {

    @Before
    public void init() {
        super.init();
        FilterOperatorFactory filterOperatorFactory = new FilterOperatorFactory();
        FactoryConfiguration factoryConfiguration = new FactoryConfiguration();
        factoryConfiguration.put("name", "leaf");
        filterOperator = filterOperatorFactory.build(factoryConfiguration);
        filterOperator.insertFilter(filter);
    }

    @Test
    public void shouldFilterTrue() {
        Mockito.when(filter.filter(triggerOutput)).thenAnswer(foo -> true);
        Mockito.when(filter.getInputType()).thenAnswer(foo -> Object.class);
        Assert.assertTrue(filterOperator.filterAll(triggerOutput));
    }

    @Test
    public void shouldFilterFalse() {
        Mockito.when(filter.filter(triggerOutput)).thenAnswer(foo -> false);
        Mockito.when(filter.getInputType()).thenAnswer(foo -> Object.class);
        Assert.assertFalse(filterOperator.filterAll(triggerOutput));
    }

    @Test
    public void shouldProcessNextInChain() {
        Mockito.when(filter.filter(triggerOutput)).thenAnswer(foo -> true);
        Mockito.when(filter.getInputType()).thenAnswer(foo -> Object.class);

        TriggerOutputProcessor nextElement = Mockito.mock(TriggerOutputProcessor.class);

        filterOperator.setNext(nextElement);
        filterOperator.process(triggerOutput);
        Mockito.verify(nextElement, Mockito.times(1)).process(triggerOutput);
    }

    @Test
    public void shouldNotProcessNextInChain() {
        Mockito.when(filter.filter(triggerOutput)).thenAnswer(foo -> false);
        Mockito.when(filter.getInputType()).thenAnswer(foo -> Object.class);

        TriggerOutputProcessor nextElement = Mockito.mock(TriggerOutputProcessor.class);

        filterOperator.setNext(nextElement);
        filterOperator.process(triggerOutput);
        Mockito.verify(nextElement, Mockito.times(0)).process(triggerOutput);
    }
}
