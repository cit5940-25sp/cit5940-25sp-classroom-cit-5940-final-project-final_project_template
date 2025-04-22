import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Player {
    // === Attributes ===
    private final String name;
    private final String winGenre; // The genre the player is trying to collect
    private final int winThreshold; // How many movies of that genre are required to win

    private final Set<Integer> usedMovies; // Prevents movie reuse
    private final Map<String, Integer> genreCount; // Tracks how many times a genre has been played

    private boolean hasBlocked; // Tracks whether block power-up was used
    private boolean isSkipped;  // Flag to indicate if player loses next turn

    // === Constructor ===
    public Player(String name, String winGenre, int winThreshold) {
        this.name = name;
        this.winGenre = winGenre.toLowerCase();
        this.winThreshold = winThreshold;
        this.usedMovies = new HashSet<>();
        this.genreCount = new HashMap<>();
        this.hasBlocked = false;
        this.isSkipped = false;
    }

    // === Getters ===
    public String getName() {
        return name;
    }

    /**
     * Checks if the player has already used a movie with the given ID.
     */
    public boolean hasUsedMovie(int movieId) {
        return usedMovies.contains(movieId);
    }

    public boolean isSkipped() {
        return isSkipped;
    }

    public boolean hasBlocked() {
        return hasBlocked;
    }

    // === Functional Methods ===

    /**
     * Adds a movie to the player's history and updates genre count.
     * @param movie The movie to add
     */
    public void addMovie(Movie movie) {
        Integer id = movie.getId();
        if (usedMovies.contains(id)) return;

        usedMovies.add(id);
        for (String genre : movie.getGenre()) {
            String lowerGenre = genre.toLowerCase();
            genreCount.put(lowerGenre, genreCount.getOrDefault(lowerGenre, 0) + 1);
        }
    }

    /**
     * Checks if the player has met their win condition.
     */
    public boolean hasMetWinCondition() {
        return genreCount.getOrDefault(winGenre, 0) >= winThreshold;
    }

    /**
     * Activate a skip (used by opponent power-up).
     */
    public void activateSkip() {
        isSkipped = true;
    }

    /**
     * Consume the skip and allow turn again.
     */
    public void clearSkip() {
        isSkipped = false;
    }

    /**
     * Activates block usage flag (only once allowed, depending on game logic).
     */
    public void useBlock() {
        hasBlocked = true;
    }

    @Override
    public String toString() {
        return name + " | Genre Goal: " + winGenre + " (" +
                genreCount.getOrDefault(winGenre, 0) + "/" + winThreshold + ")";
    }
}

