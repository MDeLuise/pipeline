package md.dev.trigger;

import md.dev.trigger.exception.NullWebApiException;
import md.dev.trigger.output.TriggerOutput;
import md.dev.webapi.WebApi;

public abstract class AbstractOneTimeWebTrigger<T> extends AbstractOneTimeTrigger<T> {
    protected final WebApi WEB_API;

    public AbstractOneTimeWebTrigger(TriggerOutput<T> triggerOutputToUse, WebApi webApi) {
        super(triggerOutputToUse);
        if (webApi == null) {
            throw new NullWebApiException();
        }
        WEB_API = webApi;
    }
}
