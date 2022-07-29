package md.dev.factory.exception;

public class NamedElementNotFoundException extends RuntimeException {

    public NamedElementNotFoundException(String name) {
        super("Cannot build element with name \"" + name + "\"");
    }
}
