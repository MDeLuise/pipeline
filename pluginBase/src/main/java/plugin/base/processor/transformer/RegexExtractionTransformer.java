package plugin.base.processor.transformer;

import md.dev.processor.transformer.AbstractTransformer;
import md.dev.options.Options;
import md.dev.options.description.OptionDescription;
import md.dev.plugin.annotation.Transformer;
import md.dev.trigger.output.TriggerOutput;
import md.dev.trigger.output.TriggerOutputImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Transformer(
        id = "regexExt",
        inputType = String.class,
        outputType = String.class,
        description = "Extract matching regex from a string."
)
public class RegexExtractionTransformer extends AbstractTransformer<String, String> {
    private Pattern pattern;

    @Override
    public TriggerOutput<String> transform(TriggerOutput<String> triggerOutput) {
        TriggerOutput<String> formattedTriggerOutput = new TriggerOutputImpl<>();
        Matcher matcher = pattern.matcher(triggerOutput.getValue());
        String newValue = matcher.find() ? matcher.group() : "";
        formattedTriggerOutput.setValue(newValue);
        return formattedTriggerOutput;
    }

    @Override
    protected Collection<? extends OptionDescription> acceptedClassOptions() {
        return new ArrayList<>(Arrays.asList(
                new OptionDescription(
                        "pattern",
                        "Pattern to use to match regex.",
                        String.class,
                        ".*",
                        false
                )));
    }

    @Override
    protected void loadInstanceOptions(Options options) {
        if (options != null && options.has("pattern")) {
            pattern = Pattern.compile((String) options.get("pattern"));
        }
    }

    @Override
    public void initializeClassOptions() {
        pattern = Pattern.compile(".*");
    }
}
