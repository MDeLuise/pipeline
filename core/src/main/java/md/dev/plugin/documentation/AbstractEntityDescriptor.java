package md.dev.plugin.documentation;

import md.dev.options.description.OptionDescription;
import md.dev.plugin.documentation.table.DescriptionRow;
import md.dev.plugin.documentation.table.DescriptionTable;
import md.dev.plugin.documentation.table.SimpleCell;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

abstract class AbstractEntityDescriptor<T> {
    protected DescriptionTable descriptionTable;
    protected final Set<T> entities = new HashSet<>();


    public abstract String createDescription();


    public void addEntity(T entity) {
        entities.add(entity);
    }


    @SuppressWarnings("chekstyle:MagicNumber")
    protected DescriptionTable createDescriptionForOptions(
        Collection<OptionDescription> optionDescriptions) {
        DescriptionTable optionsCell = new DescriptionTable(new String[]{
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
            optionsCell.addRow(optionDescriptionRow);
        }
        return optionsCell;
    }


    protected String removeClassWordIfStartWithIt(String stringToProcess) {
        if (!stringToProcess.contains(".")) {
            return stringToProcess;
        }

        String[] explodedString = stringToProcess.split("\\.");
        return explodedString[explodedString.length - 1];
    }

}
