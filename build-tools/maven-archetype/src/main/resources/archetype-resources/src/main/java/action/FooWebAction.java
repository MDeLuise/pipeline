package ${package}.action;

import md.dev.action.AbstractWebAction;
import md.dev.options.Options;
import md.dev.options.description.OptionDescription;
import md.dev.plugin.annotation.Action;
import md.dev.trigger.output.TriggerOutput;
import ${package}.webapi.FooWebApi1;
import md.dev.webapi.WebApi;
import md.dev.webapi.configuration.WebApiConfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

@SuppressWarnings({
      "checkstyle:NonEmptyAtclauseDescription",
      "checkstyle:JavadocStyle",
      "checkstyle:JavadocTagContinuationIndentation",
      "checkstyle:JavadocParagraph",
      "checkstyle:InvalidJavadocPosition",
      "checkstyle:AtclauseOrder",
      "checkstyle:SingleLineJavadoc"
})
/**
 * This is an example of Action.
 * "extends AbstractWebAction" means that:
 *      - this action accept a triggerOutput of type String,
 *      - this is a web action (i.e. does use WebApi object to perform web requests).
 */
@Action(
    id = "actFoo1",
    webApi = FooWebApi1.class,
    inputType = String.class,
    description = "..."
)
public class FooWebAction extends AbstractWebAction {
    private String accessToken;


    public FooWebAction(WebApi webApi) {
        super(webApi);
    }

    /**
     * Load used options.
     * @param options: options to load
     */
    @Override
    protected void loadInstanceOptions(Options options) {
        if (options != null && options.has("tokenVar")) {
            accessToken = System.getenv(options.getString("tokenVar"));
        }
    }

    /**
     * List which options are loaded by the action.
     * @return collection of used options.
     */
    @SuppressWarnings("checkstyle:LineLength")
    @Override
    protected Collection<OptionDescription> acceptedClassOptions() {
        return new ArrayList<>(Arrays.asList(
                new OptionDescription(
                        "tokenVar", // name of the option
                        "Name of the global var containing the bot access token.", // description of the option
                        String.class, // type of the option
                        "null", // default value of the option,
                        true // is option mandatory?
                )
        ));
    }

    /**
     * Perform the action.
     * @param triggerOutput: data from the pipeline.
     */
    @Override
    public void doAction(TriggerOutput triggerOutput) {
        WebApiConfiguration webApiConfiguration = new WebApiConfiguration();
        webApiConfiguration.insert("token", accessToken);
        webApiConfiguration.insert("text", triggerOutput.getValue());
        webApi.configure(webApiConfiguration);
        webApi.perform();
    }

    /**
     * Initialize options with default values.
     */
    @Override
    public void initializeClassOptions() {
    }
}
