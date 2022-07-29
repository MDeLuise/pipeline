package md.dev.factory;

import md.dev.factory.exception.NamedElementNotFoundException;
import md.dev.modifier.filter.operator.AndFilterOperator;
import md.dev.modifier.filter.operator.FilterOperator;
import md.dev.modifier.filter.operator.LeafFilterOperator;
import md.dev.modifier.filter.operator.NotFilterOperator;
import md.dev.modifier.filter.operator.OrFilterOperator;

import java.lang.reflect.Constructor;
import java.util.List;

public class FilterOperatorFactory extends EntityFactory<FilterOperator> {

    public FilterOperatorFactory() {
        super("");
    }

    @Override
    public FilterOperator build(FactoryConfiguration factoryConfiguration) {
        final String NAME = (String) factoryConfiguration.get("name");
        return switch (NAME) {
            case "and" -> new AndFilterOperator<>();
            case "or" -> new OrFilterOperator<>();
            case "not" -> new NotFilterOperator<>();
            case "leaf" -> new LeafFilterOperator<>();
            default -> throw new NamedElementNotFoundException(NAME);
        };
    }

    @Override
    protected FilterOperator create(
            FactoryConfiguration factoryConfiguration, Constructor constructor,
            List<Object> defaultParams) {
        return null;
    }
}
