package md.dev.trigger.output;

import lombok.Data;

@Data
public class TriggerOutputImpl<T> implements TriggerOutput<T> {
    private T value;
}
