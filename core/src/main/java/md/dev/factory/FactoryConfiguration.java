package md.dev.factory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class FactoryConfiguration {
    private final Map<String, Object> MAP = new HashMap<>();

    public boolean contains(Object key) {
        return MAP.containsKey(key);
    }

    public Object get(Object key) {
        return MAP.get(key);
    }

    public Object put(String key, Object value) {
        return MAP.put(key, value);
    }

    public Set<String> keySet() {
        return MAP.keySet();
    }
}
