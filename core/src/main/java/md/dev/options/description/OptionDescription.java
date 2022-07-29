package md.dev.options.description;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OptionDescription {
    String option;
    String description;
    Class type;
    String defaultOptionValue;
    boolean mandatory;
}
