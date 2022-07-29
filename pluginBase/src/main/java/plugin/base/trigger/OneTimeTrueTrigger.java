package plugin.base.trigger;

import md.dev.options.Options;
import md.dev.options.description.OptionDescription;
import md.dev.plugin.annotation.Trigger;

import md.dev.trigger.AbstractOneTimeTrigger;
import md.dev.trigger.output.TriggerOutput;

import java.util.ArrayList;
import java.util.Collection;

@Trigger(
        id = "oneTrue",
        outputType = Boolean.class,
        description = "Send one single True value in the pipeline."
)
public class OneTimeTrueTrigger extends AbstractOneTimeTrigger<Boolean> {

    public OneTimeTrueTrigger(TriggerOutput<Boolean> triggerOutputToUse) {
        super(triggerOutputToUse);
    }

    @Override
    protected void listen() {
        triggerOutput.setValue(Boolean.TRUE);
        triggerPipelines();
    }

    @Override
    protected Collection<? extends OptionDescription> acceptedClassOptions() {
        return new ArrayList<>();
    }

    @Override
    public void initializeClassOptions() {
    }


    @Override
    protected void loadInstanceOptions(Options options) {
    }

    @Override
    public void loadState() {

    }

    @Override
    public void saveState() {

    }
}
