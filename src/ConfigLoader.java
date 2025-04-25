import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {
    private static final Properties properties = new Properties();

    static {
        try (InputStream input = ConfigLoader.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                System.err.println("config.properties not found in resources.");
            } else {
                properties.load(input);
            }
        } catch (IOException ex) {
            System.err.println("Error loading config.properties: " + ex.getMessage());
        }
    }

    public static String get(String key) {
        return properties.getProperty(key);
    }
}
