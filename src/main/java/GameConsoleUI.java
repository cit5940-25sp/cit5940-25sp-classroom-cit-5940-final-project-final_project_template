import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class GameConsoleUI implements IGameObserver {
    private final GameEngine gameEngine;
    private final Scanner scanner;
    private final CountryLanguageManager dataService;

    public GameConsoleUI(GameEngine gameEngine, CountryLanguageManager dataService) {
        this.gameEngine = gameEngine;
        this.scanner = new Scanner(System.in);
        this.dataService = dataService;
        gameEngine.addObserver(this);
    }

    public void start() {
        System.out.println("=== Language Connection Game ===");
        System.out.println("Connect countries through shared languages!");
        System.out.println("Rarer languages earn more points. " +
                                   "Maintain a streak for bonus points.\n");

        // Prompt user to select hard more
        System.out.print("Would you like to play in hard mode? (y/n): ");
        String hardModeChoice = scanner.nextLine().trim().toLowerCase();
        boolean hardMode = hardModeChoice.equals("y") || hardModeChoice.equals("yes");
        gameEngine.setHardMode(hardMode);

        if (hardMode) {
            System.out.println("Hard mode activated! You may only use each language up to 4 times.");
        } else {
            System.out.println("Normal mode selected. You may only use each language up to 7 times.");
        }
        gameEngine.resetGame();

        boolean running = true;
        boolean waitingForCountry = false;
        Language selectedLanguage = null;

        while (running) {
            GameState state = gameEngine.getGameState();
            Country current = state.getCurrentCountry();

            if (!state.hasMovesRemaining()) {
                continue;
            }

            if (!waitingForCountry) {
                System.out.println("Current Country: " + current.getName());
                System.out.println("Moves: " + (state.getMoves().size() - 1) + "/" + state.getMaxMoves());
                Set<Language> langs = current.getLanguages();
                List<Language> langList = new ArrayList<>(langs);

                // filter to show only viable languages
                List<Language> viableLangs = new ArrayList<>();
                for (Language lang : langList) {
                    if (gameEngine.isViableLanguage(lang) && !gameEngine.isLanguageLimitReached(lang)) {
                        viableLangs.add(lang);
                    }
                }

                if (viableLangs.isEmpty()) {
                    System.out.println("No viable languages found for " + current.getName() + ".");
                    System.out.println("Generating a new country while preserving your score...");
                    gameEngine.refreshCountry();
                    continue;
                }

                System.out.println("Languages spoken: ");
                for (int i = 0; i < viableLangs.size(); i++) {
                    // Check current usage of this language
                    Language lang = viableLangs.get(i);
                    int usageCount = state.getLanguageUsage().getOrDefault(lang, 0);
                    int limit = gameEngine.isHardMode() ? 4 : 7;

                    System.out.println("  " + (i + 1) + ": " + viableLangs.get(i).getName() +
                            " (Used: " + usageCount + "/" + limit + ")");
                }

                System.out.print("Choose a language (1-" + viableLangs.size() + "): ");
                try {
                    int choice = Integer.parseInt(scanner.nextLine().trim());
                    if (choice >= 1 && choice <= viableLangs.size()) {
                        selectedLanguage = viableLangs.get(choice - 1);
                        boolean languageAccepted = gameEngine.setSelectedLanguage(selectedLanguage);
                        if (languageAccepted) {
                            waitingForCountry = true;
                        } else {
                            // If language was rejected, stay in language selection mode
                            selectedLanguage = null;
                            waitingForCountry = false;
                        }
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
    }


    /**
     * Callback method invoked whenever the game state changes.
     * This method displays the updated game state to the console,
     * including the current country, game mode, moves taken and remaining,
     * available languages, selected language stats, and the player's score.
     * If the move history exists, it is printed as well.
     * Additionally, if the game has ended (no moves remaining),
     * it provides final feedback based on performance and prompts the user
     * to play again or exit the game.
     *
     * @param gameState the updated state of the game
     */
    @Override
    public void onGameStateChanged(GameState gameState) {
        System.out.println("\n--- Game State ---");
        System.out.println("Current Country: " + gameState.getCurrentCountry().getName());
        System.out.println("Game Mode: " + (gameEngine.isHardMode() ? "Hard (limit: 4)" : "Normal (limit: 7)"));
        System.out.println("Moves: " + (gameState.getMoves().size() - 1) + "/" + gameState.getMaxMoves() +
                " (Remaining: " + gameState.getRemainingMoves() + ")");
        Country currentCountry = gameState.getCurrentCountry();
        Set<Language> allLanguages = currentCountry.getLanguages();
        int viableLanguageCount = 0;
        for (Language lang : allLanguages) {
            if (gameEngine.isViableLanguage(lang) && !gameEngine.isLanguageLimitReached(lang)) {
                viableLanguageCount++;
            }
        }

        System.out.println("Available Languages: " + allLanguages.size() + " (Viable: " + viableLanguageCount + ")");

        if (gameState.getCurrentLanguage() != null) {
            Language currentLang = gameState.getCurrentLanguage();
            int usageCount = gameState.getLanguageUsage().getOrDefault(currentLang, 0);
            int limit = gameEngine.isHardMode() ? 4 : 7;

            System.out.println("Current Language: " + currentLang.getName() +
                    " (Streak: " + gameState.getCurrentStreak() +
                    ", Used: " + usageCount + "/" + limit + ")");
        }

        System.out.println("Score: " + gameState.getTotalScore());

        if (gameState.getMoves().size() > 1) {
            System.out.println("\nMove History:");
            for (int i = 1; i < gameState.getMoves().size(); i++) {
                System.out.println("  " + gameState.getMoves().get(i));
            }
        }

        System.out.println("------------------\n");

        // Check if the game is over due to move limit
        if (!gameState.hasMovesRemaining()) {
            System.out.println("\n=== GAME OVER ===");
            System.out.println("You've used all " + gameState.getMaxMoves() + " moves!");
            System.out.println("Final Score: " + gameState.getTotalScore());

            // Add some feedback based on score
            double avgPointsPerMove = (double) gameState.getTotalScore() / gameState.getMaxMoves();
            if (avgPointsPerMove >= 3.0) {
                System.out.println("Amazing! You're a language connection master!");
            } else if (avgPointsPerMove >= 2.5) {
                System.out.println("Great job! You've got a solid strategy!");
            } else if (avgPointsPerMove >= 1.5) {
                System.out.println("Good effort! Try to build longer streaks next time!");
            } else {
                System.out.println("Keep practicing! Aim for longer language streaks to boost your score!");
            }

            System.out.println("===========================");

            // Ask if they want to play again
            System.out.print("Would you like to play again? (y/n): ");
            String playAgain = scanner.nextLine().trim().toLowerCase();
            if (playAgain.equals("y") || playAgain.equals("yes")) {
                // Reset the game and restart
                System.out.print("Would you like to play in hard mode? (y/n): ");
                String hardModeChoice = scanner.nextLine().trim().toLowerCase();
                boolean hardMode = hardModeChoice.equals("y") || hardModeChoice.equals("yes");
                gameEngine.setHardMode(hardMode);
                gameEngine.resetGame();
            } else {
                // Exit the game
                System.out.println("Thanks for playing LingoLink! Goodbye!");
                System.exit(0);
            }
        }
    }
}
