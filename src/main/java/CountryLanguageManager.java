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

//    /**
//     * Calculates language rarity scores based on how many countries speak each language
//     */
//    public void calculateLanguageRarityScores() {
//        // Count occurrences of each language
//        Map<Language, Integer> languageCounts = new HashMap<>();
//
//        for (Country country : countries.values()) {
//            for (Language language : country.getLanguages()) {
//                languageCounts.put(language, languageCounts.getOrDefault(language, 0) + 1);
//            }
//        }
//
//        // Update rarity scores based on counts
//        // Formula: MAX(1, CEIL(10 / (count + 1)))
//        for (Map.Entry<Language, Integer> entry : languageCounts.entrySet()) {
//            Language language = languages.get(entry.getKey().getName().toLowerCase());
//            int count = entry.getValue();
//            int score = Math.max(1, (int) Math.ceil(10.0 / (count + 1)));
//
//            // Update language with new rarity score
//            languages.put(language.getName().toLowerCase(), new Language(language.getName(), score));
//        }
//    }
//
//    /**
//     * Initializes the service with sample data
//     */
//    public void initializeWithSampleData() {
//        // Add languages
//        addLanguage("English", 1);
//        addLanguage("Spanish", 2);
//        addLanguage("French", 3);
//        addLanguage("Portuguese", 2);
//        addLanguage("German", 4);
//        addLanguage("Italian", 5);
//        addLanguage("Dutch", 5);
//        addLanguage("Mandarin", 3);
//        addLanguage("Hindi", 5);
//        addLanguage("Arabic", 4);
//        addLanguage("Russian", 4);
//        addLanguage("Japanese", 5);
//        addLanguage("Bengali", 5);
//        addLanguage("Malay", 4);
//        addLanguage("Tamil", 4);
//        addLanguage("Swahili", 4);
//
//        // Add countries with their languages
//        addCountry("United States", "English");
//        addCountry("Canada", "English", "French");
//        addCountry("Mexico", "Spanish");
//        addCountry("Brazil", "Portuguese");
//        addCountry("United Kingdom", "English");
//        addCountry("France", "French");
//        addCountry("Spain", "Spanish");
//        addCountry("Portugal", "Portuguese");
//        addCountry("Germany", "German");
//        addCountry("Italy", "Italian");
//        addCountry("Netherlands", "Dutch");
//        addCountry("Belgium", "Dutch", "French", "German");
//        addCountry("Switzerland", "German", "French", "Italian");
//        addCountry("China", "Mandarin");
//        addCountry("Taiwan", "Mandarin");
//        addCountry("India", "Hindi", "English", "Bengali", "Tamil");
//        addCountry("Singapore", "English", "Mandarin", "Malay", "Tamil");
//        addCountry("Malaysia", "Malay", "English");
//        addCountry("Kenya", "Swahili", "English");
//        addCountry("Tanzania", "Swahili", "English");
//
//        // Calculate rarity scores
//        calculateLanguageRarityScores();
//    }

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

            // Add all languages
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

