package md.dev.factory;

import lombok.SneakyThrows;
import md.dev.trigger.Trigger;
import md.dev.trigger.output.TriggerOutput;
import md.dev.trigger.output.TriggerOutputImpl;
import md.dev.webapi.WebApi;

import java.lang.reflect.Constructor;
import java.util.List;

public class TriggerFactory extends EntityFactory<Trigger> {
    public TriggerFactory(String prefix) {
        super(prefix);
    }

    @SneakyThrows
    @Override
    protected Trigger create(
            FactoryConfiguration factoryConfiguration,
            Constructor constructor,
            List<Object> defaultParams) {

        final TriggerOutput TRIGGER_OUTPUT_TO_USE =
                factoryConfiguration.contains("triggerOutput") ?
                        (TriggerOutput) factoryConfiguration.get("triggerOutput") :
                        new TriggerOutputImpl();

        if (defaultParams.size() == 1) {
            return (Trigger) constructor.newInstance(TRIGGER_OUTPUT_TO_USE);
        }

        final WebApi WEB_API_TO_USE =
                factoryConfiguration.contains("webApi") ?
                        (WebApi) factoryConfiguration.get("webApi") :
                        (WebApi) defaultParams.get(1);

        return (Trigger) constructor.newInstance(TRIGGER_OUTPUT_TO_USE, WEB_API_TO_USE);
    }
}
