import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Timer;

public class RunGame {

    Timer timer;

    public static void main(String[] args) {
        String player1Name = promptForUsername(1);
        String genre1 = promptForWinCondition();
        System.out.println(player1Name + " selected: " + genre1);

        String player2Name = promptForUsername(2);
        String genre2 = promptForWinCondition();
        System.out.println(player2Name + " selected: " + genre2);

        Game game = new Game("src/tmdb_data.txt", player1Name, player2Name, genre1, genre2);

        System.out.println("Starting movie: " + game.getCurrentMovie());

        while (true) {
            System.out.println("It's " + game.getWhosTurn() + "'s turn!");

            AutocompleteGUI.setSelectedMovie(null);
            AutocompleteGUI.main(new String[] {"src/autocomplete.txt", "5"});

            long start = System.currentTimeMillis();

            while (AutocompleteGUI.getSelectedMovie() == null &&
                    (System.currentTimeMillis() - start) < 30_000) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            String movie = AutocompleteGUI.getSelectedMovie();

            // ❌ Time ran out
            if (movie == null) {
                String loser = game.getWhosTurn();
                String winner = loser.equals(game.usernamePlayer1()) ? game.usernamePlayer2() : game.usernamePlayer1();
                System.out.println("⏰ Time's up! " + loser + " failed to make a move.");
                System.out.println("🎉 Game ended: " + winner + " has won!");
                break;
            }

            boolean worked = game.update(movie, game.getWhosTurn());

            if (worked) {
                System.out.println("✅ Valid move!");
                System.out.println(game.usernamePlayer1() + " progress: " + game.progressPlayer1() + "%");
                System.out.println(game.usernamePlayer2() + " progress: " + game.progressPlayer2() + "%");

                if (game.isGameOver()) {
                    System.out.println("🎉 Game ended: " + game.getWinner() + " has won!");
                    break;
                }
            } else {
                // ❌ Invalid move — opponent wins
                String loser = game.getWhosTurn();
                String winner = loser.equals(game.usernamePlayer1()) ? game.usernamePlayer2() : game.usernamePlayer1();
                System.out.println("❌ Invalid move by " + loser + " (duplicate or no connection)");
                System.out.println("🎉 Game ended: " + winner + " has won!");
                break;
            }
        }
    }

    public static String promptForWinCondition() {
        Scanner scanner = new Scanner(System.in);
        HashSet<String> validGenres = new HashSet<>(Arrays.asList("Action", "Comedy", "Horror", "Romance", "Drama"));

        System.out.println("Select a category for the win condition amongst these genres: Action, Comedy, Horror, Romance, Drama.");
        System.out.println("Player wins by naming five movies from that genre.");

        while (true) {
            System.out.print("Enter a genre: ");
            String input = scanner.nextLine().trim();

            for (String valid : validGenres) {
                if (valid.equalsIgnoreCase(input)) {
                    return valid;
                }
            }

            System.out.println("Invalid genre. Please choose one of: Action, Comedy, Horror, Romance, Drama.");
        }
    }

    public static String promptForUsername(int playerNumber) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter username for Player " + playerNumber + ": ");

        String username;
        while (true) {
            username = scanner.nextLine().trim();
            if (!username.isEmpty()) {
                return username;
            }
            System.out.print("Username cannot be empty. Please enter a valid name: ");
        }
    }

}
