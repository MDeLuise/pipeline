package plugin.base.modifier.transformer;

import md.dev.modifier.transformer.AbstractTransformer;
import md.dev.options.Options;
import md.dev.options.description.OptionDescription;
import md.dev.plugin.annotation.Transformer;
import md.dev.trigger.output.TriggerOutput;
import md.dev.trigger.output.TriggerOutputImpl;

import java.util.ArrayList;
import java.util.Collection;

@Transformer(
        id = "objToStr",
        inputType = Object.class,
        outputType = String.class,
        description = "Transforms an object into a string calling toString() method."
)
public class ObjectToStringTransformer extends AbstractTransformer<Object, String> {
    @Override
    public TriggerOutput<String> transform(
            TriggerOutput<Object> triggerOutput) {
        TriggerOutput<String> stringTriggerOutput = new TriggerOutputImpl<>();
        stringTriggerOutput.setValue(triggerOutput.getValue().toString());
        return stringTriggerOutput;
    }

    @Override
    public void initializeClassOptions() {

    }

    @Override
    protected Collection<? extends OptionDescription> acceptedClassOptions() {
        return new ArrayList<>();
    }

    @Override
    protected void loadInstanceOptions(Options options) {

    }
}
