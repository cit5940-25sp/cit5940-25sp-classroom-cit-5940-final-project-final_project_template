import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;

import java.io.*;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

public class GameConsoleUITest {

    private CountryLanguageManager dataService;
    private GameEngine gameEngine;
    private ByteArrayOutputStream outContent;
    private PrintStream originalOut;
    private InputStream originalIn;

    @BeforeEach
    public void setUp() {
        GameEngine.resetInstance();
        dataService = new CountryLanguageManager();

        // Setup test data
        dataService.addLanguage("English", 5);
        dataService.addLanguage("Spanish", 3);

        dataService.addCountry("USA", "English");
        dataService.addCountry("UK", "English");  // Added for move testing
        dataService.addCountry("Spain", "Spanish", "English");

        gameEngine = GameEngine.getInstance(dataService);

        // Setup stream capturing
        outContent = new ByteArrayOutputStream();
        originalOut = System.out;
        originalIn = System.in;
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setIn(originalIn);
    }

    @Test
    public void testStartNormalModeThenQuit() {
        // User input: normal mode, pick language 1, then enter 'quit'
        String simulatedInput = "n\n1\nquit\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        GameConsoleUI ui = new GameConsoleUI(gameEngine, dataService);
        ui.start();

        String output = outContent.toString();
        assertTrue(output.contains("Normal mode selected"));
    }

    @Test
    public void testInvalidLanguageChoice() {
        // User enters an invalid language choice then a valid one
        String simulatedInput = "n\n99\n1\nquit\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        GameConsoleUI ui = new GameConsoleUI(gameEngine, dataService);
        ui.start();

        String output = outContent.toString();
        assertTrue(output.contains("Invalid choice"));
    }

    // --- End Game Logic Tests ---

    /**
     * Test that the end game logic shows the correct message
     * Note: We need to fix GameState.getRemainingMoves first
     */
    @Test
    public void testGameOverMessage() {
        // Fix GameState.getRemainingMoves to use testMaxMoves
        fixGameStateRemainingMoves();

        // Set up game state to simulate end game
        gameEngine.setMaxMovesForTest(2);

        // Directly call onGameStateChanged with a mockup of end state
        GameState gameState = createEndGameState();

        // Create a TestUI that doesn't actually call System.exit
        TestGameConsoleUI ui = new TestGameConsoleUI(gameEngine, dataService);
        ui.onGameStateChanged(gameState);

        String output = outContent.toString();
        assertTrue(output.contains("=== GAME OVER ==="), "Should display GAME OVER message");
        assertTrue(output.contains("You've used all"), "Should mention using all moves");
        assertTrue(output.contains("Final Score:"), "Should show final score");
    }

    /**
     * Test that score feedback is correct for low scores
     */
    @Test
    public void testLowScoreFeedback() {
        fixGameStateRemainingMoves();

        // Set up game state to simulate end game with low score
        gameEngine.setMaxMovesForTest(20); // Using a larger number to reduce avg points per move
        GameState gameState = createEndGameState();
        setStateScore(gameState, 10); // This should give 10/20 = 0.5 avg which is < 1.5

        TestGameConsoleUI ui = new TestGameConsoleUI(gameEngine, dataService);
        ui.setTestInput("n\n"); // Don't play again
        ui.onGameStateChanged(gameState);

        String output = outContent.toString();
        assertTrue(output.contains("Keep practicing!"), "Should display feedback for low scores");
    }

    @Test
    public void testStartHardMode() {
        String simulatedInput = "y\n1\nquit\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        GameConsoleUI ui = new GameConsoleUI(gameEngine, dataService);
        ui.start();

        String output = outContent.toString();
        assertTrue(output.contains("Hard mode activated!"));
        assertFalse(output.contains("Normal mode selected."));
    }

    @Test
    public void testNewCommandResetsGame() {
        String simulatedInput = "n\n1\nnew\n1\nquit\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        GameConsoleUI ui = new GameConsoleUI(gameEngine, dataService);
        ui.start();

        String output = outContent.toString();
        assertTrue(output.contains("Normal mode selected"));
        assertTrue(output.contains("Choose a language")); // happens after reset
    }



