package md.dev.factory;

import md.dev.factory.exception.NamedElementNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

public class EntityFactorySet<T> {
    private final Set<EntityFactory<T>> FACTORIES = new HashSet<>();
    private final Logger LOGGER = LoggerFactory.getLogger(EntityFactorySet.class);

    public void addFactory(EntityFactory<T> factory) {
        FACTORIES.add(factory);
    }

    public T build(FactoryConfiguration factoryConfiguration) {
        final String NAME = (String) factoryConfiguration.get("name");
        LOGGER.info("searching " + NAME + " in " + FACTORIES);
        for (EntityFactory<T> factory : FACTORIES) {
            try {
                return factory.build(factoryConfiguration);
            } catch (NamedElementNotFoundException ignore) {
                LOGGER.info(NAME + " not in " + factory);
            }
        }
        throw new NamedElementNotFoundException(NAME);
    }

    public T build(String name) {
        FactoryConfiguration factoryConfiguration = new FactoryConfiguration();
        factoryConfiguration.put("name", name);
        return build(factoryConfiguration);
    }
}
