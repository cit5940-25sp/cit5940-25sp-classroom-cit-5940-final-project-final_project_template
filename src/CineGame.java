// Import the Scanner class to read user input from the standard input stream
import java.util.Scanner;

/**
 * CineGame class represents the main entry point for the movie-related game.
 * It initializes the MVC (Model-View-Controller) components and orchestrates the game flow.
 */
public class CineGame {
    // Reference to the GameModel object, which manages the game's data and logic
    private GameModel model;
    // Reference to the GameView object, responsible for displaying the game's user interface
    private GameView view;
    // Reference to the GameControl object, which handles user input and interactions
    private GameControl control;

    /**
     * Constructor for the CineGame class.
     * Initializes the MVC components and sets up the observer relationship between the model and the view.
     */
    public CineGame(){
        // Create a new instance of GameView
        view = new GameView();
        // Create a new instance of GameModel
        model = new GameModel();
        // Create a new instance of GameControl
        control = new GameControl();
        // Register the view as an observer of the model so it can be notified of changes
        model.addObserver(view);
        // Set the model for the controller, allowing it to interact with the game data
        control.setModel(model);
        // Set the model for the view, enabling it to display the game data
        view.setModel(model);
    }

    /**
     * Initializes the game by starting the view, loading initial data,
     * and adding two players to the game.
     */
    public void init(){
        // Start the game view, typically displaying the initial screen
        view.start();
        // Initialize the game data in the model
        model.initialData();
        // Prompt the user to enter the first player's name and create a Player object
        Player player1 = readPlayer("Please enter the first player's name:");
        // Add the first player to the game model
        model.addPlayer(player1);
        // Prompt the user to enter the second player's name and create a Player object
        Player player2 = readPlayer("Please enter the second player's name:");
        // Add the second player to the game model
        model.addPlayer(player2);
    }

    /**
     * Prompts the user to enter a player's name and creates a new Player object.
     *
     * @param prompt The message to display to the user when asking for the player's name.
     * @return A new Player object with the name entered by the user.
     */
    public Player readPlayer(String prompt){
        // Display the prompt to the user
        System.out.println(prompt);
        // Create a new Scanner object to read user input
        Scanner scanner = new Scanner(System.in);
        // Read a line of input from the user as the player's name
        String name = scanner.nextLine();
        // Create and return a new Player object with the entered name
        return new Player(name);
    }

    /**
     * Main game loop that runs until the game is over.
     * It advances the game to the next round and handles user input.
     */
    public void gameLoop(){
        // Continue the loop as long as the game is not over
        while(!model.isGameOver()){
            // Move the game to the next round
            model.nextRound();
            // Handle user input through the controller
            control.runInput();
        }
    }
    public static void main(String[] args) {
        CineGame game = new CineGame();
        game.init();
        game.gameLoop();
    }
}


*