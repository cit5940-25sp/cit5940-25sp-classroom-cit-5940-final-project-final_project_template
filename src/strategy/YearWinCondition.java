package strategy;

import model.Player;

/**
 * Win condition where a player wins by naming movies from a specific year.
 */
public class YearWinCondition implements IWinCondition {
    private int targetYear;

    /**
     * Constructs a YearWinCondition for the given year.
     * @param targetYear the target release year
     */
    public YearWinCondition(int targetYear) {
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
