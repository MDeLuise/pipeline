package md.dev.plugin.documentation;

import md.dev.action.Action;
import md.dev.plugin.documentation.table.DescriptionRow;
import md.dev.plugin.documentation.table.DescriptionTable;
import md.dev.plugin.documentation.table.SimpleCell;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

class ActionDescriptor extends AbstractEntityDescriptor<Action> {

    @SuppressWarnings("chekstyle:MagicNumber")
    @Override
    public String createDescription() {
        descriptionTable = new DescriptionTable(new String[]{
            "Identifier", "Type used", "Options", "Description"
        });
        final List<Action> ACTION_LIST = new ArrayList<>(ENTITIES);
        ACTION_LIST.sort(Comparator.comparing(ac -> getId((Class<Action>) ac.getClass())));
        for (Action action: ACTION_LIST) {
            final Class<Action> ACTION_CLASS = (Class<Action>) action.getClass();

            DescriptionRow descriptionRow = new DescriptionRow(4);

            descriptionRow.addCell(new SimpleCell(String.valueOf(getId(ACTION_CLASS))));

            descriptionRow.addCell(new SimpleCell(
                    removeClassWordIfStartWithIt(getType(ACTION_CLASS)))
            );

            descriptionRow.addInnerTable(createDescriptionForOptions(action.acceptedOptions()));

            descriptionRow.addCell(new SimpleCell(getDescription(ACTION_CLASS)));

            descriptionTable.addRow(descriptionRow);
        }
        return descriptionTable.toString();
    }


    private static String getId(Class<Action> action) {
        return action.getAnnotation(md.dev.plugin.annotation.Action.class).id();
    }

    private static String getDescription(Class<Action> action) {
        return action.getAnnotation(md.dev.plugin.annotation.Action.class).description();
    }

    private String getType(Class<Action> action) {
        return removeClassWordIfStartWithIt(action.getAnnotation(
                md.dev.plugin.annotation.Action.class).inputType().toString()
        );
    }
}