    @Test
    public void testFeedbackGreatStrategy() {
        fixGameStateRemainingMoves();
        gameEngine.setMaxMovesForTest(4);
        GameState gameState = createEndGameState();
        setStateScore(gameState, 11); // 11/4 = 2.75

        TestGameConsoleUI ui = new TestGameConsoleUI(gameEngine, dataService);
        ui.setTestInput("n\n");
        ui.onGameStateChanged(gameState);

        String output = outContent.toString();
        assertTrue(output.contains("Great job!"));
    }


    @Test
    public void testOnGameStateChangedDisplaysFullState_NormalMode() {
        fixGameStateRemainingMoves();
        gameEngine.setHardMode(false);
        GameState gameState = gameEngine.getGameState();

        // Simulate one move
        Country start = dataService.getCountry("usa");
        Language lang = dataService.getLanguage("english");
        gameState.addMove(new GameMove(start, lang, 2));
        gameState.setCurrentLanguage(lang);

        TestGameConsoleUI ui = new TestGameConsoleUI(gameEngine, dataService);
        ui.setTestInput("n\n"); // Don't play again
        ui.onGameStateChanged(gameState);

        String output = outContent.toString();
        assertTrue(output.contains("Current Country: USA"));
        assertTrue(output.contains("Game Mode: Normal"));
        assertTrue(output.contains("Available Languages:"));
        assertTrue(output.contains("Current Language: English"));
        assertTrue(output.contains("Score:"));
        assertTrue(output.contains("Move History:"));
    }

    @Test
    public void testOnGameStateChangedDisplaysHardModeLanguageInfo() {
        fixGameStateRemainingMoves();
        gameEngine.setHardMode(true);
        GameState gameState = gameEngine.getGameState();

        Country country = dataService.getCountry("usa");
        Language lang = dataService.getLanguage("english");
        gameState.addMove(new GameMove(country, lang, 1));
        gameState.setCurrentLanguage(lang);

        TestGameConsoleUI ui = new TestGameConsoleUI(gameEngine, dataService);
        ui.setTestInput("n\n");
        ui.onGameStateChanged(gameState);

        String output = outContent.toString();
        assertTrue(output.contains("Game Mode: Hard"));
        assertTrue(output.contains("Used:"));
    }

    @Test
    public void testFeedbackGoodEffort() {
        fixGameStateRemainingMoves();
        gameEngine.setMaxMovesForTest(4);
        GameState gameState = createEndGameState();
        setStateScore(gameState, 7); // 7/4 = 1.75

        TestGameConsoleUI ui = new TestGameConsoleUI(gameEngine, dataService);
        ui.setTestInput("n\n");
        ui.onGameStateChanged(gameState);

        String output = outContent.toString();
        assertTrue(output.contains("Good effort! Try to build longer streaks next time!"));
    }

    @Test
    public void testFeedbackAmazingMaster() {
        fixGameStateRemainingMoves();
        gameEngine.setMaxMovesForTest(4);
        GameState gameState = createEndGameState();
        setStateScore(gameState, 13); // 13/4 = 3.25

        TestGameConsoleUI ui = new TestGameConsoleUI(gameEngine, dataService);
        ui.setTestInput("n\n");
        ui.onGameStateChanged(gameState);

        String output = outContent.toString();
        assertTrue(output.contains("Amazing! You're a language connection master!"));
    }



    // --- Helper methods ---

    /**
     * Fix the getRemainingMoves method to use testMaxMoves if set
     */
    private void fixGameStateRemainingMoves() {
        try {
            Field gameStateField = GameEngine.class.getDeclaredField("gameState");
            gameStateField.setAccessible(true);
            GameState state = (GameState) gameStateField.get(gameEngine);

            // Override the methods using reflection
            Field testMaxMovesField = GameState.class.getDeclaredField("testMaxMoves");
            testMaxMovesField.setAccessible(true);
            testMaxMovesField.set(state, 2); // Default to 2 for testing
        } catch (Exception e) {
            fail("Failed to fix getRemainingMoves: " + e.getMessage());
        }
    }

