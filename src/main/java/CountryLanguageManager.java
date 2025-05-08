import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;


public class CountryLanguageManager {
    private final Map<String, Country> countries = new HashMap<>();
    private final Map<String, Language> languages = new HashMap<>();

    public void addCountry(String name, String... languageNames) {
        Set<Language> countryLanguages = new HashSet<>();
        for (String langName : languageNames) {
            Language lang = languages.get(langName.toLowerCase());
            if (lang != null) {
                countryLanguages.add(lang);
            }
        }
        Country country = new Country(name, countryLanguages);
        countries.put(name.toLowerCase(), country);
    }

    public void addLanguage(String name, int rarityScore) {
        Language language = new Language(name, rarityScore);
        languages.put(name.toLowerCase(), language);
    }

    public Country getCountry(String name) {
        return countries.get(name.toLowerCase());
    }

    public Language getLanguage(String name) {
        return languages.get(name.toLowerCase());
    }

    public Collection<Country> getAllCountries() {
        return Collections.unmodifiableCollection(countries.values());
    }

    public Collection<Language> getAllLanguages() {
        return Collections.unmodifiableCollection(languages.values());
    }

    /**
     * Initializes data with csv file
     */
    public void initializeData(URL filePath) throws IOException, CsvValidationException {

        try (InputStream is = filePath.openStream(); InputStreamReader isr = new InputStreamReader(
                is); CSVReader reader = new CSVReader(isr)) {
            String[] headers = reader.readNext();

            int countryIndex = -1;
            int languagesIndex = -1;

            for (int i = 0; i < headers.length; i++) {
                if (headers[i].trim().equalsIgnoreCase("Country")) {
                    countryIndex = i;
                } else if (headers[i].trim().equalsIgnoreCase("Language")) {
                    languagesIndex = i;
                }
            }

            if (countryIndex == -1 || languagesIndex == -1) {
                throw new IllegalArgumentException(
                        "CSV must contain 'Country' and 'Language' columns");
            }

            Map<String, Integer> langFrequency = new HashMap<>();
            Map<String, Set<String>> countryLangMap = new HashMap<>();

            String[] line;
            while ((line = reader.readNext()) != null) {
                String country = line[countryIndex].trim();
                String[] langs = line[languagesIndex].split("[,;|]"); // covers different separators

                Set<String> uniqueLangs = new HashSet<>();

                for (String rawLang : langs) {
                    String lang = rawLang.trim().toLowerCase();
                    if (!lang.isEmpty()) {
                        uniqueLangs.add(lang);
                        langFrequency.put(lang, langFrequency.getOrDefault(lang, 0) + 1);
                    }
                }
                countryLangMap.put(country, uniqueLangs);
            }

            // Add all languages and calculate a rarity score based on its count
            for (Map.Entry<String, Integer> entry : langFrequency.entrySet()) {
                int count = entry.getValue();
                int score = Math.max(1, (int) Math.ceil(10.0 / (count + 1)));
                addLanguage(entry.getKey(), score);
            }

            // Add all countries
            for (Map.Entry<String, Set<String>> entry : countryLangMap.entrySet()) {
                String country = entry.getKey();
                String[] langs = entry.getValue().toArray(new String[0]);
                addCountry(country, langs);
            }
        }
    }
}

