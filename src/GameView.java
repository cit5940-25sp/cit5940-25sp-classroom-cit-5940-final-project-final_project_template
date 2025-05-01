import java.util.List;

/**
 * Handles rendering game state and displaying information to the user.
 */
public class GameView {

    /**
     * Renders the current state of the game.
     *
     * @param state the current game state
     */
    public void render(GameState state) {
        System.out.println("--- Game State ---");
        System.out.println("Current Round: " + state.getCurrRound());
        System.out.println("Current Player: " + state.getCurrentPlayer().getName());
        System.out.println("Recent History:");
        for (Movie movie : state.getRecentHistory()) {
            System.out.println(" - " + movie.getTitle() + " (" + movie.getYear() + ")");
        }
        System.out.println();
    }

    /**
     * Displays informational messages to the user.
     *
     * @param message the message to display
     */
    public void displayInfo(String message) {
        System.out.println("[INFO] " + message);
    }

    /**
     * Displays autocomplete suggestions to assist movie title entry.
     *
     * @param suggestions list of suggested movie titles
     */
    public void showAutocomplete(List<String> suggestions) {
        System.out.println("Did you mean:");
        for (String suggestion : suggestions) {
            System.out.println(" - " + suggestion);
        }
    }
}

