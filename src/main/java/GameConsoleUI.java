import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class GameConsoleUI extends GameObserver {
    private final GameEngine gameEngine;
    private final Scanner scanner;

    public GameConsoleUI(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
        this.scanner = new Scanner(System.in);
        gameEngine.addObserver(this);
    }

    public void start() {
        System.out.println("=== Language Connection Game ===");
        System.out.println("Connect countries through shared languages!");
        System.out.println("Rarer languages earn more points. Maintain a streak for bonus points.");
        System.out.println();

        gameEngine.resetGame();

        boolean running = true;
        while (running) {
            System.out.print("Enter a country name (or 'quit' to exit, 'new' for a new game): ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("quit")) {
                running = false;
            } else if (input.equalsIgnoreCase("new")) {
                gameEngine.resetGame();
            } else {
                MoveResult result = gameEngine.makeMove(input);
                System.out.println(result.getMessage());

                if (result.requiresLanguageSelection()) {
                    handleLanguageSelection(result.getLanguageOptions());
                }
            }
        }

        System.out.println("Thanks for playing!");
    }

    private void handleLanguageSelection(Set<Language> options) {
        System.out.println("Available languages:");
        List<Language> languages = new ArrayList<>(options);
        for (int i = 0; i < languages.size(); i++) {
            Language lang = languages.get(i);
            System.out.println(
                    (i + 1) + ": " + lang.getName() + " (" + lang.getRarityScore() + " points)");
        }

        System.out.print("Select a language (1-" + languages.size() + "): ");
        try {
            int selection = Integer.parseInt(scanner.nextLine().trim());
            if (selection >= 1 && selection <= languages.size()) {
                Language selected = languages.get(selection - 1);
                MoveResult result = gameEngine.selectLanguage(selected);
                System.out.println(result.getMessage());
            } else {
                System.out.println("Invalid selection. Please try again.");
                handleLanguageSelection(options);
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
            handleLanguageSelection(options);
        }
    }

    @Override
    public void onGameStateChanged(GameState gameState) {
        System.out.println("\n--- Game State ---");
        System.out.println("Current Country: " + gameState.getCurrentCountry().getName());
        System.out.println("Available Languages: " +
                                   gameState.getCurrentCountry().getLanguages());

        if (gameState.getCurrentLanguage() != null) {
            System.out.println("Current Language: " + gameState.getCurrentLanguage().getName() +
                                       " (Streak: " + gameState.getCurrentStreak() + ")");
        }

        System.out.println("Score: " + gameState.getTotalScore());

        if (gameState.getMoves().size() > 1) {
            System.out.println("\nMove History:");
            for (int i = 1; i < gameState.getMoves().size(); i++) {
                System.out.println("  " + gameState.getMoves().get(i));
            }
        }

        System.out.println("------------------\n");
    }
}
