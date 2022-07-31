package md.dev.factory;

import md.dev.factory.exception.NamedElementNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

public class EntityFactorySet<T> {
    private final Set<EntityFactory<T>> factories = new HashSet<>();
    private final Logger log = LoggerFactory.getLogger(EntityFactorySet.class);


    public void addFactory(EntityFactory<T> factory) {
        factories.add(factory);
    }


    public T build(FactoryConfiguration factoryConfiguration) {
        String name = (String) factoryConfiguration.get("name");
        log.info("searching " + name + " in " + factories);
        for (EntityFactory<T> factory : factories) {
            try {
                return factory.build(factoryConfiguration);
            } catch (NamedElementNotFoundException ignore) {
                log.info(name + " not in " + factory);
            }
        }
        throw new NamedElementNotFoundException(name);
    }


    public T build(String name) {
        FactoryConfiguration factoryConfiguration = new FactoryConfiguration();
        factoryConfiguration.put("name", name);
        return build(factoryConfiguration);
    }
}
