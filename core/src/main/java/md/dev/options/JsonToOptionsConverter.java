package md.dev.options;

import org.json.JSONObject;

public class JsonToOptionsConverter {
    public static Options convert(JSONObject json) {
        Options options = new OptionsImpl();
        for (String key : json.keySet()) {
            options.add(key, json.get(key));
        }
        return options;
    }
}
