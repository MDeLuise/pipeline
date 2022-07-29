package md.dev.trigger;

import md.dev.options.Options;
import md.dev.options.OptionsValidator;
import md.dev.options.description.OptionDescription;
import md.dev.trigger.output.TriggerOutput;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public abstract class AbstractPeriodicTrigger<T> extends AbstractTrigger<T> {
    private int period;
    private Optional<Integer> repeatTimes = Optional.empty();


    public AbstractPeriodicTrigger(TriggerOutput<T> triggerOutputToUse) {
        super(triggerOutputToUse);
    }

    @Override
    public Collection<? extends OptionDescription> acceptedPeriodicityOptions() {
        List<OptionDescription> optionDescriptions = new ArrayList<>(Arrays.asList(
                new OptionDescription(
                        "period",
                        "Perform action every given amount of seconds.",
                        java.lang.Integer.class,
                        "5",
                        false
                ),
                new OptionDescription(
                        "repeat",
                        "Perform action given amount of time.",
                        java.lang.Integer.class,
                        "null",
                        false
                )));
        optionDescriptions.addAll(acceptedClassOptions());
        return optionDescriptions;
    }

    @Override
    public void startListening() {
        /*ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
        exec.scheduleAtFixedRate(this::listen, delay, period, TimeUnit.SECONDS);*/
        if (options == null || options.size() == 0) {
            OptionsValidator.acceptNoOption(acceptedOptions());
        }
        new Thread(() -> {
            try {
                LOG.debug("sleep for {} seconds (delay)", delay);
                TimeUnit.SECONDS.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            boolean firstIteration = true;
            while (repeatTimes.orElse(1) > 0) {
                if (!firstIteration) {
                    try {
                        LOG.debug("sleep for {} seconds (period)", period);
                        TimeUnit.SECONDS.sleep(period);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                firstIteration = false;
                LOG.debug("call trigger's listen");
                listen();
                LOG.debug("call to trigger's listen terminated");
                if (repeatTimes.isPresent()) {
                    repeatTimes = repeatTimes.map(count -> count - 1);
                    LOG.info("to repeat more " + repeatTimes.get() + " times");
                }
            }
        }).start();
    }


    protected abstract Collection<OptionDescription> acceptedClassOptions();

    @Override
    protected void loadPeriodicityOptions(Options options) {
        if (options.has("period")) {
            period = (int) options.get("period");
            LOG.info("loaded period value from option");
        }

        if (options.has("repeat")) {
            repeatTimes = Optional.of((int) options.get("repeat"));
            LOG.info("loaded repeat value from option");
        }
        loadInstanceOptions(options);
    }

    protected abstract void loadInstanceOptions(Options options);

    @Override
    @SuppressWarnings("checkstyle:MagicNumber")
    public void initializePeriodicityOptions() {
        period = 5;
        repeatTimes = Optional.empty();
        initializeClassOptions();
    }

    protected abstract void initializeClassOptions();

    protected abstract void listen();
}
