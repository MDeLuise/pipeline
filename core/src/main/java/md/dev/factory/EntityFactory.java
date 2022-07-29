package md.dev.factory;

import md.dev.factory.exception.NamedElementNotFoundException;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class EntityFactory<T> {
    private final Map<String, Constructor> ID_TO_CONSTRUCTOR;
    private final Map<Constructor, List<Object>> CONSTRUCTOR_TO_PARAM;
    private final String PREFIX;


    public EntityFactory(String prefix) {
        this.PREFIX = prefix;
        this.ID_TO_CONSTRUCTOR = new HashMap<>();
        this.CONSTRUCTOR_TO_PARAM = new HashMap<>();
    }

    public void addConstructor(String id, Constructor constructor, List<Object> defaultParams) {
        ID_TO_CONSTRUCTOR.put(id, constructor);
        CONSTRUCTOR_TO_PARAM.put(constructor, defaultParams);
    }

    public T build(FactoryConfiguration factoryConfiguration) {
        final String NAME = (String) factoryConfiguration.get("name");
        if (!NAME.startsWith(PREFIX) ||
                !ID_TO_CONSTRUCTOR.containsKey(removePrefixIfExists(NAME))) {
            throw new NamedElementNotFoundException(NAME);
        }

        Constructor constructor = ID_TO_CONSTRUCTOR.get(removePrefixIfExists(NAME));
        List<Object> defaultParams = CONSTRUCTOR_TO_PARAM.get(constructor);
        return create(factoryConfiguration, constructor, defaultParams);
    }

    public T build(String name) {
        FactoryConfiguration factoryConfiguration = new FactoryConfiguration();
        factoryConfiguration.put("name", removePrefixIfExists(name));
        return build(factoryConfiguration);
    }

    @Override
    public String toString() {
        return String.format("EntityFactory{prefix='%s'}", PREFIX);
    }

    protected abstract T create(
            FactoryConfiguration factoryConfiguration,
            Constructor constructor,
            List<Object> defaultParams);


    private String removePrefixIfExists(String name) {
        return PREFIX.isBlank() ? name : name.split("\\.")[1];
    }
}
