package strategy;

import model.Movie;
import model.Player;

/**
 * Win condition where a player wins by naming movies from a specific year.
 */
public class YearWinCondition implements IWinCondition {
    private int targetYear;
    private static final int WIN_COUNT = 3;

    /**
     * Constructs a YearWinCondition for the given year.
     * @param targetYear the target release year
     */
    public YearWinCondition(int targetYear) {
        this.targetYear = targetYear;
    }

    @Override
    public boolean checkWin(Player player) {
        int count = 0;
        for (Movie movie: player.getPlayedMovies()) {
            if (movie.getYear() == targetYear) {
                count++;
            }
            if (count >= WIN_COUNT) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getDescription() {
        return "Player wins by naming movies released in " + targetYear + " " + WIN_COUNT + " times.";
    }
}
