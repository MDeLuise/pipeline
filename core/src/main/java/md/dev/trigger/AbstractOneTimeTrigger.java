package md.dev.trigger;


import md.dev.options.Options;
import md.dev.options.OptionsValidator;
import md.dev.options.description.OptionDescription;
import md.dev.trigger.output.TriggerOutput;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

public abstract class AbstractOneTimeTrigger<T> extends AbstractTrigger<T> {

    public AbstractOneTimeTrigger(TriggerOutput<T> triggerOutputToUse) {
        super(triggerOutputToUse);
    }


    @Override
    protected void initializePeriodicityOptions() {
        initializeClassOptions();
    }


    protected abstract void initializeClassOptions();


    @Override
    public void startListening() {
        if (options == null || options.size() == 0) {
            OptionsValidator.acceptNoOption(acceptedOptions());
        }
        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            listen();
        }).start();
    }


    @Override
    protected Collection<? extends OptionDescription> acceptedPeriodicityOptions() {
        return acceptedClassOptions();
    }


    @Override
    protected void loadPeriodicityOptions(Options options) {
        loadInstanceOptions(options);
    }


    protected abstract void loadInstanceOptions(Options options);

    protected abstract void listen();

    protected abstract Collection<? extends OptionDescription> acceptedClassOptions();
}
