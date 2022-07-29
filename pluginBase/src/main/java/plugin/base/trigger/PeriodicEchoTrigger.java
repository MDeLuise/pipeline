package plugin.base.trigger;

import md.dev.options.Options;
import md.dev.options.description.OptionDescription;
import md.dev.plugin.annotation.Trigger;

import md.dev.trigger.AbstractPeriodicTrigger;
import md.dev.trigger.output.TriggerOutput;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

@Trigger(
        id = "echo",
        outputType = String.class,
        description = "Send a given String in the pipeline."
)
public class PeriodicEchoTrigger extends AbstractPeriodicTrigger<String> {
    private String echo;

    public PeriodicEchoTrigger(TriggerOutput<String> triggerOutputToUse) {
        super(triggerOutputToUse);
    }

    @Override
    public void loadState() {

    }

    @Override
    public void saveState() {

    }


    @Override
    protected Collection<OptionDescription> acceptedClassOptions() {
        return new ArrayList<>(Arrays.asList(
                new OptionDescription(
                        "echo",
                        "value to send when triggered.",
                        String.class,
                        "default echo message",
                        false
                )));
    }

    protected void loadInstanceOptions(Options options) {
        if (options != null && options.has("echo")) {
            echo = (String) options.get("echo");
        }
    }

    @Override
    protected void initializeClassOptions() {
        echo = "default echo message";
    }

    @Override
    protected void listen() {
        triggerOutput.setValue(echo);
        triggerPipelines();
    }

}
