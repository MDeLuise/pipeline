package md.dev.trigger;

import md.dev.options.Options;
import md.dev.options.OptionsValidator;
import md.dev.options.description.AbstractOptionsLoadableEntity;
import md.dev.options.description.OptionDescription;
import md.dev.pipeline.Pipeline;
import md.dev.state.StateHandler;
import md.dev.trigger.output.TriggerOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;


abstract class AbstractTrigger<T> extends AbstractOptionsLoadableEntity implements Trigger<T> {
    protected TriggerOutput<T> triggerOutput;
    protected int delay;
    protected final Logger log = LoggerFactory.getLogger(AbstractTrigger.class);
    private final List<Pipeline> pipelines = new ArrayList<>();
    private String stateId;


    AbstractTrigger(TriggerOutput<T> triggerOutputToUse) {
        initializeCommonOptions();
        initializeOptions();
        this.triggerOutput = triggerOutputToUse;
    }


    @Override
    public void loadClassOptions(Options options) {
        OptionsValidator.validateOptions(acceptedOptions(), options);

        if (options.has("delay")) {
            delay = (int) options.get("delay");
            log.info("loaded delay value from passed option");
        } else {
            delay = 0;
            log.info("use default delay value");
        }
        if (options.has("stateId")) {
            stateId = createEntityId(options.getString("stateId"));
        }
        loadPeriodicityOptions(options);

        if (isStateEnabled()) {
            loadState();
        }
    }

    @Override
    public void linkPipeline(Pipeline pipeline) {
        pipelines.add(pipeline);
    }

    @Override
    public void unlinkPipeline(Pipeline pipeline) {
        pipelines.remove(pipeline);
    }

    @SuppressWarnings("checkstyle:HiddenField")
    @Override
    public Collection<OptionDescription> acceptedOptions() {
        List<OptionDescription> optionDescriptionList = new ArrayList<>(Arrays.asList(
                new OptionDescription(
                        "delay",
                        "Delay the start of the trigger by the given amount of seconds.",
                        java.lang.Integer.class,
                        "0",
                        false
                ),
                new OptionDescription(
                        "stateId",
                        "ID of the state to use in the trigger.",
                        String.class,
                        "null",
                        false
                )
        ));
        optionDescriptionList.addAll(acceptedPeriodicityOptions());
        return optionDescriptionList;
    }

    @Override
    public void initializeOptions() {
        initializePeriodicityOptions();
    }

    protected abstract void initializePeriodicityOptions();

    @Override
    public Class getOutputType() {
        return this.getClass().getAnnotation(md.dev.plugin.annotation.Trigger.class).outputType();
    }

    @Override
    public Class getInputType() {
        throw new UnsupportedOperationException();
    }


    protected void triggerPipelines() {
        log.debug("called triggerPipelines");
        pipelines.forEach(pipeline -> pipeline.process(triggerOutput));
        log.debug("triggerPipelines terminated");
    }

    protected String getProperty(String key) {
        return StateHandler.getPropertyFromState(stateId, key);
    }

    protected void setProperty(String key, String value) {
        StateHandler.savePropertyInState(stateId, key, value);
    }

    protected void removeProperty(String key) {
        StateHandler.removePropertyFromState(stateId, key);
    }

    protected boolean isStateEnabled() {
        return stateId != null;
    }

    protected abstract Collection<? extends OptionDescription> acceptedPeriodicityOptions();

    @SuppressWarnings("checkstyle:HiddenField")
    protected abstract void loadPeriodicityOptions(Options options);


    private void initializeCommonOptions() {
        if (options != null && options.has("delay")) {
            delay = (int) options.get("delay");
        } else {
            delay = 0;
        }
    }

    private String createEntityId(String stateId) {
        final String[] CLASS_NAME_EXPLODED = this.getClass().toString().split("\\.");
        final String CLASS_NAME = CLASS_NAME_EXPLODED[CLASS_NAME_EXPLODED.length - 1];
        return String.format("%s-%s", CLASS_NAME.toLowerCase(Locale.ROOT), stateId);
    }

}
