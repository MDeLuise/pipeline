package md.dev.plugin.documentation;

import lombok.SneakyThrows;
import md.dev.action.Action;
import md.dev.factory.ApplicationFactories;
import md.dev.processor.filter.Filter;
import md.dev.processor.transformer.Transformer;
import md.dev.plugin.PropertyFileReader;
import md.dev.plugin.PluginsProcessor;
import md.dev.trigger.Trigger;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.FileSystems;

@SuppressWarnings("checkstyle:ClassDataAbstractionCoupling")
public class DocumentationGenerator {
    private final File pluginManifestFile;
    private final ApplicationFactories applicationFactories;


    public DocumentationGenerator(File pluginManifestFile,
                                  ApplicationFactories applicationFactories) {
        this.pluginManifestFile = pluginManifestFile;
        this.applicationFactories = applicationFactories;
    }


    public void createDocumentation() {
        String separator = FileSystems.getDefault().getSeparator();
        String[] explodedPath =
            pluginManifestFile.getAbsolutePath().split(separator);
        StringBuilder readmeFilePath = new StringBuilder();
        for (String explodedSection : explodedPath) {
            if ("src".equals(explodedSection)) {
                break;
            }
            readmeFilePath.append(explodedSection);
            readmeFilePath.append(separator);
        }
        readmeFilePath.append("README.md");
        createDocumentation(new File(readmeFilePath.toString()));
    }


    @SneakyThrows
    public void createDocumentation(File file) {
        final PropertyFileReader PLUGIN_MANIFEST_READER =
            new PropertyFileReader(pluginManifestFile);
        String packageName = PLUGIN_MANIFEST_READER.get("pluginPackage");
        String pluginName = PLUGIN_MANIFEST_READER.get("pluginName");
        String prefix = PluginsProcessor.getPrefix(packageName);

        DocumentationTemplate documentationTemplate = new DocumentationTemplate(
            pluginName, prefix
        );

        createTriggerDocumentation(packageName, prefix, documentationTemplate);
        createActionDocumentation(packageName, prefix, documentationTemplate);
        createFilterDocumentation(packageName, prefix, documentationTemplate);
        createTransformerDocumentation(packageName, prefix, documentationTemplate);

        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(documentationTemplate.toString());
        fileWriter.close();
    }


    private void createTransformerDocumentation(String packageName, String prefix,
                                                DocumentationTemplate documentationTemplate) {
        AbstractEntityDescriptor<Transformer> transformerDescriptor = new TransformerDescriptor();
        for (Class<Transformer> transformer : PluginsProcessor.getTransformers(packageName)) {
            Transformer transformerInstance = applicationFactories.getTransformerFactories().build(
                prependPrefixToName(
                    prefix,
                    transformer
                        .getAnnotation(md.dev.plugin.annotation.Transformer.class)
                        .id()
                )
            );
            transformerDescriptor.addEntity(transformerInstance);
        }
        documentationTemplate.setTransformersTable(transformerDescriptor);
    }


    private void createFilterDocumentation(String packageName, String prefix,
                                           DocumentationTemplate documentationTemplate) {
        AbstractEntityDescriptor<Filter> filterDescriptor = new FilterDescriptor();
        for (Class<Filter> filter : PluginsProcessor.getFilters(packageName)) {
            Filter filterInstance = applicationFactories.getFilterFactories().build(
                prependPrefixToName(
                    prefix,
                    filter.getAnnotation(md.dev.plugin.annotation.Filter.class).id()
                ));
            filterDescriptor.addEntity(filterInstance);
        }
        documentationTemplate.setFiltersTable(filterDescriptor);
    }


    private void createActionDocumentation(String packageName, String prefix,
                                           DocumentationTemplate documentationTemplate) {
        AbstractEntityDescriptor<Action> actionDescriptor = new ActionDescriptor();
        for (Class<Action> action : PluginsProcessor.getActions(packageName)) {
            Action actionInstance = applicationFactories.getActionFactories().build(
                prependPrefixToName(
                    prefix,
                    action.getAnnotation(md.dev.plugin.annotation.Action.class).id()
                ));
            actionDescriptor.addEntity(actionInstance);
        }
        documentationTemplate.setActionsTable(actionDescriptor);
    }


    private void createTriggerDocumentation(String packageName, String prefix,
                                            DocumentationTemplate documentationTemplate) {
        AbstractEntityDescriptor<Trigger> triggerDescriptor = new TriggerDescriptor();
        for (Class<Trigger> trigger : PluginsProcessor.getTriggers(packageName)) {
            Trigger triggerInstance = applicationFactories.getTriggerFactories().build(
                prependPrefixToName(
                    prefix,
                    trigger.getAnnotation(md.dev.plugin.annotation.Trigger.class).id()
                ));
            triggerDescriptor.addEntity(triggerInstance);
        }
        documentationTemplate.setTriggersTable(triggerDescriptor);
    }


    private String prependPrefixToName(String prefix, String name) {
        if (prefix.isBlank()) {
            return name;
        }
        return prefix + "." + name;
    }

}
