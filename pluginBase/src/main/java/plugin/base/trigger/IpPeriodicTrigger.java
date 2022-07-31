package plugin.base.trigger;

import md.dev.options.Options;
import md.dev.options.description.OptionDescription;
import md.dev.plugin.annotation.Trigger;
import md.dev.trigger.AbstractPeriodicWebTrigger;
import md.dev.trigger.output.TriggerOutput;
import md.dev.webapi.WebApi;
import md.dev.webapi.configuration.WebApiResponse;
import plugin.base.webapi.IpWebApi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

@Trigger(
    id = "ip",
    webApi = IpWebApi.class,
    outputType = String.class,
    description = "Send the current IP in the pipeline."
)
public class IpPeriodicTrigger extends AbstractPeriodicWebTrigger<String> {
    private String ip;
    private boolean triggerOnlyIfChanged;


    public IpPeriodicTrigger(TriggerOutput<String> triggerOutputToUse, WebApi webApi) {
        super(triggerOutputToUse, webApi);
    }


    @Override
    public void loadState() {
        String savedIp = getProperty("ip");
        if (savedIp != null) {
            ip = getProperty("ip");
        }
    }


    @Override
    public void saveState() {
        setProperty("ip", ip);
    }


    @Override
    protected void initializeClassOptions() {
        triggerOnlyIfChanged = true;
        ip = "";
    }


    @Override
    public Collection<OptionDescription> acceptedClassOptions() {
        return new ArrayList<>(Arrays.asList(
            new OptionDescription(
                "onChange",
                "if true trigger only on ip change.",
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
    }


    @Override
    protected void listen() {
        WebApiResponse webApiResponse = webApi.perform();

        if (webApiResponse != null && webApiResponse.isOk()) {
            String newIp = webApiResponse.getResponse();
            if (!ip.equals(newIp) || !triggerOnlyIfChanged) {
                triggerOutput.setValue(newIp);
                triggerPipelines();
                ip = newIp;
                saveState();
            }
        }
    }
}
