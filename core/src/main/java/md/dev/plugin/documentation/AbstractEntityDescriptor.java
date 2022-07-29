package md.dev.plugin.documentation;

import md.dev.options.description.OptionDescription;
import md.dev.plugin.documentation.table.DescriptionRow;
import md.dev.plugin.documentation.table.DescriptionTable;
import md.dev.plugin.documentation.table.SimpleCell;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

abstract class AbstractEntityDescriptor<T> {
    protected final Set<T> ENTITIES = new HashSet<>();
    protected DescriptionTable descriptionTable;


    public abstract String createDescription();

    public void addEntity(T entity) {
        ENTITIES.add(entity);
    }

    @SuppressWarnings("chekstyle:MagicNumber")
    protected DescriptionTable createDescriptionForOptions(
            Collection<OptionDescription> optionDescriptions) {
        final DescriptionTable OPTIONS_CELL = new DescriptionTable(new String[] {
            "Name", "Type", "Default", "Description"
        });
        for (OptionDescription optionDescription : optionDescriptions) {
            DescriptionRow optionDescriptionRow = new DescriptionRow(4);
            optionDescriptionRow.addCell(new SimpleCell(optionDescription.getOption()));
            optionDescriptionRow.addCell(new SimpleCell(removeClassWordIfStartWithIt(
                    optionDescription.getType().toString()))
            );
            optionDescriptionRow.addCell(new SimpleCell(optionDescription.getDefaultOptionValue()));
            optionDescriptionRow.addCell(new SimpleCell(optionDescription.getDescription()));
            OPTIONS_CELL.addRow(optionDescriptionRow);
        }
        return OPTIONS_CELL;
    }


    protected String removeClassWordIfStartWithIt(String stringToProcess) {
        if (!stringToProcess.contains(".")) {
            return stringToProcess;
        }

        String[] splittedString = stringToProcess.split("\\.");
        return splittedString[splittedString.length - 1];
    }

}
