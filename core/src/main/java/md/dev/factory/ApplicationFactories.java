package md.dev.factory;

import lombok.Data;
import md.dev.action.Action;
import md.dev.processor.filter.Filter;
import md.dev.processor.filter.operator.FilterOperator;
import md.dev.processor.transformer.Transformer;
import md.dev.trigger.Trigger;

@Data
public class ApplicationFactories {
    private EntityFactorySet<Trigger> triggerFactories;
    private EntityFactorySet<Action> actionFactories;
    private EntityFactorySet<Filter> filterFactories;
    private EntityFactorySet<Transformer> transformerFactories;
    private EntityFactorySet<FilterOperator> filterOperatorFactories;
}
