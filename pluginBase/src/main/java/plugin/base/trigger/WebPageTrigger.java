package plugin.base.trigger;

import md.dev.options.Options;
import md.dev.options.description.OptionDescription;
import md.dev.plugin.annotation.Trigger;
import md.dev.trigger.AbstractPeriodicWebTrigger;
import md.dev.trigger.output.TriggerOutput;
import md.dev.webapi.WebApi;
import md.dev.webapi.configuration.WebApiConfiguration;
import md.dev.webapi.configuration.WebApiResponse;
import org.apache.commons.codec.digest.DigestUtils;
import plugin.base.webapi.SimpleHttpGetWebApi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

@Trigger(
    id = "page",
    webApi = SimpleHttpGetWebApi.class,
    outputType = String.class,
    description = "Send hash of one web page in the pipeline."
)
public class WebPageTrigger extends AbstractPeriodicWebTrigger<String> {
    private boolean triggerOnlyIfChanged;
    private String pageHash;


    public WebPageTrigger(TriggerOutput<String> triggerOutputToUse, WebApi webApi) {
        super(triggerOutputToUse, webApi);
    }


    @Override
    public void loadState() {
        String savedHash = getProperty("pageHash");
        if (savedHash != null) {
            pageHash = savedHash;
        }
    }


    @Override
    public void saveState() {
        setProperty("pageHash", pageHash);
    }


    @Override
    protected void initializeClassOptions() {
        triggerOnlyIfChanged = true;
        pageHash = "";
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
                "if true trigger only on page change.",
                java.lang.Boolean.class,
                "true",
                false
            )
        ));
    }


    @Override
    protected void loadInstanceOptions(Options options) {
        if (options != null && options.has("onChange")) {
            triggerOnlyIfChanged = (boolean) options.get("onChange");
        }

        configureWebApi(options);
    }


    protected void configureWebApi(Options options) {
        WebApiConfiguration webApiConfiguration = new WebApiConfiguration();
        webApiConfiguration.insert("url", options.get("url"));
        webApi.configure(webApiConfiguration);
    }


    @Override
    protected void listen() {
        WebApiResponse webApiResponse = webApi.perform();

        if (webApiResponse != null && webApiResponse.isOk()) {
            String newPageHash = DigestUtils.md5Hex(webApiResponse.getResponse());
            if (!pageHash.equals(newPageHash) || !triggerOnlyIfChanged) {
                triggerOutput.setValue(newPageHash);
                triggerPipelines();
            }

            if (!pageHash.equals(newPageHash)) {
                pageHash = newPageHash;
                saveState();
            }
        }
    }
}
