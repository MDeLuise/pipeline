package md.dev.modifier.transformer;

import lombok.Getter;
import lombok.Setter;
import md.dev.options.Options;
import md.dev.options.OptionsValidator;
import md.dev.options.description.AbstractOptionsLoadableEntity;
import md.dev.options.description.OptionDescription;
import md.dev.pipeline.TriggerOutputProcessor;
import md.dev.trigger.output.TriggerOutput;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class AbstractTransformer<T, E>
        extends AbstractOptionsLoadableEntity
        implements Transformer<T, E> {
    @Getter
    @Setter
    private TriggerOutputProcessor<E, ?> next;


    public AbstractTransformer() {
        initializeOptions();
    }


    @Override
    public void process(TriggerOutput<T> triggerOutput) {
        if (options == null || options.size() == 0) {
            OptionsValidator.acceptNoOption(acceptedOptions());
        }
        processNext(transform(triggerOutput));
    }

    @Override
    public void processNext(TriggerOutput<E> triggerOutput) {
        next.process(triggerOutput);
    }

    @Override
    public Collection<OptionDescription> acceptedOptions() {
        List<OptionDescription> acceptedOption = new ArrayList<>();
        acceptedOption.addAll(acceptedClassOptions());
        return acceptedOption;
    }

    @Override
    public void initializeOptions() {
        initializeClassOptions();
    }

    protected abstract void initializeClassOptions();


    @Override
    public void loadClassOptions(Options options) {
        OptionsValidator.validateOptions(acceptedOptions(), options);
        loadInstanceOptions(options);
    }

    @Override
    public Class getInputType() {
        return this.getClass().
                getAnnotation(md.dev.plugin.annotation.Transformer.class).
                inputType();
    }

    @Override
    public Class getOutputType() {
        return this.getClass().
                getAnnotation(md.dev.plugin.annotation.Transformer.class).
                outputType();
    }


    protected abstract Collection<? extends OptionDescription> acceptedClassOptions();

    protected abstract void loadInstanceOptions(Options options);
}
