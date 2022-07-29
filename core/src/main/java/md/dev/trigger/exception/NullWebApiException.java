package md.dev.trigger.exception;

public class NullWebApiException extends RuntimeException {
    public NullWebApiException() {
        super("Cannot use null WebApi");
    }
}
