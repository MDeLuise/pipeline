package plugin.telegram.processor.transformer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import md.dev.processor.transformer.AbstractTransformer;
import md.dev.options.Options;
import md.dev.options.description.OptionDescription;
import md.dev.pipeline.exception.PipelineGenericException;
import md.dev.plugin.annotation.Transformer;
import md.dev.trigger.output.TriggerOutput;
import md.dev.trigger.output.TriggerOutputImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import plugin.telegram.model.TelegramUpdate;

import java.util.ArrayList;
import java.util.Collection;

@Transformer(
    id = "upToStr",
    inputType = TelegramUpdate.class,
    outputType = String.class,
    description = "Convert a TelegramUpdate object to String."
)
public class TelegramUpdateToStringTransformer
    extends AbstractTransformer<TelegramUpdate, String> {
    private final Logger log = LoggerFactory.getLogger(TelegramUpdateToStringTransformer.class);


    @Override
    public TriggerOutput<String> transform(
        TriggerOutput<TelegramUpdate> triggerOutput) {
        TriggerOutput<String> transformedTriggerOutput = new TriggerOutputImpl<>();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        try {
            transformedTriggerOutput.setValue(
                objectMapper.writeValueAsString(triggerOutput.getValue())
            );
        } catch (JsonProcessingException e) {
            log.error("error while converting telegramUpdate to String", e);
            throw new PipelineGenericException(e.getMessage());
        }
        return transformedTriggerOutput;
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
