package md.dev.plugin;

import md.dev.plugin.exception.PrefixNotUniqueException;
import md.dev.plugin.exception.WrongPluginClassesNumberException;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class PluginsValidator {

    private static class PackageNameWithPrefix {
        String packageName;
        String prefix;
    }


    public static void validatePlugins(Set<File> packagesFileProperty) {
        Set<String> pluginPackageNames = new HashSet<>();
        for (File fileProperty : packagesFileProperty) {
            PropertyFileReader propertyFileReader = new PropertyFileReader(fileProperty);
            pluginPackageNames.add(propertyFileReader.get("pluginPackage"));
        }
        checkPrefixesUniqueness(pluginPackageNames);
    }


    private static void checkPrefixesUniqueness(Set<String> packagesNames) {
        final Set<PackageNameWithPrefix> PREFIXES = new HashSet<>();

        final PluginsProcessor PLUGIN_PROCESSOR = new PluginsProcessor();
        for (String packageName : packagesNames) {
            Set<String> packagePrefixes = PLUGIN_PROCESSOR.getPrefixes(packageName);
            if (packagePrefixes.size() != 1) {
                throw new WrongPluginClassesNumberException(packageName, packagePrefixes.size());
            }

            String newPrefix = (new ArrayList<>(packagePrefixes)).get(0);
            List<PackageNameWithPrefix> alreadyIncurredPrefix = PREFIXES.stream().
                    filter(packageNameWithPrefix ->
                            packageNameWithPrefix.prefix.equals(newPrefix)).
                    collect(Collectors.toList());

            if (alreadyIncurredPrefix.size() != 0) {
                throw new PrefixNotUniqueException(
                        newPrefix,
                        packageName,
                        alreadyIncurredPrefix.get(0).packageName
                );
            }

            PackageNameWithPrefix packageNameWithPrefix = new PackageNameWithPrefix();
            packageNameWithPrefix.prefix = newPrefix;
            packageNameWithPrefix.packageName = packageName;
            PREFIXES.add(packageNameWithPrefix);
        }
    }
}
