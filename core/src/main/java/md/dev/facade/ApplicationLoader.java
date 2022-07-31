package md.dev.facade;

import md.dev.action.Action;
import md.dev.factory.ApplicationFactories;
import md.dev.factory.FactoryConfiguration;
import md.dev.factory.exception.NamedElementNotFoundException;
import md.dev.processor.filter.Filter;
import md.dev.processor.transformer.Transformer;
import md.dev.options.JsonToOptionsConverter;
import md.dev.options.Options;
import md.dev.options.OptionsImpl;
import md.dev.options.description.OptionsLoadableEntity;
import md.dev.pipeline.Pipeline;
import md.dev.pipeline.PipelineImpl;
import md.dev.trigger.Trigger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ApplicationLoader {
    private final PipelineImpl pipeline;
    private final List<Trigger<?>> triggers;


    public ApplicationLoader(JSONObject jsonObject, ApplicationFactories applicationFactories) {
        pipeline = new PipelineImpl();
        triggers = new ArrayList<>();
        createTriggers(jsonObject, applicationFactories);
        createProcessors(jsonObject, applicationFactories);
    }


    public Pipeline buildAndStart() {
        start();
        return build();
    }


    public Pipeline build() {
        return pipeline;
    }


    public void start() {
        triggers.forEach(Trigger::startListening);
    }


    private void createTriggers(JSONObject jsonObject, ApplicationFactories applicationFactories) {
        JSONArray jsonArrayTriggers = jsonObject.getJSONArray("triggers");
        jsonArrayTriggers.iterator().forEachRemaining(obj -> {
            JSONObject jsonObj = (JSONObject) obj;
            String triggerName = jsonObj.getString("name");
            FactoryConfiguration factoryConfiguration = new FactoryConfiguration();
            factoryConfiguration.put("name", triggerName);
            Trigger<?> trigger = applicationFactories
                .getTriggerFactories()
                .build(factoryConfiguration);

            loadOptionsIfExist(jsonObj, trigger);

            pipeline.addTrigger(trigger);
            triggers.add(trigger);
        });
    }


    private <T> void createProcessors(
        JSONObject jsonObject, ApplicationFactories applicationFactories) {
        JSONArray jsonArrayProcessor = jsonObject.getJSONArray("processors");
        jsonArrayProcessor.iterator().forEachRemaining(obj -> {
            JSONObject jsonObj = (JSONObject) obj;

            switch (jsonObj.getString("type")) {

                case "action" -> {
                    FactoryConfiguration factoryConfiguration = new FactoryConfiguration();
                    factoryConfiguration.put("name", jsonObj.getString("name"));
                    Action<?> action = applicationFactories
                        .getActionFactories()
                        .build(factoryConfiguration);
                    loadOptionsIfExist(jsonObj, action);
                    pipeline.addElement(action);
                }

                case "transformer" -> {
                    FactoryConfiguration factoryConfiguration = new FactoryConfiguration();
                    factoryConfiguration.put("name", jsonObj.getString("name"));
                    Transformer<T, ?> transformer = applicationFactories
                        .getTransformerFactories()
                        .build(factoryConfiguration);
                    loadOptionsIfExist(jsonObj, transformer);
                    pipeline.addElement(transformer);
                }

                case "filter" -> {
                    FactoryConfiguration factoryConfiguration = new FactoryConfiguration();
                    factoryConfiguration.put("name", jsonObj.getString("name"));
                    Filter<?> filter = applicationFactories
                        .getFilterFactories()
                        .build(factoryConfiguration);
                    loadOptionsIfExist(jsonObj, filter);
                    pipeline.addElement(filter);
                }

                case "filterOperator" -> {
                    JsonLoaderFilterOperator jsonLoaderFilterOperator =
                        new JsonLoaderFilterOperator();
                    pipeline.addElement(
                        jsonLoaderFilterOperator.buildFilterOperator(
                            jsonObj,
                            applicationFactories.getFilterFactories()
                        )
                    );
                }

                default -> throw new NamedElementNotFoundException(jsonObj.getString("type"));
            }
        });
    }


    private void loadOptionsIfExist(JSONObject jsonObj, OptionsLoadableEntity entity) {
        if (jsonObj.has("options")) {
            Options options = JsonToOptionsConverter.convert(
                jsonObj.getJSONObject("options")
            );
            entity.loadOptions(options);
        } else {
            entity.loadOptions(new OptionsImpl());
        }
    }

}
