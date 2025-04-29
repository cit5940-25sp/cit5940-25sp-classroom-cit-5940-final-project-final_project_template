package strategy;

import model.Player;

/**
 * Win condition where a player wins by naming a certain number of movies in a specific genre.
 */
public class GenreWinCondition implements IWinCondition {
    private String genre;

    /**
     * Constructs a GenreWinCondition for the given genre.
     * @param genre the target genre
     */
    public GenreWinCondition(String genre) {
        // TODO
    }

    @Override
    public boolean checkWin(Player player) {
        // TODO
        return false;
    }

    @Override
    public String getDescription() {
        // TODO
        return null;
    }
}
