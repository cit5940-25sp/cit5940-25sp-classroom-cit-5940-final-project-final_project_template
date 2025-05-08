import java.util.Scanner;
import java.util.List;

public class GameTUI {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String apiKey = ConfigLoader.get("tmdb.api.key");
        GameController controller = new GameController(apiKey);

        System.out.println("============================");
        System.out.println("Welcome to Movie Game!");
        System.out.println("============================");

        System.out.print("Enter Player 1 name: ");
        String p1 = scanner.nextLine().trim();
        System.out.print("Enter Player 2 name: ");
        String p2 = scanner.nextLine().trim();

        controller.getMovieDatabase().preloadPopularMovies();
        controller.startGame(p1, p2, new TwoHorrorMoviesWin());
        if (controller.getGameState() == null) {
            System.out.println("Failed to start game. Exiting.");
            return;
        }
        while (true) {
            long roundStart = System.currentTimeMillis();

            System.out.println("\nEnter a movie title (or type 'exit' to quit):");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("exit")) {
                System.out.println("Thanks for playing!");
                break;
            }

            // Autocomplete suggestions
            List<String> suggestions = controller.getAutocompleteSuggestions(input);
            if (!suggestions.isEmpty()) {
                controller.getView().showAutocomplete(suggestions);
            }

            long timeUsed = (System.currentTimeMillis() - roundStart) / 1000;
            controller.processTurn(input);
        }
    }
}



