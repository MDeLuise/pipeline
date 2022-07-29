package md.dev.plugin.documentation;

import md.dev.modifier.filter.Filter;
import md.dev.plugin.documentation.table.DescriptionRow;
import md.dev.plugin.documentation.table.DescriptionTable;
import md.dev.plugin.documentation.table.SimpleCell;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

class FilterDescriptor extends AbstractEntityDescriptor<Filter> {

    @SuppressWarnings("chekstyle:MagicNumber")
    @Override
    public String createDescription() {
        descriptionTable = new DescriptionTable(new String[] {
            "Identifier", "Type used", "Options", "Description"
        });
        final List<Filter> FILTER_LIST = new ArrayList<>(ENTITIES);
        FILTER_LIST.sort(Comparator.comparing(fi -> getId((Class<Filter>) fi.getClass())));
        for (Filter filter: FILTER_LIST) {
            final Class<Filter> FILTER_CLASS = (Class<Filter>) filter.getClass();

            DescriptionRow descriptionRow = new DescriptionRow(4);

            descriptionRow.addCell(new SimpleCell(String.valueOf(getId(FILTER_CLASS))));

            descriptionRow.addCell(new SimpleCell(
                    removeClassWordIfStartWithIt(getType(FILTER_CLASS)))
            );

            descriptionRow.addInnerTable(createDescriptionForOptions(filter.acceptedOptions()));

            descriptionRow.addCell(new SimpleCell(getDescription(FILTER_CLASS)));

            descriptionTable.addRow(descriptionRow);
        }

        return descriptionTable.toString();
    }


    private static String getId(Class<Filter> action) {
        return action.getAnnotation(md.dev.plugin.annotation.Filter.class).id();
    }

    private static String getDescription(Class<Filter> action) {
        return action.getAnnotation(md.dev.plugin.annotation.Filter.class).description();
    }

    private String getType(Class<Filter> action) {
        return removeClassWordIfStartWithIt(action.getAnnotation(
                md.dev.plugin.annotation.Filter.class).inputType().toString()
        );
    }
}
