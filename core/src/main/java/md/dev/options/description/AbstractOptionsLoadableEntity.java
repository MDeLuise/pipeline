package md.dev.options.description;

import lombok.NonNull;
import md.dev.options.Options;

public abstract class AbstractOptionsLoadableEntity implements OptionsLoadableEntity {
    protected Options options;

    @Override
    public void loadOptions(@NonNull Options options) {
        this.options = options;
        loadClassOptions(options);
    }

    protected abstract void loadClassOptions(Options options);

}
