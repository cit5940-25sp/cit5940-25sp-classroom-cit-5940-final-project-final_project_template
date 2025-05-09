import java.util.*;

public class GameState {
    private final List<GameMove> moves = new ArrayList<>();
    private final Set<Country> usedCountries = new HashSet<>();
    private Country currentCountry;
    private Language currentLanguage;
    private int currentStreak;
    private int totalScore;
    private final Map<Language, Integer> languageUsage = new HashMap<>();
    private final int MAX_MOVES = 30;
    private Integer testMaxMoves = null;


    public GameState(Country startingCountry) {
        this.currentCountry = startingCountry;
        this.currentLanguage = null;
        this.currentStreak = 0;
        this.totalScore = 0;

        // add starting country to used countries and move history
        usedCountries.add(startingCountry);
        moves.add(new GameMove(startingCountry, null, 0));
    }

    public Country getCurrentCountry() {
        return currentCountry;
    }

    public void setCurrentCountry(Country currentCountry) {
        this.currentCountry = currentCountry;
        usedCountries.add(currentCountry);
    }

    public Language getCurrentLanguage() {
        return currentLanguage;
    }

    public void setCurrentLanguage(Language currentLanguage) {
        this.currentLanguage = currentLanguage;
    }

    public int getCurrentStreak() {
        return currentStreak;
    }

    public void setCurrentStreak(int currentStreak) {
        this.currentStreak = currentStreak;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void addPoints(int points) {
        this.totalScore += points;
    }

    public List<GameMove> getMoves() {
        return Collections.unmodifiableList(moves);
    }

    public void addMove(GameMove move) {
        moves.add(move);
    }

    public boolean isCountryUsed(Country country) {
        return usedCountries.contains(country);
    }

    public Map<Language, Integer> getLanguageUsage() {
        return Collections.unmodifiableMap(languageUsage);
    }

    public void incrementLanguageUsage(Language lang) {
        languageUsage.put(lang, languageUsage.getOrDefault(lang, 0) + 1);
    }

    public int getRemainingMoves() {
        int maxToUse = testMaxMoves != null ? testMaxMoves : MAX_MOVES;
        return maxToUse - (moves.size() - 1);
        //subtracts 1 for the starting country
    }

    public boolean hasMovesRemaining() {
        return getRemainingMoves() > 0;
    }

    public int getMaxMoves() {
        return testMaxMoves != null ? testMaxMoves : MAX_MOVES;
    }

    /**
     * Set max moves for testing purposes
     * @param maxMoves Number of max moves to set
     */
    public void setMaxMovesForTest(int maxMoves) {
        this.testMaxMoves = maxMoves;
    }


}
