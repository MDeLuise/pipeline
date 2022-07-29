package md.dev.pipeline.exception;

public class IncompatibleTypeException extends RuntimeException {

    public <T> IncompatibleTypeException(
            Class<?> element1,
            Class<?> element1Type,
            Class<?> element2,
            Class<?> element2Type) {
        super(String.format("Type of '%s' ('%s') is not compatible with type of '%s' ('%s')",
                element1, element1Type, element2, element2Type));
    }
}
