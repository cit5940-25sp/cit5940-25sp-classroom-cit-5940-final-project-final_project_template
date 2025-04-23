import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Main game engine that implements the game rules and logic
 */
public class GameEngine {
    // Using Singleton pattern for the game engine
    private static GameEngine instance;

    private final CountryLanguageManager dataService;
    private final List<GameObserver> observers = new ArrayList<>();

    private GameState gameState;

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
     * Resets the game with a random starting country
     */
    public void resetGame() {
        List<Country> allCountries = new ArrayList<>(dataService.getAllCountries());
        Random random = new Random();
        Country startingCountry = allCountries.get(random.nextInt(allCountries.size()));

        gameState = new GameState(startingCountry);
        notifyObservers();
    }

    /**
     * Processes a player's move
     *
     * @param countryName The name of the country the player wants to move to
     * @return Result of the move attempt
     */
    public MoveResult makeMove(String countryName) {
        Country country = dataService.getCountry(countryName.toLowerCase());

        // Check if country exists
        if (country == null) {
            return new MoveResult(false, "Country not found: " + countryName);
        }

        // Check if country was already used
        if (gameState.isCountryUsed(country)) {
            return new MoveResult(false, "Country already used: " + countryName);
        }

        Country currentCountry = gameState.getCurrentCountry();
        Set<Language> sharedLanguages = currentCountry.getSharedLanguages(country);

        // Check if countries share any languages
        if (sharedLanguages.isEmpty()) {
            return new MoveResult(false, country.getName() + " doesn't share any languages with " +
                    currentCountry.getName());
        }

        // If this is not the first move and a language streak exists
        if (gameState.getCurrentLanguage() != null) {
            // Check if the new country has the current streak language
            if (!country.hasLanguage(gameState.getCurrentLanguage())) {
                // Multiple shared languages available - requires user to choose
                if (sharedLanguages.size() > 1) {
                    gameState.setPendingCountry(country);
                    gameState.setAvailableLanguages(sharedLanguages);
                    notifyObservers();
                    return new MoveResult(false, "Please choose a language to continue with",
                                          sharedLanguages);
                } else {
                    // Only one shared language - automatically switch to it
                    Language newLanguage = sharedLanguages.iterator().next();
                    return selectLanguage(newLanguage);
                }
            } else {
                // Continue the streak with same language
                return continueStreak(country, gameState.getCurrentLanguage());
            }
        } else {
            // First move after starting country
            if (sharedLanguages.size() > 1) {
                // Multiple shared languages - requires user to choose
                gameState.setPendingCountry(country);
                gameState.setAvailableLanguages(sharedLanguages);
                notifyObservers();
                return new MoveResult(false, "Please choose a language to continue with",
                                      sharedLanguages);
            } else {
                // Only one shared language - automatically select it
                Language language = sharedLanguages.iterator().next();
                return selectLanguage(language);
            }
        }
    }

    /**
     * Selects a language when multiple options are available
     */
    public MoveResult selectLanguage(Language language) {
        if (gameState.getPendingCountry() == null) {
            return new MoveResult(false, "No pending move to apply language selection to");
        }

        // Check if the selected language is valid for the pending move
        if (!gameState.getAvailableLanguages().contains(language)) {
            return new MoveResult(false, "Selected language is not valid for this move");
        }

        // Check if it's the same as current streak language
        if (language.equals(gameState.getCurrentLanguage())) {
            return continueStreak(gameState.getPendingCountry(), language);
        } else {
            // Reset streak with new language
            int points = language.getRarityScore();
            GameMove move = new GameMove(gameState.getPendingCountry(), language, points);

            gameState.setCurrentCountry(gameState.getPendingCountry());
            gameState.setCurrentLanguage(language);
            gameState.setCurrentStreak(1);
            gameState.addPoints(points);
            gameState.addMove(move);
            gameState.clearPendingMove();

            notifyObservers();
            return new MoveResult(true, "Language changed to " + language.getName() +
                    ". Streak reset. +" + points + " points", move);
        }
    }

    /**
     * Continues the current language streak
     */
    private MoveResult continueStreak(Country country, Language language) {
        int newStreak = gameState.getCurrentStreak() + 1;
        int points = language.getRarityScore() * newStreak;
        GameMove move = new GameMove(country, language, points);

        gameState.setCurrentCountry(country);
        gameState.setCurrentStreak(newStreak);
        gameState.addPoints(points);
        gameState.addMove(move);
        gameState.clearPendingMove();

        notifyObservers();
        return new MoveResult(true, "Streak continued with " + language.getName() +
                ". +" + points + " points", move);
    }

    /**
     * Adds an observer to receive game state updates
     */
    public void addObserver(GameObserver observer) {
        observers.add(observer);
    }

    /**
     * Removes an observer
     */
    public void removeObserver(GameObserver observer) {
        observers.remove(observer);
    }

    /**
     * Notifies all observers of game state changes
     */
    private void notifyObservers() {
        for (GameObserver observer : observers) {
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
