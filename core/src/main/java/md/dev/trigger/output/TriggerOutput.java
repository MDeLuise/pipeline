package md.dev.trigger.output;

public interface TriggerOutput<T> {
    void setValue(T value);

    T getValue();
}
