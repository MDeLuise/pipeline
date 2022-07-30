package md.dev;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import md.dev.facade.ApplicationLoader;
import md.dev.facade.FileToPipelineConfigurationConverter;
import md.dev.factory.ApplicationFactories;
import md.dev.factory.ApplicationFactoriesBuilder;
import md.dev.picocli.converter.LogLevelConverter;
import md.dev.picocli.converter.OptionsConverter;
import md.dev.plugin.PluginsProcessor;
import md.dev.plugin.PluginsValidator;
import md.dev.plugin.documentation.DocumentationGenerator;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

public class Application implements Callable<Void> {

    @CommandLine.Option(
            names = {"-i", "--inline"},
            converter = OptionsConverter.class,
            arity = "0..*"
    )
    List<JSONObject> inlineConfigs;

    @CommandLine.Option(
            names = {"-f", "--from-file"},
            arity = "0..*"
    )
    List<File> fileConfigs;

    @CommandLine.Option(
            names = {"-v", "--verbose"},
            converter = LogLevelConverter.class,
            defaultValue = "WARN"
    )
    Level logLevel;

    @CommandLine.Option(
            names = {"-d", "--documentation"},
            arity = "0..1"
    )
    File pluginManifestFileToGenerateDocumentation;


    public static void main(String... args) {
        new CommandLine(new Application()).
                setCaseInsensitiveEnumValuesAllowed(true).
                execute(args);
    }


    @Override
    public Void call() {
        Logger root = (ch.qos.logback.classic.Logger)
                org.slf4j.LoggerFactory.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);
        root.setLevel(logLevel);

        Logger reflectionsLog = (ch.qos.logback.classic.Logger)
                LoggerFactory.getLogger("org.reflections");
        reflectionsLog.setLevel(ch.qos.logback.classic.Level.OFF);

        Set<File> pluginManifestFiles =
                PluginsProcessor.getAllManifestPluginFilesInDir(false);
        PluginsValidator.validatePlugins(pluginManifestFiles);

        ApplicationFactoriesBuilder applicationFactoriesBuilder = new ApplicationFactoriesBuilder();
        applicationFactoriesBuilder.addPackage(pluginManifestFiles);
        ApplicationFactories applicationFactories =
                applicationFactoriesBuilder.getApplicationFactories();

        if (pluginManifestFileToGenerateDocumentation != null) {
            DocumentationGenerator documentationGenerator = new DocumentationGenerator(
                    pluginManifestFileToGenerateDocumentation,
                    applicationFactories
            );
            documentationGenerator.createDocumentation();
            System.exit(0);
        }

        Set<ApplicationLoader> applicationLoaderSet = new HashSet<>();
        if (fileConfigs != null) {
            for (File fileConfig : fileConfigs) {
                applicationLoaderSet.addAll(
                        FileToPipelineConfigurationConverter.convert(
                                fileConfig,
                                applicationFactories
                        )
                );
            }
        }
        if (inlineConfigs != null) {
            for (JSONObject inlineConfig : inlineConfigs) {
                applicationLoaderSet.addAll(
                        FileToPipelineConfigurationConverter.convert(
                                inlineConfig.toString(),
                                "json",
                                applicationFactories
                        )
                );
            }
        }

        applicationLoaderSet.forEach(ApplicationLoader::buildAndStart);
        return null;
    }
}
