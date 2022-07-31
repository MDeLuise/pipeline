package md.dev.trigger;

import md.dev.trigger.exception.NullWebApiException;
import md.dev.trigger.output.TriggerOutput;
import md.dev.webapi.WebApi;

public abstract class AbstractPeriodicWebTrigger<T> extends AbstractPeriodicTrigger<T> {
    protected final WebApi webApi;


    public AbstractPeriodicWebTrigger(TriggerOutput<T> triggerOutputToUse, WebApi webApi) {
        super(triggerOutputToUse);
        if (webApi == null) {
            throw new NullWebApiException();
        }
        this.webApi = webApi;
    }
}
