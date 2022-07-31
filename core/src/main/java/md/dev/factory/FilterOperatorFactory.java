package md.dev.factory;

import md.dev.factory.exception.NamedElementNotFoundException;
import md.dev.processor.filter.operator.AndFilterOperator;
import md.dev.processor.filter.operator.FilterOperator;
import md.dev.processor.filter.operator.LeafFilterOperator;
import md.dev.processor.filter.operator.NotFilterOperator;
import md.dev.processor.filter.operator.OrFilterOperator;

import java.lang.reflect.Constructor;
import java.util.List;

public class FilterOperatorFactory extends EntityFactory<FilterOperator> {

    public FilterOperatorFactory() {
        super("");
    }


    @Override
    public FilterOperator build(FactoryConfiguration factoryConfiguration) {
        String name = (String) factoryConfiguration.get("name");
        return switch (name) {
            case "and" -> new AndFilterOperator<>();
            case "or" -> new OrFilterOperator<>();
            case "not" -> new NotFilterOperator<>();
            case "leaf" -> new LeafFilterOperator<>();
            default -> throw new NamedElementNotFoundException(name);
        };
    }


    @Override
    protected FilterOperator create(FactoryConfiguration factoryConfiguration,
                                    Constructor constructor,
                                    List<Object> defaultParams) {
        return null;
    }
}
