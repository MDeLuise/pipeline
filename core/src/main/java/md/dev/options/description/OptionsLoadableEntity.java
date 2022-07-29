package md.dev.options.description;

import lombok.NonNull;
import md.dev.options.Options;

import java.util.Collection;

public interface OptionsLoadableEntity {
    Collection<OptionDescription> acceptedOptions();

    void loadOptions(@NonNull Options options);

    void initializeOptions();
}
