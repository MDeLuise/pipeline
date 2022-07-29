package md.dev.plugin.exception;


public class PrefixNotUniqueException extends RuntimeException {
    public PrefixNotUniqueException(
            String prefix,
            String packageName,
            String packageNameAlreadyLoaded) {
        super(String.format("Prefix %s of package %s is already used inside package %s",
                prefix, packageName, packageNameAlreadyLoaded)
        );
    }
}
