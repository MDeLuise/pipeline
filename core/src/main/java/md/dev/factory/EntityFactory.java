package md.dev.factory;

import md.dev.factory.exception.NamedElementNotFoundException;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class EntityFactory<T> {
    private final Map<String, Constructor> idToConstructor;
    private final Map<Constructor, List<Object>> constructorToParam;
    private final String prefix;


    public EntityFactory(String prefix) {
        this.prefix = prefix;
        this.idToConstructor = new HashMap<>();
        this.constructorToParam = new HashMap<>();
    }


    public void addConstructor(String id, Constructor constructor, List<Object> defaultParams) {
        idToConstructor.put(id, constructor);
        constructorToParam.put(constructor, defaultParams);
    }


    public T build(FactoryConfiguration factoryConfiguration) {
        String name = (String) factoryConfiguration.get("name");
        if (!name.startsWith(prefix) ||
            !idToConstructor.containsKey(removePrefixIfExists(name))) {
            throw new NamedElementNotFoundException(name);
        }

        Constructor constructor = idToConstructor.get(removePrefixIfExists(name));
        List<Object> defaultParams = constructorToParam.get(constructor);
        return create(factoryConfiguration, constructor, defaultParams);
    }


    public T build(String name) {
        FactoryConfiguration factoryConfiguration = new FactoryConfiguration();
        factoryConfiguration.put("name", removePrefixIfExists(name));
        return build(factoryConfiguration);
    }


    @Override
    public String toString() {
        return String.format("EntityFactory{prefix='%s'}", prefix);
    }


    protected abstract T create(FactoryConfiguration factoryConfiguration,
                                Constructor constructor,
                                List<Object> defaultParams);


    private String removePrefixIfExists(String name) {
        return prefix.isBlank() ? name : name.split("\\.")[1];
    }
}
