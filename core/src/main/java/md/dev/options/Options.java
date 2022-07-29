package md.dev.options;

import java.util.Collection;
import java.util.Optional;

public interface Options {
    void add(String key, Object value);

    Object get(String key);

    String getString(String key);

    int getInt(String key);

    boolean has(String key);

    Collection<String> getKeys();

    int size();

    Optional<Object> getIfExists(String key);
}
