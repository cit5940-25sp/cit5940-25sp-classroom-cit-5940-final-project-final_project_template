import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Timer;

public class RunGame {

    Timer timer;


    public static void main(String[] args) {
        // TODO: get player 1 to input username
        String player1Name = "";
        String genre1 = promptForWinCondition();
        System.out.println(player1Name + " selected: " + genre1);

        String player2Name = "";
        String genre2 = promptForWinCondition();
        System.out.println(player2Name + " selected: " + genre2);

        Game game = new Game("fileName", player1Name, player2Name, genre1, genre2);

        while (true) {
            System.out.println("It's" + game.getWhosTurn() + "'s turn!");

            // TODO: integrate autocomplete into this!!!!!!!
            String movie = "";
            boolean worked = game.update(movie, game.getWhosTurn());

            // TODO: figure out timer
            if (worked) {
                // TODO: make a nice display for these things
                System.out.println("Both users' na\n" +
                        "mes, win conditions, and progress\n" +
                        "History of most recent five movies played (including links between them, title,\n" +
                        "release year, and genres)\n" +
                        "Number of rounds played");
            } else {
                if (game.getWhosTurn().equals(game.usernamePlayer1())) {
                    System.out.println("Game ended: " + game.usernamePlayer2() + " has won!");
                } else {
                    System.out.println("Game ended: " + game.usernamePlayer1() + " has won!");
                }
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

}
