import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActorWinCondition implements IWinConditionStrategy {
    private String selectedActor;

    public ActorWinCondition(String selectedActor) {
        this.selectedActor = selectedActor;
    }

    @Override
    public boolean checkWin(List<IMovie> playedMovies) {
        Map<String, Integer>  actorCount = new HashMap<String, Integer>();
        for (IMovie movie : playedMovies) {
            List<String> actors = movie.getActors();
            for (String actor : actors) {
                if (!actorCount.containsKey(actor)) {
                    actorCount.put(actor, 1);
                } else {
                    actorCount.put(actor, actorCount.get(actor) + 1);
                }
                if (actorCount.get(actor) == 5) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String getDescription() {
        return "Play 5 movies of actor " + selectedActor + ".";
    }

    public String getSelectedActor() {
        return selectedActor;
    }
}
