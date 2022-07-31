package md.dev.factory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class FactoryConfiguration {
    private final Map<String, Object> map = new HashMap<>();


    public boolean contains(Object key) {
        return map.containsKey(key);
    }


    public Object get(Object key) {
        return map.get(key);
    }


    public Object put(String key, Object value) {
        return map.put(key, value);
    }


    public Set<String> keySet() {
        return map.keySet();
    }
}
