package md.dev.plugin;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class PropertyFileReader {
    private final Properties PROPERTY;

    public PropertyFileReader(File file) {
        PROPERTY = new Properties();
        try {
            PROPERTY.load(this.getClass().getClassLoader().getResourceAsStream(file.getName()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String get(String key) {
        return PROPERTY.getProperty(key);
    }
}
