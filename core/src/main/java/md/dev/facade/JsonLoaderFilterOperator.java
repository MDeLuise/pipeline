package md.dev.facade;

import md.dev.factory.EntityFactorySet;
import md.dev.factory.FactoryConfiguration;
import md.dev.factory.exception.NamedElementNotFoundException;
import md.dev.processor.filter.Filter;
import md.dev.processor.filter.operator.AndFilterOperator;
import md.dev.processor.filter.operator.FilterOperator;
import md.dev.processor.filter.operator.LeafFilterOperator;
import md.dev.processor.filter.operator.NotFilterOperator;
import md.dev.processor.filter.operator.OrFilterOperator;
import md.dev.options.JsonToOptionsConverter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Iterator;

class JsonLoaderFilterOperator {

    public <T> FilterOperator<T> buildFilterOperator(
            JSONObject jsonObj,
            EntityFactorySet<Filter> filterFactory) {
        final String NAME = jsonObj.getString("name");
        final String TYPE = jsonObj.getString("type");
        if ("filterOperator".equals(TYPE)) {
            return switch (NAME) {
                case "and" -> createAndFilterRecursive(jsonObj, filterFactory);
                case "or" -> createOrFilterRecursive(jsonObj, filterFactory);
                case "not" -> getNotFilterOperator(jsonObj, filterFactory);
                default -> throw new NamedElementNotFoundException(NAME);
            };
        } else {
            return getLeafFilter(jsonObj, filterFactory);
        }
    }

    private <T> FilterOperator<T> getNotFilterOperator(
            JSONObject jsonObj, EntityFactorySet<Filter> filterFactory) {
        JSONArray jsonArray = jsonObj.getJSONArray("filters");
        NotFilterOperator<T> notFilterOperator = new NotFilterOperator<>();
        notFilterOperator.insertOperator(buildFilterOperator(
                jsonArray.getJSONObject(0),
                filterFactory
        ));
        return notFilterOperator;
    }

    private <T> FilterOperator<T> createAndFilterRecursive(
            JSONObject jsonObj,
            EntityFactorySet<Filter> filterFactory) {
        JSONArray jsonArray = jsonObj.getJSONArray("filters");
        FilterOperator<T> andFilterOperator = new AndFilterOperator<>();
        Iterator<Object> objectIterator = jsonArray.iterator();
        while (objectIterator.hasNext()) {
            JSONObject insideJsonObj = (JSONObject) objectIterator.next();
            andFilterOperator.insertOperator(
                    buildFilterOperator(insideJsonObj, filterFactory)
            );
        }
        return andFilterOperator;
    }

    private <T> FilterOperator<T> createOrFilterRecursive(
            JSONObject jsonObj,
            EntityFactorySet<Filter> filterFactory) {
        JSONArray jsonArray = jsonObj.getJSONArray("filters");
        FilterOperator<T> orFilterOperator = new OrFilterOperator<>();
        Iterator<Object> objectIterator = jsonArray.iterator();
        while (objectIterator.hasNext()) {
            JSONObject insideJsonObj = (JSONObject) objectIterator.next();
            orFilterOperator.insertOperator(
                    buildFilterOperator(insideJsonObj, filterFactory)
            );
        }
        return orFilterOperator;
    }


    private <T> FilterOperator<T> getLeafFilter(
            JSONObject jsonObj, EntityFactorySet<Filter> filterFactory) {
        FactoryConfiguration factoryConfiguration = new FactoryConfiguration();
        factoryConfiguration.put("name", jsonObj.getString("name"));
        Filter<T> filter = filterFactory.build(factoryConfiguration);
        if (jsonObj.has("options")) {
            filter.loadOptions(JsonToOptionsConverter.convert(
                    (JSONObject) jsonObj.get("options")
            ));
        }
        LeafFilterOperator<T> leafFilterOperator = new LeafFilterOperator<>();
        leafFilterOperator.insertFilter(filter);
        return leafFilterOperator;
    }
}
