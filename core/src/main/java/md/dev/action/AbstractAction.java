package md.dev.action;

import lombok.Getter;
import lombok.Setter;
import md.dev.options.Options;
import md.dev.options.OptionsValidator;
import md.dev.options.description.AbstractOptionsLoadableEntity;
import md.dev.options.description.OptionDescription;
import md.dev.pipeline.TriggerOutputProcessor;
import md.dev.trigger.output.TriggerOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;

public abstract class AbstractAction<T> extends AbstractOptionsLoadableEntity implements Action<T> {
    protected final Logger log = LoggerFactory.getLogger(AbstractAction.class);
    @Getter
    @Setter
    private TriggerOutputProcessor<T, ?> next;


    public AbstractAction() {
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
    public Collection<OptionDescription> acceptedOptions() {
        ArrayList<OptionDescription> acceptedOptions = new ArrayList<>();
        acceptedOptions.addAll(acceptedClassOptions());
        return acceptedOptions;
    }


    protected abstract void loadInstanceOptions(Options options);

    protected abstract Collection<OptionDescription> acceptedClassOptions();


    @Override
    public void process(TriggerOutput<T> triggerOutput) {
        if (options == null || options.size() == 0) {
            OptionsValidator.acceptNoOption(acceptedOptions());
        }
        doAction(triggerOutput);
        processNext(triggerOutput);
    }


    @Override
    public void processNext(TriggerOutput<T> triggerOutput) {
        if (next != null) {
            next.process(triggerOutput);
        }
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
        return this.getClass().getAnnotation(md.dev.plugin.annotation.Action.class).inputType();
    }
}
