package utils;

import java.io.IOException;
import java.util.Properties;

public class Props {
    private static Properties properties;
    public static final String SETTINGS = "/application.properties";

    static {
        properties = new Properties();
        try {
            properties.load(Props.class.getResourceAsStream(SETTINGS));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getValueFromProperties(String key) {
        return properties.getProperty(key);
    }
}
