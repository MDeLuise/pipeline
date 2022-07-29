package md.dev.plugin.documentation;

import lombok.Setter;
import md.dev.action.Action;
import md.dev.modifier.filter.Filter;
import md.dev.modifier.transformer.Transformer;
import md.dev.trigger.Trigger;

@Setter
class DocumentationTemplate {
    private final String PREFIX;
    private final String NAME;
    private AbstractEntityDescriptor<Trigger> triggersTable;
    private AbstractEntityDescriptor<Action> actionsTable;
    private AbstractEntityDescriptor<Filter> filtersTable;
    private AbstractEntityDescriptor<Transformer> transformersTable;


    DocumentationTemplate(String pluginName, String prefix) {
        this.NAME = pluginName;
        this.PREFIX = prefix;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("# ");
        builder.append(NAME);

        builder.append("\nPrefix used: `");
        builder.append(PREFIX);
        if (PREFIX.isBlank()) {
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
