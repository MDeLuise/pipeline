package plugin.base.trigger;

import md.dev.options.Options;
import md.dev.options.description.OptionDescription;
import md.dev.plugin.annotation.Trigger;

import md.dev.trigger.AbstractPeriodicTrigger;
import md.dev.trigger.output.TriggerOutput;

import java.util.ArrayList;
import java.util.Collection;

@Trigger(
        id = "perTrue",
        outputType = Boolean.class,
        description = "Send True values in the pipeline."
)
public class PeriodicTrueTrigger extends AbstractPeriodicTrigger<Boolean> {

    public PeriodicTrueTrigger(TriggerOutput<Boolean> triggerOutputToUse) {
        super(triggerOutputToUse);
    }

    @Override
    public void loadState() {

    }

    @Override
    public void saveState() {

    }

    @Override
    protected void listen() {
        log.debug("called listen");
        triggerOutput.setValue(Boolean.TRUE);
        triggerPipelines();
        log.debug("pipeline triggered");
    }

    @Override
    protected Collection<OptionDescription> acceptedClassOptions() {
        return new ArrayList<>();
    }

    protected void loadInstanceOptions(Options options) {
    }

    @Override
    protected void initializeClassOptions() {
    }

}
