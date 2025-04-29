package view;

/**
 * A simple text-based view for displaying the game state and prompting user input.
 */
public class GameView {

    /**
     * Displays the current game state to the user.
     *
     * @param info a string containing the information about the current game state
     */
    public void renderGameState(String info) {
        System.out.println(info);
    }

    /**
     * Prompts the user to enter a movie name.
     */
    public void renderInputPrompt() {
        System.out.print("Please enter a movie name: ");
    }

    /**
     * Displays an error message to the user.
     *
     * @param message the error message to display
     */
    public void displayError(String message) {
        System.out.println("Error: " + message);
    }
}
