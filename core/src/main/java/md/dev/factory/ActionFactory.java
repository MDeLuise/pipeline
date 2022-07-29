package md.dev.factory;

import lombok.SneakyThrows;
import md.dev.action.Action;
import md.dev.webapi.WebApi;

import java.lang.reflect.Constructor;
import java.util.List;

public class ActionFactory extends EntityFactory<Action> {
    public ActionFactory(String prefix) {
        super(prefix);
    }

    @SneakyThrows
    @Override
    protected Action create(
            FactoryConfiguration factoryConfiguration,
            Constructor constructor,
            List<Object> defaultParams) {

        if (defaultParams.size() == 0) {
            return (Action) constructor.newInstance();
        }

        final WebApi WEB_API_TO_USE =
                factoryConfiguration.contains("webApi") ?
                        (WebApi) factoryConfiguration.get("webApi") :
                        (WebApi) defaultParams.get(0);

        return (Action) constructor.newInstance(WEB_API_TO_USE);
    }
}
