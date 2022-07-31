package plugin.base.trigger;

import md.dev.options.Options;
import md.dev.options.description.OptionDescription;
import md.dev.plugin.annotation.Trigger;
import md.dev.trigger.AbstractPeriodicWebTrigger;
import md.dev.trigger.output.TriggerOutput;
import md.dev.webapi.WebApi;
import md.dev.webapi.configuration.WebApiConfiguration;
import md.dev.webapi.configuration.WebApiResponse;
import plugin.base.webapi.JsoupSelectorApi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

@Trigger(
        id = "element",
        webApi = JsoupSelectorApi.class,
        outputType = String.class,
        description = "Send in the pipeline the element selected from an html page."
)
public class WebPageElementTrigger extends AbstractPeriodicWebTrigger<String> {
    private boolean triggerOnlyIfChanged;
    private String element;


    public WebPageElementTrigger(TriggerOutput<String> triggerOutputToUse, WebApi webApi) {
        super(triggerOutputToUse, webApi);
    }


    @Override
    public Collection<OptionDescription> acceptedClassOptions() {
        return new ArrayList<>(Arrays.asList(
                new OptionDescription(
                        "url",
                        "URL to check for changes.",
                        String.class,
                        "null",
                        true
                ),
                new OptionDescription(
                        "onChange",
                        "if true trigger only on element change.",
                        java.lang.Boolean.class,
                        "true",
                        false
                ),
                new OptionDescription(
                        "selector",
                        "value used to select the element in the page" +
                                " (<a href='https://jsoup.org/cookbook/extracting-data/selector" +
                                "'>syntax</a>).",
                        String.class,
                        "null",
                        false
                )
        ));
    }

    @Override
    public void loadState() {
        String savedElement = getProperty("element");
        if (savedElement != null) {
            element = savedElement;
        }
    }

    @Override
    public void saveState() {
        setProperty("element", element);
    }

    @Override
    protected void initializeClassOptions() {
        triggerOnlyIfChanged = true;
        element = "";
    }

    @Override
    protected void loadInstanceOptions(Options options) {
        if (options != null && options.has("onChange")) {
            triggerOnlyIfChanged = (boolean) options.get("onChange");
        }

        configureWebApi(options);
    }

    @Override
    protected void listen() {
        WebApiResponse webApiResponse = webApi.perform();

        if (webApiResponse != null && webApiResponse.isOk()) {
            String newElement = webApiResponse.getResponse();
            if (!element.equals(newElement) || !triggerOnlyIfChanged) {
                triggerOutput.setValue(newElement);
                triggerPipelines();
            }
            if (!element.equals(newElement)) {
                element = newElement;
                saveState();
            }
        }
    }

    protected void configureWebApi(Options options) {
        WebApiConfiguration webApiConfiguration = new WebApiConfiguration();
        webApiConfiguration.insert("url", options.get("url"));
        webApiConfiguration.insert("selector", options.get("selector"));
        webApi.configure(webApiConfiguration);
    }
}
