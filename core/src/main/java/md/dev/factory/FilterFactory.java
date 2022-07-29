package md.dev.factory;

import lombok.SneakyThrows;
import md.dev.modifier.filter.Filter;

import java.lang.reflect.Constructor;
import java.util.List;

public class FilterFactory extends EntityFactory<Filter> {
    public FilterFactory(String prefix) {
        super(prefix);
    }

    @SneakyThrows
    @Override
    protected Filter create(
            FactoryConfiguration factoryConfiguration,
            Constructor constructor,
            List<Object> defaultParams) {
        return (Filter) constructor.newInstance();
    }
}
