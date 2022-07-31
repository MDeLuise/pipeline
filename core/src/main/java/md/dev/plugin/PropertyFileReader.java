package md.dev.plugin;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class PropertyFileReader {
    private final Properties property;


    public PropertyFileReader(File file) {
        property = new Properties();
        try {
            property.load(this.getClass().getClassLoader().getResourceAsStream(file.getName()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public String get(String key) {
        return property.getProperty(key);
    }
}
