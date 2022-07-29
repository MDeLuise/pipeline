package md.dev.plugin.exception;

import java.util.Arrays;

public class RuntimeMissingMandatoryOption extends RuntimeException {
    public RuntimeMissingMandatoryOption(String... options) {
        super(String.format("at least one of the following options must be used: %s",
                Arrays.toString(options)
        ));
    }
}
