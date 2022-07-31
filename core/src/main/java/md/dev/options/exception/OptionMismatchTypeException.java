package md.dev.options.exception;

public class OptionMismatchTypeException extends RuntimeException {
    public OptionMismatchTypeException(String optionName, Class wantedType, Class givenType) {
        super(String.format(
            "Wanted type of option %s: %s, but given: %s", optionName, wantedType, givenType)
        );
    }
}
