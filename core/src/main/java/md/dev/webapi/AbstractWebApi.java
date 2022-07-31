package md.dev.webapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractWebApi implements WebApi {
    protected final Logger log = LoggerFactory.getLogger(AbstractWebApi.class);
}
