package plugin.base.modifier.transformer;

import md.dev.modifier.transformer.AbstractTransformer;
import md.dev.options.Options;
import md.dev.options.description.OptionDescription;
import md.dev.plugin.annotation.Transformer;
import md.dev.trigger.output.TriggerOutput;
import md.dev.trigger.output.TriggerOutputImpl;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

@Transformer(
        id = "jsonExt",
        inputType = String.class,
        outputType = String.class,
        description = "Extract a field from a json string."
)
public class JsonExtractorTransformer extends AbstractTransformer<String, String> {
    String fieldToExtract;

    @Override
    protected Collection<? extends OptionDescription> acceptedClassOptions() {
        return new ArrayList<>(Collections.singletonList(
                new OptionDescription(
                        "field",
                        "field to extract (separate by space for inner extraction).",
                        String.class,
                        "",
                        true
                )
        ));
    }

    @Override
    protected void loadInstanceOptions(Options options) {
        if (options != null && options.has("field")) {
            fieldToExtract = options.getString("field");
        }
    }

    @Override
    public TriggerOutput<String> transform(TriggerOutput<String> triggerOutput) {
        TriggerOutput<String> transformedTriggerOutput = new TriggerOutputImpl<>();
        JSONObject extracted = new JSONObject(triggerOutput.getValue());
        String[] fieldsToExtract = fieldToExtract.split(" ");
        for (int i = 0; i < fieldsToExtract.length - 1; i++) {
            extracted = (JSONObject) extracted.get(fieldsToExtract[i]);
        }
        transformedTriggerOutput.setValue(
                extracted.get(fieldsToExtract[fieldsToExtract.length - 1]).toString()
        );
        return transformedTriggerOutput;
    }

    @Override
    public void initializeClassOptions() {
        fieldToExtract = "";
    }
}
