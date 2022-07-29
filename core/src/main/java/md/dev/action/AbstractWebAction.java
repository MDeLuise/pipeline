package md.dev.action;

import md.dev.webapi.WebApi;

public abstract class AbstractWebAction<T> extends AbstractAction<T> {
    protected WebApi webApi;

    public AbstractWebAction(WebApi webApi) {
        this.webApi = webApi;
    }
}
