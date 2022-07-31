package md.dev.plugin.documentation;

import lombok.Setter;
import md.dev.action.Action;
import md.dev.processor.filter.Filter;
import md.dev.processor.transformer.Transformer;
import md.dev.trigger.Trigger;

@Setter
class DocumentationTemplate {
    private AbstractEntityDescriptor<Trigger> triggersTable;
    private AbstractEntityDescriptor<Action> actionsTable;
    private AbstractEntityDescriptor<Filter> filtersTable;
    private AbstractEntityDescriptor<Transformer> transformersTable;
    private final String prefix;
    private final String name;


    DocumentationTemplate(String pluginName, String prefix) {
        this.name = pluginName;
        this.prefix = prefix;
    }


    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("# ");
        builder.append(name);

        builder.append("\nPrefix used: `");
        builder.append(prefix);
        if (prefix.isBlank()) {
            builder.append(" `(_no prefix_)");
        } else {
            builder.append("`");
        }

        builder.append("\n## Triggers");
        builder.append(triggersTable.createDescription());

        builder.append("\n## Actions");
        builder.append(actionsTable.createDescription());

        builder.append("\n## Filters");
        builder.append(filtersTable.createDescription());

        builder.append("\n## Transformers");
        builder.append(transformersTable.createDescription());

        return builder.toString();
    }
}
