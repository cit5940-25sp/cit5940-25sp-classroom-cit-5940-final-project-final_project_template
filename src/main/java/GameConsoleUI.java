import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class GameConsoleUI implements IGameObserver {
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
        System.out.println("Rarer languages earn more points. " +
                                   "Maintain a streak for bonus points.\n");

        gameEngine.resetGame();

        boolean running = true;
        boolean waitingForCountry = false;
        Language selectedLanguage = null;

        while (running) {
            GameState state = gameEngine.getGameState();
            Country current = state.getCurrentCountry();

            if (!waitingForCountry) {
                System.out.println("Current Country: " + current.getName());
                Set<Language> langs = current.getLanguages();
                List<Language> langList = new ArrayList<>(langs);
                System.out.println("Languages spoken: ");
                for (int i = 0; i < langList.size(); i++) {
                    System.out.println("  " + (i + 1) + ": " + langList.get(i).getName());
                }

                System.out.print("Choose a language (1-" + langList.size() + "): ");
                try {
                    int choice = Integer.parseInt(scanner.nextLine().trim());
                    if (choice >= 1 && choice <= langList.size()) {
                        selectedLanguage = langList.get(choice - 1);
                        gameEngine.setSelectedLanguage(selectedLanguage);
                        waitingForCountry = true;
                    } else {
                        System.out.println("Invalid choice. Try again.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a number.");
                }
            } else {
                System.out.print("Enter a country that speaks " + selectedLanguage.getName() +
                                         " (or 'new' to restart, 'quit' to exit): ");
                String input = scanner.nextLine().trim();

                if (input.equalsIgnoreCase("quit")) {
                    running = false;
                } else if (input.equalsIgnoreCase("new")) {
                    gameEngine.resetGame();
                    selectedLanguage = null;
                    waitingForCountry = false;
                } else {
                    MoveResult result = gameEngine.moveToCountry(input);
                    System.out.println(result.getMessage());
                    if (result.isSuccess() ||
                            result.getMessage().contains("already used") && result.getMessage().contains("Please pick another language")) {
                        // clear selected language in two cases
                        // 1. move is successful
                        // 2. move is unsuccessful because language has been used up to the limit
                        selectedLanguage = null;
                        waitingForCountry = false;
                    }
                }
            }
        }

        System.out.println("Thanks for playing!");
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
