import com.opencsv.exceptions.CsvValidationException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;


public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        CountryLanguageManager dataService = new CountryLanguageManager();
        String resourcePath = "langSpoken.csv";
        ClassLoader loader = CountryLanguageManager.class.getClassLoader();
        URL data = loader.getResource(resourcePath);

        if (data == null) {
            throw new FileNotFoundException(
                    "Resource '" + resourcePath + "' not found.\n" +
                            "Checked with: " + loader + "\n" +
                            "Classpath: " + System.getProperty("java.class.path")
            );
        }

        try {
            dataService.initializeData(data);
        } catch (IOException | CsvValidationException e) {
            System.err.println("Failed to initialize data: " + e.getMessage());
            return;
        }

        GameEngine gameEngine = GameEngine.getInstance(dataService);

        GameConsoleUI ui = new GameConsoleUI(gameEngine); // start UI
        ui.start();
    }
}