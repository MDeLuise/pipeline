package md.dev.processor.filter;

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

public abstract class AbstractFilter<T> extends AbstractOptionsLoadableEntity implements Filter<T> {
    @Getter
    @Setter
    private TriggerOutputProcessor<T, ?> next;


    public AbstractFilter() {
        initializeOptions();
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
    public void process(TriggerOutput<T> triggerOutput) {
        if (options == null || options.size() == 0) {
            OptionsValidator.acceptNoOption(acceptedOptions());
        }
        if (filter(triggerOutput)) {
            processNext(triggerOutput);
        }
    }


    @Override
    public void processNext(TriggerOutput<T> triggerOutput) {
        if (next != null) {
            next.process(triggerOutput);
        }
    }


    @Override
    public Collection<OptionDescription> acceptedOptions() {
        ArrayList<OptionDescription> acceptedOptions = new ArrayList<>();
        acceptedOptions.addAll(acceptedClassOptions());
        return acceptedOptions;
    }


    @Override
    public Class getInputType() {
        return getType();
    }


    @Override
    public Class getOutputType() {
        return getType();
    }


    protected abstract void loadInstanceOptions(Options options);

    protected abstract Collection<? extends OptionDescription> acceptedClassOptions();


    private Class getType() {
        return this.getClass().getAnnotation(md.dev.plugin.annotation.Filter.class).inputType();
    }
}
