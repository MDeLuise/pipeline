package plugin.base.processor.transformer;

import md.dev.processor.transformer.AbstractTransformer;
import md.dev.processor.transformer.exception.TransformationException;
import md.dev.options.Options;
import md.dev.options.description.OptionDescription;
import md.dev.plugin.annotation.Transformer;
import md.dev.trigger.output.TriggerOutput;
import md.dev.trigger.output.TriggerOutputImpl;

import java.util.Collection;
import java.util.Collections;

@Transformer(
        id = "strToInt",
        inputType = String.class,
        outputType = Integer.class,
        description = "Transform a string into Integer (useful used previous to a compare filter)."
)
public class StringToIntegerTransformer extends AbstractTransformer<String, Integer> {


    @Override
    protected Collection<? extends OptionDescription> acceptedClassOptions() {
        return Collections.emptyList();
    }

    @Override
    protected void loadInstanceOptions(Options options) {
    }

    @Override
    public TriggerOutput<Integer> transform(TriggerOutput<String> triggerOutput) {
        String oldValue = triggerOutput.getValue();
        Integer newValue;

        try {
            newValue = Integer.parseInt(oldValue);
        } catch (NumberFormatException e) {
            throw new TransformationException(
                    String.format("Cannot convert %s to number", oldValue)
            );
        }
        TriggerOutputImpl<Integer> toReturn = new TriggerOutputImpl<>();
        toReturn.setValue(newValue);
        return toReturn;
    }

    @Override
    public void initializeClassOptions() {

    }
}
