package md.dev.pipeline;

import md.dev.trigger.Trigger;
import md.dev.trigger.output.TriggerOutput;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class PipelineTest {
    Pipeline pipeline;
    Trigger trigger1;
    Trigger trigger2;
    TriggerOutput triggerOutput;
    TriggerOutputProcessor triggerOutputProcessor1;
    TriggerOutputProcessor triggerOutputProcessor2;
    TriggerOutputProcessor triggerOutputProcessor3;


    @Before
    public void init() {
        pipeline = new PipelineImpl();
        trigger1 = Mockito.mock(Trigger.class);
        trigger2 = Mockito.mock(Trigger.class);
        triggerOutput = Mockito.mock(TriggerOutput.class);
        triggerOutputProcessor1 = Mockito.mock(TriggerOutputProcessor.class);
        triggerOutputProcessor2 = Mockito.mock(TriggerOutputProcessor.class);
        triggerOutputProcessor3 = Mockito.mock(TriggerOutputProcessor.class);
        Mockito.when(triggerOutputProcessor1.getInputType()).thenAnswer(foo -> Object.class);
        Mockito.when(triggerOutputProcessor2.getInputType()).thenAnswer(foo -> Object.class);
        Mockito.when(triggerOutputProcessor3.getInputType()).thenAnswer(foo -> Object.class);
        Mockito.when(triggerOutputProcessor1.getOutputType()).thenAnswer(foo -> Object.class);
        Mockito.when(triggerOutputProcessor2.getOutputType()).thenAnswer(foo -> Object.class);
        Mockito.when(triggerOutputProcessor3.getOutputType()).thenAnswer(foo -> Object.class);
    }


    @Test
    public void shouldProcessFirstElement() {
        pipeline.addElement(triggerOutputProcessor1);
        pipeline.process(null);
        Mockito.verify(
            triggerOutputProcessor1,
            Mockito.times(1)
        ).process(null);
    }


    @Test
    public void shouldLinkSecondElement() {
        pipeline.addElement(triggerOutputProcessor1);
        pipeline.addElement(triggerOutputProcessor2);
        Mockito.verify(
            triggerOutputProcessor1,
            Mockito.times(1)
        ).setNext(triggerOutputProcessor2);
    }


    @Test
    public void shouldRemoveFirstElement() {
        Mockito.when(triggerOutputProcessor1.getNext()).thenAnswer(foo -> triggerOutputProcessor2);

        pipeline.addElement(triggerOutputProcessor1);
        pipeline.addElement(triggerOutputProcessor2);
        pipeline.removeElement(triggerOutputProcessor1);

        Mockito.verify(
            triggerOutputProcessor1,
            Mockito.times(1)
        ).setNext(null);

        pipeline.process(triggerOutput);
        Mockito.verify(triggerOutputProcessor2, Mockito.times(1))
            .process(triggerOutput);
    }


    @Test
    public void shouldRemoveSecondElement() {
        Mockito.when(triggerOutputProcessor1.getNext()).thenAnswer(foo -> triggerOutputProcessor2);

        pipeline.addElement(triggerOutputProcessor1);
        pipeline.addElement(triggerOutputProcessor2);
        pipeline.removeElement(triggerOutputProcessor2);

        Mockito.verify(
            triggerOutputProcessor2,
            Mockito.times(1)
        ).setNext(null);

        pipeline.process(triggerOutput);
        Mockito.verify(triggerOutputProcessor1, Mockito.times(1))
            .process(triggerOutput);
    }


    @Test
    public void shouldReturnCorrectIterator() {
        pipeline.addElement(triggerOutputProcessor1);
        pipeline.addElement(triggerOutputProcessor2);
        pipeline.addElement(triggerOutputProcessor3);

        Mockito.when(triggerOutputProcessor1.getNext()).thenAnswer(foo -> triggerOutputProcessor2);
        Mockito.when(triggerOutputProcessor2.getNext()).thenAnswer(foo -> triggerOutputProcessor3);
        Mockito.when(triggerOutputProcessor3.getNext()).thenAnswer(foo -> null);

        Iterator<TriggerOutputProcessor<?, ?>> iterator = pipeline.iterator();
        List<TriggerOutputProcessor> wantedElements = new ArrayList<>(Arrays.asList(
            triggerOutputProcessor1, triggerOutputProcessor2, triggerOutputProcessor3
        ));
        List<TriggerOutputProcessor<?, ?>> currentElements = new ArrayList<>();
        while (iterator.hasNext()) {
            currentElements.add(iterator.next());
        }

        Assert.assertEquals(wantedElements, currentElements);
    }


}
