package md.dev.plugin.documentation;

import md.dev.plugin.documentation.table.DescriptionRow;
import md.dev.plugin.documentation.table.DescriptionTable;
import md.dev.plugin.documentation.table.SimpleCell;
import md.dev.trigger.AbstractPeriodicTrigger;
import md.dev.trigger.Trigger;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

class TriggerDescriptor extends AbstractEntityDescriptor<Trigger> {

    @SuppressWarnings("chekstyle:MagicNumber")
    @Override
    public String createDescription() {
        descriptionTable = new DescriptionTable(new String[]{
            "Identifier", "Periodic", "Type sent", "Options", "Description"
        });

        List<Trigger> triggerList = new ArrayList<>(entities);
        triggerList.sort(Comparator.comparing(tr -> getId((Class<Trigger>) tr.getClass())));
        for (Trigger trigger : triggerList) {
            Class<Trigger> triggerClass = (Class<Trigger>) trigger.getClass();

            DescriptionRow descriptionRow = new DescriptionRow(5);

            descriptionRow.addCell(new SimpleCell(getId(triggerClass)));

            descriptionRow.addCell(new SimpleCell(String.valueOf(isPeriodic(triggerClass))));

            descriptionRow.addCell(new SimpleCell(
                removeClassWordIfStartWithIt(getType(triggerClass)))
            );

            descriptionRow.addInnerTable(createDescriptionForOptions(trigger.acceptedOptions()));

            descriptionRow.addCell(new SimpleCell(getDescription(triggerClass)));

            descriptionTable.addRow(descriptionRow);
        }
        return descriptionTable.toString();
    }


    private static boolean isPeriodic(Class<Trigger> trigger) {
        return AbstractPeriodicTrigger.class.isAssignableFrom(trigger);
    }


    private static String getId(Class<Trigger> trigger) {
        return trigger.getAnnotation(md.dev.plugin.annotation.Trigger.class).id();
    }


    private static String getDescription(Class<Trigger> trigger) {
        return trigger.getAnnotation(md.dev.plugin.annotation.Trigger.class).description();
    }


    private String getType(Class<Trigger> trigger) {
        return removeClassWordIfStartWithIt(trigger.getAnnotation(
            md.dev.plugin.annotation.Trigger.class).outputType().toString()
        );
    }
}
