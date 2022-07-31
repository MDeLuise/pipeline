package md.dev.factory;

import lombok.SneakyThrows;
import md.dev.action.Action;
import md.dev.processor.filter.Filter;
import md.dev.processor.transformer.Transformer;
import md.dev.plugin.PropertyFileReader;
import md.dev.plugin.PluginsProcessor;
import md.dev.trigger.Trigger;
import md.dev.trigger.output.TriggerOutput;
import md.dev.trigger.output.TriggerOutputImpl;
import md.dev.webapi.WebApi;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SuppressWarnings("checkstyle:ClassDataAbstractionCoupling")
public class ApplicationFactoriesBuilder {
    private final Set<File> packagesProperty = new HashSet<>();


    public void addPackage(Set<File> packagesFileProperty) {
        packagesProperty.addAll(packagesFileProperty);
    }


    public ApplicationFactories getApplicationFactories() {
        ApplicationFactories applicationFactories = new ApplicationFactories();
        applicationFactories.setTriggerFactories(createTriggerFactories());
        applicationFactories.setActionFactories(createActionFactories());
        applicationFactories.setFilterFactories(createFilterFactories());
        applicationFactories.setTransformerFactories(createTransformerFactories());
        return applicationFactories;
    }


    private EntityFactorySet<Trigger> createTriggerFactories() {
        EntityFactorySet<Trigger> triggerFactories = new EntityFactorySet<>();
        packagesProperty.forEach(packageName ->
            triggerFactories.addFactory(createTriggerFactory(packageName))
        );
        return triggerFactories;
    }


    private TriggerFactory createTriggerFactory(File pluginManifest) {
        PropertyFileReader pluginManifestReader =
            new PropertyFileReader(pluginManifest);
        String pluginPackage = pluginManifestReader.get("pluginPackage");
        TriggerFactory factory = new TriggerFactory(PluginsProcessor.getPrefix(pluginPackage));

        for (String triggerPackage :
            pluginManifestReader.get("triggerPackages").split(",")) {
            PluginsProcessor.getTriggers(triggerPackage).stream().
                forEach(triggerClass -> {
                    String id = triggerClass.getAnnotation(
                        md.dev.plugin.annotation.Trigger.class
                    ).id();
                    List<Object> defaultConstructorParams = new ArrayList<>(Arrays.asList(
                        new TriggerOutputImpl<>()
                    ));
                    Constructor triggerConstructor = getTriggerConstructor(
                        triggerClass, defaultConstructorParams
                    );
                    factory.addConstructor(id, triggerConstructor, defaultConstructorParams);
                });
        }
        return factory;
    }


    @SneakyThrows
    private Constructor getTriggerConstructor(Class<?> triggerClass,
                                              List<Object> defaultConstructorParams) {
        Constructor triggerConstructor;

        boolean isWeb = triggerClass.getAnnotation(md.dev.plugin.annotation.Trigger.class).webApi()
            != Class.class;

        if (!isWeb) {
            triggerConstructor = triggerClass.getConstructor(TriggerOutput.class);
        } else {
            triggerConstructor = triggerClass.getConstructor(
                TriggerOutput.class, WebApi.class
            );
            defaultConstructorParams.add(
                triggerClass.getAnnotation(md.dev.plugin.annotation.Trigger.class)
                    .webApi()
                    .getConstructor()
                    .newInstance()
            );
        }
        return triggerConstructor;
    }


    private EntityFactorySet<Action> createActionFactories() {
        EntityFactorySet<Action> actionFactories = new EntityFactorySet<>();
        packagesProperty.forEach(packageName ->
            actionFactories.addFactory(createActionFactory(packageName))
        );
        return actionFactories;
    }


