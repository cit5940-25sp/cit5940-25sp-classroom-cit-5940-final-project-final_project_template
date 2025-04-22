/**
 * Win condition where a player wins after guessing five horror movies.
 */
public class FiveHorrorMoviesWin implements WinCondition {

    @Override
    public boolean checkVictory(Player player);

    @Override
    public String description();
}
