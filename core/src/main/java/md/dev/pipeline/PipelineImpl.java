package md.dev.pipeline;

import md.dev.pipeline.exception.IncompatibleTypeException;
import md.dev.trigger.Trigger;
import md.dev.trigger.output.TriggerOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;


public class PipelineImpl implements Pipeline {

    private static class PipelineIterator implements Iterator<TriggerOutputProcessor<?, ?>> {

        private TriggerOutputProcessor<?, ?> nextElementToReturn;

        PipelineIterator(TriggerOutputProcessor<?, ?> firstInChain) {
            this.nextElementToReturn = firstInChain;
        }

        @Override
        public boolean hasNext() {
            return nextElementToReturn != null;
        }

        @Override
        public TriggerOutputProcessor<?, ?> next() {
            TriggerOutputProcessor<?, ?> toReturn = nextElementToReturn;
            nextElementToReturn = nextElementToReturn.getNext();
            return toReturn;
        }
    }


    private final List<Trigger<?>> TRIGGERS = new ArrayList<>();
    private TriggerOutputProcessor<?, ?> firstInChain;
    private TriggerOutputProcessor<?, ?> lastInChain;
    private final Logger LOGGER = LoggerFactory.getLogger(PipelineImpl.class);


    @Override
    public <T> void addTrigger(Trigger<T> trigger) {
        LOGGER.debug("add trigger {}", trigger);
        checkTriggerTypeCompatibility(trigger, firstInChain);
        TRIGGERS.add(trigger);
        trigger.linkPipeline(this);

        configureEntityIfNeeded(trigger);
    }


    @Override
    public void removeTrigger(Trigger<?> trigger) {
        LOGGER.debug("remove trigger {}", trigger);
        TRIGGERS.remove(trigger);
        trigger.unlinkPipeline(this);
    }

    @Override
    public <T> void addElement(TriggerOutputProcessor<T, ?> triggerOutputProcessor) {
        LOGGER.debug("add element {}", triggerOutputProcessor);
        if (firstInChain == null) {
            if (TRIGGERS.size() != 0) {
                checkTriggerTypeCompatibility(TRIGGERS.get(0), triggerOutputProcessor);
            }
            firstInChain = triggerOutputProcessor;
        } else {
            TriggerOutputProcessor<?, T> actualLastInChain =
                    lastInChain == null ?
                            (TriggerOutputProcessor<?, T>) firstInChain :
                            (TriggerOutputProcessor<?, T>) lastInChain;
            checkElementCompatibility(actualLastInChain, triggerOutputProcessor);
            actualLastInChain.setNext(triggerOutputProcessor);
            lastInChain = triggerOutputProcessor;
        }

        configureEntityIfNeeded(triggerOutputProcessor);
    }

    @SuppressWarnings({"checkstyle:ReturnCount", "checkstyle:NestedIfDepth"})
    @Override
    public <T> void removeElement(
            TriggerOutputProcessor<T, ?> elementToRemove) { // TODO add compatibility check
        if (firstInChain == null) {
            return;
        }

        if (firstInChain == elementToRemove) {
            TriggerOutputProcessor<?, ?> oldFirstInChain = firstInChain;
            firstInChain = firstInChain.getNext();
            oldFirstInChain.setNext(null);

        } else {
            TriggerOutputProcessor<?, ?> prevElement = firstInChain;
            Iterator<TriggerOutputProcessor<?, ?>> iterator =
                    new PipelineIterator(firstInChain.getNext());
            while (iterator.hasNext()) {
                TriggerOutputProcessor<T, ?> currentElement =
                        (TriggerOutputProcessor<T, ?>) iterator.next();
                if (currentElement == elementToRemove) {
                    TriggerOutputProcessor<T, ?> next =
                            (TriggerOutputProcessor<T, ?>) currentElement.getNext();
                    ((TriggerOutputProcessor<?, T>) prevElement).setNext(next);
                    if (currentElement.getNext() == null) {
                        lastInChain = prevElement;
                    }
                    currentElement.setNext(null);
                    break;
                }
                prevElement = currentElement;
            }
        }

        if (firstInChain == lastInChain) {
            lastInChain = null;
        }
    }

    @Override
    public <T> void process(TriggerOutput<T> triggerOutput) {
        LOGGER.debug("processing {}", triggerOutput);
        if (firstInChain != null) {
            LOGGER.debug("calling process on {}", firstInChain);
            ((TriggerOutputProcessor<T, ?>) firstInChain).process(triggerOutput);
        }
    }

    @Override
    public Iterator<TriggerOutputProcessor<?, ?>> iterator() {
        return new PipelineIterator(firstInChain);
    }

    @Override
    public void forEach(Consumer<? super TriggerOutputProcessor<?, ?>> action) {
        Iterator<TriggerOutputProcessor<?, ?>> iterator = iterator();
        iterator.forEachRemaining(action);
    }


    private void configureEntityIfNeeded(PipelineEntity entity) {
        if (RuntimeConfigurableEntity.class.isAssignableFrom(entity.getClass())) {
            RuntimeConfigurableEntity runtimeConfigurableEntity =
                    (RuntimeConfigurableEntity) entity;
            if (!runtimeConfigurableEntity.isConfigured()) {
                runtimeConfigurableEntity.configure();
            }
        }
    }

    @SuppressWarnings("checkstyle:ReturnCount")
    private <T> void checkTriggerTypeCompatibility(
            Trigger<T> trigger,
            TriggerOutputProcessor<?, ?> triggerOutputProcessor) {

        if (triggerOutputProcessor == null) {
            return;
        }
        Class triggerType = trigger.getOutputType();
        Class triggerOutputProcessorType = triggerOutputProcessor.getInputType();

        if (!triggerOutputProcessorType.isAssignableFrom(triggerType)) {
            throw new IncompatibleTypeException(
                    trigger.getClass(),
                    triggerType,
                    triggerOutputProcessor.getClass(),
                    triggerOutputProcessorType
            );
        }
    }

    @SuppressWarnings("checkstyle:ReturnCount")
    private void checkElementCompatibility(
            TriggerOutputProcessor<?, ?> triggerOutputProcessor1,
            TriggerOutputProcessor<?, ?> triggerOutputProcessor2) {

        Class triggerOutputProcessor1Type = triggerOutputProcessor1.getOutputType();
        Class triggerOutputProcessor2Type = triggerOutputProcessor2.getInputType();

        if (!triggerOutputProcessor2Type.isAssignableFrom(triggerOutputProcessor1Type)) {
            throw new IncompatibleTypeException(
                    triggerOutputProcessor1.getClass(),
                    triggerOutputProcessor1Type,
                    triggerOutputProcessor2.getClass(),
                    triggerOutputProcessor2Type
            );
        }
    }
}
