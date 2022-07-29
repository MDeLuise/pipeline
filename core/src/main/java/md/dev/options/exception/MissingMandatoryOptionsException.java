package md.dev.options.exception;

public class MissingMandatoryOptionsException extends RuntimeException {
    public MissingMandatoryOptionsException(String key) {
        super(String.format("Mandatory key '%s' not provided", key));
    }

    public MissingMandatoryOptionsException() {
        super("Missing some mandatory option");
    }
}
