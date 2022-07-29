package plugin.base.trigger;

import md.dev.options.Options;
import md.dev.options.description.OptionDescription;
import md.dev.plugin.annotation.Trigger;

import md.dev.trigger.AbstractPeriodicTrigger;
import md.dev.trigger.output.TriggerOutput;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Random;

@Trigger(
        id = "rand",
        outputType = Integer.class,
        description = "Send random number in the pipeline."
)
public class PeriodicRandomIntTrigger extends AbstractPeriodicTrigger<Integer> {
    private int minimum;
    private int maximum;


    public PeriodicRandomIntTrigger(TriggerOutput<Integer> triggerOutputToUse) {
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
                        "min",
                        "minimum random value.",
                        java.lang.Integer.class,
                        "0",
                        false
                ),
                new OptionDescription(
                        "max",
                        "maximum random value.",
                        java.lang.Integer.class,
                        "100",
                        false
                )
        ));
    }

    protected void loadInstanceOptions(Options options) {
        if (options != null && options.has("min")) {
            minimum = (int) options.get("min");
        }

        if (options != null && options.has("max")) {
            maximum = (int) options.get("max");
        }
    }

    @SuppressWarnings("checkstyle:MagicNumber")
    @Override
    protected void initializeClassOptions() {
        minimum = 0;
        maximum = 100;
    }

    @Override
    protected void listen() {
        Random random = new Random();
        triggerOutput.setValue(random.nextInt(minimum, maximum));
        triggerPipelines();
    }
}
