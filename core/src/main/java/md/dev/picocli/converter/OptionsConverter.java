package md.dev.picocli.converter;

import md.dev.options.JsonToOptionsConverter;
import md.dev.options.Options;
import org.json.JSONObject;
import picocli.CommandLine;

public class OptionsConverter implements CommandLine.ITypeConverter<Options> {
    @Override
    public Options convert(String value) {
        return JsonToOptionsConverter.convert(new JSONObject(value));
    }
}
