package md.dev.plugin;


import md.dev.Application;
import md.dev.action.Action;
import md.dev.modifier.filter.Filter;
import md.dev.modifier.transformer.Transformer;
import md.dev.plugin.annotation.Plugin;
import md.dev.trigger.Trigger;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.CodeSource;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class PluginsProcessor {
    private static final Logger LOG = LoggerFactory.getLogger(PluginsProcessor.class);

    public static Set<File> getAllManifestPluginFilesInDir(boolean isRelease) {
        final String ENDING_STR = "plugin-manifest.property";
        return isRelease ?
                getAllManifestPluginFilesInJar(ENDING_STR) :
                getAllManifestPluginFilesInDir(new File("."), new HashSet<>(), ENDING_STR);
    }

    public static Set<Class<Trigger>> getTriggers(String packageName) {
        return getEntities(md.dev.plugin.annotation.Trigger.class, packageName);
    }

    public static Set<Class<Action>> getActions(String packageName) {
        return getEntities(md.dev.plugin.annotation.Action.class, packageName);
    }

    public static Set<Class<Filter>> getFilters(String packageName) {
        return getEntities(md.dev.plugin.annotation.Filter.class, packageName);
    }

    public static Set<Class<Transformer>> getTransformers(String packageName) {
        return getEntities(md.dev.plugin.annotation.Transformer.class, packageName);
    }

    public static Set<String> getPrefixes(String packageName) {
        final Set<Class> PLUGIN_CLASSES = getEntities(Plugin.class, packageName);
        final Set<String> PREFIXES = new HashSet<>();
        PLUGIN_CLASSES.forEach(aClass ->
                PREFIXES.add(((Plugin) aClass.getAnnotation(Plugin.class)).prefix())
        );
        return PREFIXES;
    }

    public static String getPrefix(String packageName) {
        final Set<String> PREFIXES = getPrefixes(packageName);
        return PREFIXES.toArray(new String[0])[0];
    }


    private static Set<File> getAllManifestPluginFilesInDir(
            File directory, Set<File> manifestFile,
            String ending) {
        LOG.debug("searching manifest files inside directory {}", directory);
        final String[] EXCLUDE_DIRS = {
            "build-tools",
            "src"
        };

        File[] filesInDirectory = directory.listFiles();
        try {
            Arrays.stream(filesInDirectory).forEach(fileInDirectory -> {
                if (fileInDirectory.isDirectory() && Arrays.stream(EXCLUDE_DIRS).
                        noneMatch(bar -> bar.equals(fileInDirectory.getName()))) {
                    getAllManifestPluginFilesInDir(fileInDirectory, manifestFile, ending);
                } else {
                    if (fileInDirectory.getName().endsWith(ending)) {
                        manifestFile.add(fileInDirectory);
                        LOG.info("found manifest file: {}", fileInDirectory.getAbsoluteFile());
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return manifestFile;
    }

    public static Set<File> getAllManifestPluginFilesInJar(String ending) {
        Set<File> manifestFiles = new HashSet<>();
        CodeSource src = Application.class.getProtectionDomain().getCodeSource();
        LOG.debug("searching manifest files inside jar {}", src);
        if (src != null) {
            URL jar = src.getLocation();
            ZipInputStream zip = null;
            try {
                zip = new ZipInputStream(jar.openStream());
            } catch (IOException e) {
                LOG.error("error searching inside jar {}", jar, e);
            }
            while (true) {
                ZipEntry entry = null;
                try {
                    entry = zip.getNextEntry();
                } catch (IOException e) {
                    LOG.error("no entry inside jar {}", jar, e);
                }
                if (entry == null) {
                    break;
                }
                String name = entry.getName();
                if (name.endsWith(ending)) {
                    manifestFiles.add(new File(name));
                    LOG.info("found manifest file: {}", name);
                }
            }
        } else {
            LOG.error("cannot find jar inside which search manifest files");
        }
        return manifestFiles;
    }


    private static Set getEntities(Class annotation, String packageName) {
        Reflections reflections = new Reflections(packageName);
        return reflections.getTypesAnnotatedWith(annotation);
    }
}
