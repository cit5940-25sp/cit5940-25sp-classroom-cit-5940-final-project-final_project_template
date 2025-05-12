import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;

public class RunGame {
    Player player1;
    Player player2;
    // timer (or does this go in controller?)
    int roundsPlayed;
    HashSet<String> moviesUsed;

    public static void main(String[] args) {
        String genre = promptForWinCondition();
        System.out.println("You selected: " + genre);
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
