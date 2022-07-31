package md.dev.plugin.documentation;


import md.dev.processor.transformer.Transformer;
import md.dev.plugin.documentation.table.DescriptionRow;
import md.dev.plugin.documentation.table.DescriptionTable;
import md.dev.plugin.documentation.table.SimpleCell;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

class TransformerDescriptor extends AbstractEntityDescriptor<Transformer> {
    @SuppressWarnings("chekstyle:MagicNumber")
    public String createDescription() {
        descriptionTable = new DescriptionTable(new String[]{
            "Identifier", "Input type", "Output type", "Options", "Description"
        });
        List<Transformer> transformerList = new ArrayList<>(entities);
        transformerList.sort(Comparator.comparing(
            tr -> getId((Class<Transformer>) tr.getClass())
        ));
        for (Transformer transformer : transformerList) {
            Class<Transformer> transformerClass =
                (Class<Transformer>) transformer.getClass();

            DescriptionRow descriptionRow = new DescriptionRow(5);

            descriptionRow.addCell(new SimpleCell(String.valueOf(getId(transformerClass))));

            descriptionRow.addCell(new SimpleCell(removeClassWordIfStartWithIt(
                getInputType(transformerClass)))
            );

            descriptionRow.addCell(new SimpleCell(removeClassWordIfStartWithIt(
                getOutputType(transformerClass)))
            );

            descriptionRow.
                addInnerTable(createDescriptionForOptions(transformer.acceptedOptions()));

            descriptionRow.addCell(new SimpleCell(getDescription(transformerClass)));

            descriptionTable.addRow(descriptionRow);
        }
        return descriptionTable.toString();
    }


    private static String getId(Class<Transformer> action) {
        return action.getAnnotation(md.dev.plugin.annotation.Transformer.class).id();
    }


    private static String getDescription(Class<Transformer> action) {
        return action.getAnnotation(md.dev.plugin.annotation.Transformer.class).description();
    }


    private String getInputType(Class<Transformer> transformer) {
        return removeClassWordIfStartWithIt(transformer.getAnnotation(
            md.dev.plugin.annotation.Transformer.class).inputType().toString()
        );
    }


    private String getOutputType(Class<Transformer> transformer) {
        return removeClassWordIfStartWithIt(transformer.getAnnotation(
            md.dev.plugin.annotation.Transformer.class).outputType().toString()
        );
    }
}
