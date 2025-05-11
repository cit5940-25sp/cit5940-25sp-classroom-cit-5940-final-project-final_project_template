import java.util.List;
import java.util.function.Function;

public class PlayerFactory {

    public static Player createPlayerWithGenre(String name, String genre) {
        return new Player(name, new GenreWinCondition(genre));
    }

    public static Player createPlayerWithActor(String name, String actor) {
        return new Player(name, new ActorWinCondition(actor));
    }

    public static Player createPlayerWithCustomCondition(String name, Function<List<Movie>, Boolean> condition, String description) {
        return new Player(name, new CustomWinCondition(condition, description));
    }
}
