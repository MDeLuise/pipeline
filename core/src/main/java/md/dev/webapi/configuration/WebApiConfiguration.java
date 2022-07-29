package md.dev.webapi.configuration;

import java.util.HashMap;
import java.util.Map;

public class WebApiConfiguration {
    private final Map<String, Object> KEY_TO_VALUES = new HashMap<>();

    public boolean contains(Object key) {
        return KEY_TO_VALUES.containsKey(key);
    }

    public Object get(Object key) {
        return KEY_TO_VALUES.get(key);
    }

    public Object insert(String key, Object value) {
        return KEY_TO_VALUES.put(key, value);
    }
}
