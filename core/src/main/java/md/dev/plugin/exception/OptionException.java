package md.dev.plugin.exception;

public class OptionException extends RuntimeException {
    public OptionException(String param, String errorMsg) {
        super(String.format("error with option '%s': %s", param, errorMsg));
    }
}
