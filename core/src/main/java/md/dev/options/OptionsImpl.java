package md.dev.options;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class OptionsImpl implements Options {
    private final Map<String, Object> OPTIONS = new HashMap<>();

    @Override
    public void add(String key, Object value) {
        OPTIONS.put(key, value);
    }

    @Override
    public String getString(String key) {
        return (String) get(key);
    }

    @Override
    public int getInt(String key) {
        return (int) get(key);
    }

    @Override
    public Object get(String key) {
        return OPTIONS.get(key);
    }

    @Override
    public boolean has(String key) {
        return OPTIONS.containsKey(key);
    }

    @Override
    public Collection<String> getKeys() {
        return OPTIONS.keySet();
    }

    @Override
    public int size() {
        return OPTIONS.size();
    }

    @Override
    public Optional<Object> getIfExists(String key) {
        if (has(key)) {
            return Optional.of(get(key));
        } else {
            return Optional.empty();
        }
    }
}
