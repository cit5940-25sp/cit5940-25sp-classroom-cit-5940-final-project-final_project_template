/**
 * Win condition where a player wins after guessing five horror movies.
 */
public class FiveHorrorMoviesWin implements WinCondition {

    @Override
    public boolean checkVictory(Player player){
        return true;
    }

    @Override
    public String description(){
        return "Five Horror Movies";
    }
}
