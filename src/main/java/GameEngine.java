import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Main game engine that implements the game rules and logic
 */
public class GameEngine {
    // using Singleton pattern for the game engine
    private static GameEngine instance;

    private final CountryLanguageManager dataService;
    private final List<IGameObserver> observers = new ArrayList<>();

    private GameState gameState;
    private boolean hardMode = false;
    private Random random = new Random(); // Add this line

    private GameEngine(CountryLanguageManager dataService) {
        this.dataService = dataService;
        resetGame();
    }

    public static synchronized GameEngine getInstance(CountryLanguageManager dataService) {
        if (instance == null) {
            instance = new GameEngine(dataService);
        }
        return instance;
    }



    /**
     * reset the game with a random starting country
     * @param random
     */
    public void resetGame(Random random) {
        List<Country> allCountries = new ArrayList<>(dataService.getAllCountries());
        Country startingCountry = allCountries.get(random.nextInt(allCountries.size()));
        gameState = new GameState(startingCountry);
        notifyObservers();
    }

    public void resetGame() {
        Random random = new Random();
        resetGame(random);
    }

    public void setHardMode(boolean hardMode) {

        this.hardMode = hardMode;
    }

    public boolean isHardMode() {
        return hardMode;
    }

    /**
     * Get a new random country that hasn't been used yet
     */
    public void refreshCountry() {
        List<Country> availableCountries = new ArrayList<>();
        for (Country country : dataService.getAllCountries()) {
            if (!gameState.isCountryUsed(country) && hasViableLanguages(country)) {
                availableCountries.add(country);
            }
        }

        if (availableCountries.isEmpty()) {
            // If all countries have been used or have no viable languages, game is complete
            System.out.println("Game complete! No more viable countries available.");
            return;
        }

        // Select a random country from available ones
        Country newCountry = availableCountries.get(random.nextInt(availableCountries.size()));
        gameState.setCurrentCountry(newCountry);
        gameState.setCurrentLanguage(null);
        gameState.setCurrentStreak(0);

        System.out.println("Country refreshed to: " + newCountry.getName());
        notifyObservers();
    }

    /**
     * Checks if a country has any viable languages, importantly, spoken by multiple countries
     */
    public boolean hasViableLanguages(Country country) {
        for (Language language : country.getLanguages()) {
            if (isViableLanguage(language) && !isLanguageLimitReached(language)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if a language is viable, meaning spoken by multiple countries that haven't been used
     */
    public boolean isViableLanguage(Language language) {
        int availableCountries = 0;

        for (Country country : dataService.getAllCountries()) {
            if (!gameState.isCountryUsed(country) && country.hasLanguage(language)) {
                availableCountries++;
                if (availableCountries > 0) {  // doesn't work if no countries remaining that haven't been used
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Checks if a language has reached its usage limit
     */
    public boolean isLanguageLimitReached(Language language) {
        int languageLimit = hardMode ? 4 : 7;
        int currentUsage = gameState.getLanguageUsage().getOrDefault(language, 0);
        return currentUsage >= languageLimit;
    }


    public boolean setSelectedLanguage(Language language) {

        int languageLimit;
        if (!hardMode){
            languageLimit = 7;
        }   else {
            languageLimit = 4;
        }
        int currentUsage = gameState.getLanguageUsage().getOrDefault(language, 0);

        //checking language limits accounting for hard or regular mode
        if (currentUsage >= languageLimit) {
            // message when language has reached its limit
            System.out.println("You've already used " + language.getName() + " " +
                    languageLimit + " times. Please pick another language.");
            return false;
        }

        if (gameState.getCurrentLanguage() != language) {
            // begin new streak if user selects a new language
            gameState.setCurrentStreak(0);
        }
        gameState.setCurrentLanguage(language);
        notifyObservers();
        return true;
    }

    public MoveResult moveToCountry(String countryName) {
        Country country = dataService.getCountry(countryName.toLowerCase());

        if (country == null) {
            return new MoveResult(false, "Country not found: " + countryName);
        }

        if (gameState.isCountryUsed(country)) {
            return new MoveResult(false, "Country already used: " + countryName);
        }

        Language currentLang = gameState.getCurrentLanguage();
        if (currentLang == null) {
            return new MoveResult(false, "No language selected. Please choose a language first.");
        }

        if (!country.hasLanguage(currentLang)) {
            return new MoveResult(false, country.getName() + " does not speak " + currentLang.getName());
        }

        MoveResult result = continueStreak(country, currentLang);

        // After a successful move, check if there are any remaining valid moves for this language
        if (result.isSuccess()) {
            boolean hasRemainingMoves = false;
            for (Country otherCountry : dataService.getAllCountries()) {
                if (!gameState.isCountryUsed(otherCountry) && otherCountry.hasLanguage(currentLang)) {
                    hasRemainingMoves = true;
                    break;
                }
            }

            // If no remaining moves with this language, check if current country has any viable languages
            if (!hasRemainingMoves) {
                result = new MoveResult(true, result.getMessage() +
                        "\nNo more countries available with " + currentLang.getName() + ".", result.getMove());

                // Also check if the current country has any viable languages left
                if (!hasViableLanguages(gameState.getCurrentCountry())) {
                    result = new MoveResult(true, result.getMessage() +
                            "\nNo viable languages left for " + gameState.getCurrentCountry().getName() +
                            ". Refreshing to a new country.", result.getMove());
                    refreshCountry();
                }
            }
        }

        return result;
    }

    /**
     * Continues the current language streak
     */
    private MoveResult continueStreak(Country country, Language language) {
        int newStreak = gameState.getCurrentStreak() + 1;
        int points = language.getRarityScore() * newStreak;
        GameMove move = new GameMove(country, language, points);

        gameState.incrementLanguageUsage(language);
        gameState.setCurrentCountry(country);
        gameState.setCurrentStreak(newStreak);
        gameState.addPoints(points);
        gameState.addMove(move);

        notifyObservers();
        return new MoveResult(true, "Streak continued with " + language.getName() +
                ". +" + points + " points", move);
    }

    /**
     * Adds an observer to receive game state updates
     */
    public void addObserver(IGameObserver observer) {
        observers.add(observer);
    }

    /**
     * Notifies all observers of game state changes
     */
    private void notifyObservers() {
        for (IGameObserver observer : observers) {
            observer.onGameStateChanged(gameState);
        }
    }

    /**
     * Returns the current game state
     */
    public GameState getGameState() {
        return gameState;
    }
}
