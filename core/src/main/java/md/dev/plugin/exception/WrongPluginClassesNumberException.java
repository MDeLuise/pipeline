package md.dev.plugin.exception;

public class WrongPluginClassesNumberException extends RuntimeException {
    public WrongPluginClassesNumberException(String packageName, int size) {
        super(String.format(
            "Expected classes annotated with @Plugin: 1, but found %s in %s package",
            size, packageName
        ));
    }
}
