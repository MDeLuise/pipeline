package md.dev.processor.filter.operator;

import md.dev.factory.FactoryConfiguration;
import md.dev.factory.FilterOperatorFactory;
import md.dev.pipeline.TriggerOutputProcessor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class OrFilterOperatorTest extends BaseCompositeFilterOperatorTest {

    @Before
    public void init() {
        super.init();
        FilterOperatorFactory filterOperatorFactory = new FilterOperatorFactory();
        FactoryConfiguration factoryConfiguration = new FactoryConfiguration();
        factoryConfiguration.put("name", "or");
        filterOperator = filterOperatorFactory.build(factoryConfiguration);
    }


    @Test
    public void shouldFilterTrueTwoTrue() {
        Mockito.when(filterOperator1.filterAll(triggerOutput)).thenAnswer(foo -> true);
        Mockito.when(filterOperator2.filterAll(triggerOutput)).thenAnswer(foo -> true);
        Mockito.when(filterOperator1.getInputType()).thenAnswer(foo -> Object.class);
        Mockito.when(filterOperator2.getInputType()).thenAnswer(foo -> Object.class);
        filterOperator.insertOperator(filterOperator1);
        filterOperator.insertOperator(filterOperator2);
        Assert.assertTrue(filterOperator.filterAll(triggerOutput));
    }


    @Test
    public void shouldFilterTrueThreeTrue() {
        Mockito.when(filterOperator1.filterAll(triggerOutput)).thenAnswer(foo -> true);
        Mockito.when(filterOperator2.filterAll(triggerOutput)).thenAnswer(foo -> true);
        Mockito.when(filterOperator3.filterAll(triggerOutput)).thenAnswer(foo -> true);
        Mockito.when(filterOperator1.getInputType()).thenAnswer(foo -> Object.class);
        Mockito.when(filterOperator2.getInputType()).thenAnswer(foo -> Object.class);
        Mockito.when(filterOperator3.getInputType()).thenAnswer(foo -> Object.class);
        filterOperator.insertOperator(filterOperator1);
        filterOperator.insertOperator(filterOperator2);
        filterOperator.insertOperator(filterOperator3);
        Assert.assertTrue(filterOperator.filterAll(triggerOutput));
    }


    @Test
    public void shouldFilterTrueTwoOneFalse() {
        Mockito.when(filterOperator1.filterAll(triggerOutput)).thenAnswer(foo -> true);
        Mockito.when(filterOperator2.filterAll(triggerOutput)).thenAnswer(foo -> false);
        Mockito.when(filterOperator1.getInputType()).thenAnswer(foo -> Object.class);
        Mockito.when(filterOperator2.getInputType()).thenAnswer(foo -> Object.class);
        filterOperator.insertOperator(filterOperator1);
        filterOperator.insertOperator(filterOperator2);
        Assert.assertTrue(filterOperator.filterAll(triggerOutput));
    }


    @Test
    public void shouldFilterFalseThreeFalse() {
        Mockito.when(filterOperator1.filterAll(triggerOutput)).thenAnswer(foo -> false);
        Mockito.when(filterOperator2.filterAll(triggerOutput)).thenAnswer(foo -> false);
        Mockito.when(filterOperator3.filterAll(triggerOutput)).thenAnswer(foo -> false);
        Mockito.when(filterOperator1.getInputType()).thenAnswer(foo -> Object.class);
        Mockito.when(filterOperator2.getInputType()).thenAnswer(foo -> Object.class);
        Mockito.when(filterOperator3.getInputType()).thenAnswer(foo -> Object.class);
        filterOperator.insertOperator(filterOperator1);
        filterOperator.insertOperator(filterOperator2);
        filterOperator.insertOperator(filterOperator3);
        Assert.assertFalse(filterOperator.filterAll(triggerOutput));
    }


    @Test
    public void shouldProcessNextInChain() {
        Mockito.when(filterOperator1.filterAll(triggerOutput)).thenAnswer(foo -> true);
        Mockito.when(filterOperator2.filterAll(triggerOutput)).thenAnswer(foo -> false);
        Mockito.when(filterOperator1.getInputType()).thenAnswer(foo -> Object.class);
        Mockito.when(filterOperator2.getInputType()).thenAnswer(foo -> Object.class);
        filterOperator.insertOperator(filterOperator1);
        filterOperator.insertOperator(filterOperator2);

        TriggerOutputProcessor nextElement = Mockito.mock(TriggerOutputProcessor.class);

        filterOperator.setNext(nextElement);
        filterOperator.process(triggerOutput);
        Mockito.verify(nextElement, Mockito.times(1)).process(triggerOutput);
    }


    @Test
    public void shouldNotProcessNextInChain() {
        Mockito.when(filterOperator1.filterAll(triggerOutput)).thenAnswer(foo -> false);
        Mockito.when(filterOperator2.filterAll(triggerOutput)).thenAnswer(foo -> false);
        Mockito.when(filterOperator1.getInputType()).thenAnswer(foo -> Object.class);
        Mockito.when(filterOperator2.getInputType()).thenAnswer(foo -> Object.class);
        filterOperator.insertOperator(filterOperator1);
        filterOperator.insertOperator(filterOperator2);

        TriggerOutputProcessor nextElement = Mockito.mock(TriggerOutputProcessor.class);

        filterOperator.setNext(nextElement);
        filterOperator.process(triggerOutput);
        Mockito.verify(nextElement, Mockito.times(0)).process(triggerOutput);
    }
}
