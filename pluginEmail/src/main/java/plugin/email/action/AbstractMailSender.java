package plugin.email.action;

import md.dev.action.AbstractWebAction;
import md.dev.options.Options;
import md.dev.options.description.OptionDescription;
import md.dev.webapi.WebApi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;


public abstract class AbstractMailSender extends AbstractWebAction<String> {
    protected String to;
    protected String from;
    protected String subject;
    protected Optional<String> text;


    public AbstractMailSender(WebApi webApi) {
        super(webApi);
    }


    @Override
    public void initializeClassOptions() {
        text = Optional.empty();
        initializeImplementationOptions();
    }


    protected abstract void initializeImplementationOptions();


    @Override
    protected void loadInstanceOptions(Options options) {
        if (options.has("to")) {
            to = options.getString("to");
        }
        if (options.has("from")) {
            from = options.getString("from");
        }
        if (options.has("subject")) {
            subject = options.getString("subject");
        }
        if (options.has("text")) {
            text = Optional.of(options.getString("text"));
        }
        loadImplementationOptions(options);
    }


    protected abstract void loadImplementationOptions(Options options);


    @Override
    protected Collection<OptionDescription> acceptedClassOptions() {
        Collection<OptionDescription> abstractOptions = new ArrayList<>(Arrays.asList(
            new OptionDescription(
                "to",
                "Recipient of the email, separated by comma.",
                String.class,
                "null",
                true
            ),
            new OptionDescription(
                "from",
                "Sender of the email.",
                String.class,
                "null",
                true
            ),
            new OptionDescription(
                "subject",
                "Subject of the email.",
                String.class,
                "null",
                true
            ),
            new OptionDescription(
                "text",
                "Body of the email, if empty use passed triggerOutput.",
                String.class,
                "null",
                false
            )
        ));
        abstractOptions.addAll(acceptedImplementationOptions());
        return abstractOptions;
    }


    protected abstract Collection<OptionDescription> acceptedImplementationOptions();
}
