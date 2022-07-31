package md.dev.options;

import md.dev.options.description.OptionDescription;
import md.dev.options.exception.MissingMandatoryOptionsException;
import md.dev.options.exception.OptionMismatchTypeException;

import java.util.Collection;

public class OptionsValidator {

    public static void validateOptions(Collection<OptionDescription> acceptedOptions,
                                       Options givenOptions) {

        if (givenOptions.size() == 0) {
            acceptNoOption(acceptedOptions);
        } else {
            acceptedOptions.forEach(key -> {
                checkTypeCompliance(acceptedOptions, key.getOption(), givenOptions);
                checkMandatoryOption(acceptedOptions, key.getOption(), givenOptions);
            });
        }
    }


    public static void acceptNoOption(Collection<OptionDescription> acceptedOptions) {
        boolean mandatoryMissingOptions = acceptedOptions.stream()
            .anyMatch(OptionDescription::isMandatory);
        if (mandatoryMissingOptions) {
            throw new MissingMandatoryOptionsException();
        }
    }


    private static void checkTypeCompliance(Collection<OptionDescription> acceptedOptions,
                                            String key, Options givenOptions) {
        if (!givenOptions.has(key)) {
            return;
        }
        Class wantedValueClass = giveWantedOptionType(acceptedOptions, key);
        Class valueClass = givenOptions.get(key).getClass();

        if (wantedValueClass != null && wantedValueClass.isInterface()) {
            if (!wantedValueClass.isAssignableFrom(valueClass)) {
                throw new OptionMismatchTypeException(key, wantedValueClass, valueClass);
            }
        } else if (wantedValueClass != null && !wantedValueClass.isAssignableFrom(valueClass)) {
            throw new OptionMismatchTypeException(key, wantedValueClass, valueClass);
        }
    }


    private static Class giveWantedOptionType(Collection<OptionDescription> acceptedOptions,
                                              String key) {
        OptionDescription optionDescription =
            getOptionDescriptionFromCollection(acceptedOptions, key);
        return optionDescription != null ? optionDescription.getType() : null;
    }


    private static void checkMandatoryOption(Collection<OptionDescription> acceptedOptions,
                                             String key, Options givenOptions) {
        OptionDescription optionDescription =
            getOptionDescriptionFromCollection(acceptedOptions, key);
        boolean isMandatory = optionDescription != null && optionDescription.isMandatory();
        if (isMandatory && !givenOptions.has(key)) {
            throw new MissingMandatoryOptionsException(key);
        }
    }


    private static OptionDescription getOptionDescriptionFromCollection(
        Collection<OptionDescription> options, String option) {
        for (OptionDescription optionDescription : options) {
            if (optionDescription.getOption().equals(option)) {
                return optionDescription;
            }
        }
        return null;
    }
}
