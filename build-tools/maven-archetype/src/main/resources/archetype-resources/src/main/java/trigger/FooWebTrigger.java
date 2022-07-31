package ${package}.trigger;

import md.dev.options.Options;
import md.dev.options.description.OptionDescription;
import md.dev.plugin.annotation.Trigger;

import md.dev.trigger.AbstractPeriodicWebTrigger;
import md.dev.trigger.output.TriggerOutput;
import ${package}.webapi.FooWebApi0;
import md.dev.webapi.WebApi;
import md.dev.webapi.configuration.WebApiConfiguration;
import md.dev.webapi.configuration.WebApiResponse;

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
 * This is an example of Trigger.
 * "extends AbstractPeriodicWebTrigger<T>" means that this trigger:
 *      - it's web trigger (i.e. does use WebApi object to perform web requests),
 *      - it's periodic (i.e. it's called periodically according to the
 *          "period" and "repeat" options passed),
 *      - send a value of type T in the pipeline.
 */
@Trigger(
    id = "triFoo2",
    webApi = FooWebApi0.class,
    outputType = String.class,
    description = "..."
)
public class FooWebTrigger extends AbstractPeriodicWebTrigger<String> {
    private String url;


    public FooWebTrigger(TriggerOutput<String> triggerOutputToUse, WebApi webApi) {
        super(triggerOutputToUse, webApi);
    }


    /**
     * Initialize options with default values.
     */
    @Override
    protected void initializeClassOptions() {
    }


    /**
     * List which options are loaded by the trigger.
     *
     * @return collection of used options.
     */
    @Override
    public Collection<OptionDescription> acceptedClassOptions() {
        return new ArrayList<>(Arrays.asList(
            new OptionDescription(
                "url", // name of the option
                "URL to check for changes.", // description of the option
                String.class, // type of the option
                "false", // default value of the option
                true // is option mandatory?
            )
        ));
    }


    /**
     * Load used options.
     *
     * @param options: options to load.
     */
    @Override
    protected void loadInstanceOptions(Options options) {
        if (options.has("url")) {
            url = (String) options.get("url");
        }
    }


    /**
     * Perform the purpose of the trigger. Notice that:
     * - the value sent to the pipeline must be inserted in the triggerOutput object
     * (don't create a new one),
     * - after that the triggerPipelines() must be called.
     */
    @Override
    protected void listen() {
        WebApiConfiguration webApiConfiguration = new WebApiConfiguration();
        webApiConfiguration.insert("url", url);
        WEB_API.configure(webApiConfiguration);
        WebApiResponse webApiResponse = WEB_API.perform();

        if (webApiResponse != null && webApiResponse.isOk()) {
            triggerOutput.setValue(webApiResponse.getResponse());
            triggerPipelines();
        }
        // call saveState() if you want to save the state of the trigger
    }


    /**
     * Used in order to save the state of the trigger.
     */
    @Override
    public void saveState() {
        setProperty("url", url);
    }


    /**
     * Used in order to load the previous state of the trigger.
     */
    @Override
    public void loadState() {
        String savedUrl = getProperty("url");
        if (savedUrl != null) {
            url = getProperty("url");
        }
    }


    private void configureWebApi(Options options) {
        WebApiConfiguration webApiConfiguration = new WebApiConfiguration();
        webApiConfiguration.insert("url", url);
        WEB_API.configure(webApiConfiguration);
    }
}
