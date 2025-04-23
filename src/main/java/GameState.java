import java.util.*;

public class GameState {
    private final List<GameMove> moves = new ArrayList<>();
    private final Set<Country> usedCountries = new HashSet<>();
    private Country currentCountry;
    private Language currentLanguage;
    private int currentStreak;
    private int totalScore;
    // For pending moves that require language selection
    private Country pendingCountry;
    private Set<Language> availableLanguages = new HashSet<>();

    public GameState(Country startingCountry) {
        this.currentCountry = startingCountry;
        this.currentLanguage = null;
        this.currentStreak = 0;
        this.totalScore = 0;

        // Add starting country to used countries and move history
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

    public Country getPendingCountry() {
        return pendingCountry;
    }

    public void setPendingCountry(Country pendingCountry) {
        this.pendingCountry = pendingCountry;
    }

    public Set<Language> getAvailableLanguages() {
        return Collections.unmodifiableSet(availableLanguages);
    }

    public void setAvailableLanguages(Set<Language> availableLanguages) {
        this.availableLanguages = new HashSet<>(availableLanguages);
    }

    public void clearPendingMove() {
        this.pendingCountry = null;
        this.availableLanguages.clear();
    }
}
