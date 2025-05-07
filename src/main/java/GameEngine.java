import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Main game engine that implements the game rules and logic
 */
public class GameEngine {
    // Using Singleton pattern for the game engine
    private static GameEngine instance;

    private final CountryLanguageManager dataService;
    private final List<IGameObserver> observers = new ArrayList<>();

    private GameState gameState;
    private boolean hardMode = false;
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

    public boolean setSelectedLanguage(Language language) {

        int languageLimit;
        if (!hardMode){
            languageLimit = 7;
        }   else {
            languageLimit = 4;
        }
        int currentUsage = gameState.getLanguageUsage().getOrDefault(language, 0);

        if (currentUsage >= languageLimit) {
            // Language has reached its limit
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

        return continueStreak(country, currentLang);
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