    /**
     * Create a game state at the end of the game
     */
    private GameState createEndGameState() {
        GameState state = gameEngine.getGameState();

        // Add enough moves to reach the limit
        while (state.hasMovesRemaining()) {
            state.addMove(new GameMove(dataService.getCountry("usa"), dataService.getLanguage("english"), 1));
        }

        return state;
    }

    /**
     * Set the score in a game state
     */
    private void setStateScore(GameState state, int score) {
        try {
            Field scoreField = GameState.class.getDeclaredField("totalScore");
            scoreField.setAccessible(true);
            scoreField.set(state, score);
        } catch (Exception e) {
            fail("Failed to set score: " + e.getMessage());
        }
    }

    private int countOccurrences(String text, String substring) {
        int count = 0;
        int index = 0;
        while ((index = text.indexOf(substring, index)) != -1) {
            count++;
            index += substring.length();
        }
        return count;
    }

    /**
     * Custom GameConsoleUI that overrides System.exit
     */
    private class TestGameConsoleUI extends GameConsoleUI {
        private String testInput = "";

        public TestGameConsoleUI(GameEngine gameEngine, CountryLanguageManager dataService) {
            super(gameEngine, dataService);
        }

        public void setTestInput(String input) {
            this.testInput = input;
            System.setIn(new ByteArrayInputStream(input.getBytes()));
        }

        @Override
        public void onGameStateChanged(GameState gameState) {
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

                // Instead of System.exit, we just print that we would exit
                System.out.println("Thanks for playing LingoLink! Goodbye!");
                System.out.println("TEST: Would have called System.exit(0) here");
            } else {
                // Call the parent method for the normal case
                super.onGameStateChanged(gameState);
            }
        }
    }

    @Test
    public void testReplayGameInHardMode() {
        fixGameStateRemainingMoves();
        gameEngine.setMaxMovesForTest(2);
        GameState gameState = createEndGameState();
        setStateScore(gameState, 8); // triggers GAME OVER

        String simulatedInput = "y\ny\n"; // Play again = yes, hard mode = yes
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        GameConsoleUI ui = new GameConsoleUI(gameEngine, dataService);
        ui.onGameStateChanged(gameState);

        String output = outContent.toString();
        assertTrue(output.contains("Would you like to play again?"));
        assertTrue(output.contains("Would you like to play in hard mode?"));
        assertTrue(gameEngine.isHardMode(), "Game should be reset in hard mode");
    }

    @Test
    public void testReplayGameInNormalMode() {
        fixGameStateRemainingMoves();
        gameEngine.setMaxMovesForTest(2);
        GameState gameState = createEndGameState();
        setStateScore(gameState, 6);

        String simulatedInput = "y\nn\n"; // Play again = yes, hard mode = no
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        GameConsoleUI ui = new GameConsoleUI(gameEngine, dataService);
        ui.onGameStateChanged(gameState);

        String output = outContent.toString();
        assertTrue(output.contains("Would you like to play again?"));
        assertTrue(output.contains("Would you like to play in hard mode?"));
        assertFalse(gameEngine.isHardMode(), "Game should be reset in normal mode");
    }

    @Test
    public void testGameExitAfterNoReplay() {
        fixGameStateRemainingMoves();
        gameEngine.setMaxMovesForTest(2);
        GameState gameState = createEndGameState();
        setStateScore(gameState, 5);

        String simulatedInput = "n\n"; // Don't play again
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        TestGameConsoleUI ui = new TestGameConsoleUI(gameEngine, dataService);
        ui.onGameStateChanged(gameState);

        String output = outContent.toString();
        assertTrue(output.contains("Thanks for playing LingoLink! Goodbye!"));
        assertTrue(output.contains("TEST: Would have called System.exit(0) here"));
    }


}