    private EntityFactory<Action> createActionFactory(File pluginManifest) {
        PropertyFileReader pluginManifestReader =
            new PropertyFileReader(pluginManifest);
        String pluginPackage = pluginManifestReader.get("pluginPackage");
        ActionFactory factory = new ActionFactory(PluginsProcessor.getPrefix(pluginPackage));

        for (String actionPackage : pluginManifestReader.get("actionPackages").split(",")) {
            PluginsProcessor.getActions(actionPackage).stream().
                forEach(actionClass -> {
                    String id = actionClass.getAnnotation(
                        md.dev.plugin.annotation.Action.class
                    ).id();
                    List<Object> defaultConstructorParams = new ArrayList<>();
                    Constructor actionConstructor = getActionConstructor(
                        actionClass, defaultConstructorParams
                    );
                    factory.addConstructor(id, actionConstructor, defaultConstructorParams);
                });
        }
        return factory;
    }


    @SneakyThrows
    private Constructor getActionConstructor(Class<?> actionClass,
                                             List<Object> defaultConstructorParams) {
        Constructor actionConstructor;

        boolean isWeb = actionClass.getAnnotation(md.dev.plugin.annotation.Action.class).webApi()
            != Class.class;

        if (!isWeb) {
            actionConstructor = actionClass.getConstructor();
        } else {
            actionConstructor = actionClass.getConstructor(WebApi.class);
            defaultConstructorParams.add(
                actionClass.getAnnotation(md.dev.plugin.annotation.Action.class)
                    .webApi()
                    .getConstructor()
                    .newInstance()
            );
        }
        return actionConstructor;
    }


    private EntityFactorySet<Filter> createFilterFactories() {
        EntityFactorySet<Filter> filterFactories = new EntityFactorySet<>();
        packagesProperty.forEach(packageName ->
            filterFactories.addFactory(createFilterFactory(packageName))
        );
        return filterFactories;
    }


    @SneakyThrows
    private EntityFactory<Filter> createFilterFactory(File pluginManifest) {
        PropertyFileReader pluginManifestReader =
            new PropertyFileReader(pluginManifest);
        String pluginPackage = pluginManifestReader.get("pluginPackage");
        FilterFactory factory = new FilterFactory(PluginsProcessor.getPrefix(pluginPackage));

        for (String filterPackage :
            pluginManifestReader.get("filterPackages").split(",")) {
            PluginsProcessor.getFilters(filterPackage).stream().
                forEach(filterClass -> {
                    String id = filterClass.getAnnotation(
                        md.dev.plugin.annotation.Filter.class
                    ).id();
                    factory.addConstructor(
                        id,
                        getFilterConstructor(filterClass),
                        new ArrayList<>()
                    );
                });
        }
        return factory;
    }


    @SneakyThrows
    private Constructor getFilterConstructor(Class<Filter> filterClass) {
        return filterClass.getConstructor();
    }


    private EntityFactorySet<Transformer> createTransformerFactories() {
        EntityFactorySet<Transformer> transformerFactories = new EntityFactorySet<>();
        packagesProperty.forEach(packageName ->
            transformerFactories.addFactory(createTransformerFactory(packageName))
        );
        return transformerFactories;
    }


    @SneakyThrows
    private EntityFactory<Transformer> createTransformerFactory(File pluginManifest) {
        PropertyFileReader pluginManifestReader =
            new PropertyFileReader(pluginManifest);
        String pluginPackage = pluginManifestReader.get("pluginPackage");
        TransformerFactory factory =
            new TransformerFactory(PluginsProcessor.getPrefix(pluginPackage));

        for (String transformerPackage :
            pluginManifestReader.get("transformerPackages").split(",")) {
            PluginsProcessor.getTransformers(transformerPackage).stream().
                forEach(transformerClass -> {
                    String id = transformerClass.getAnnotation(
                        md.dev.plugin.annotation.Transformer.class).id();
                    factory.addConstructor(
                        id,
                        getTransformerConstructor(transformerClass),
                        new ArrayList<>()
                    );
                });
        }
        return factory;
    }


    @SneakyThrows
    private Constructor getTransformerConstructor(Class<Transformer> transformerClass) {
        return transformerClass.getConstructor();
    }


}
