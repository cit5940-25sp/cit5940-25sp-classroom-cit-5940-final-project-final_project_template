public class MovieNameGame {
    private GameController controller;
    private GameView view;
    private MovieDatabase movieDB;

    public void startGame() throws IOException {
        initializeComponents();
        gameLoop();
    }

    private void initializeComponents() {
        movieDB = new MovieDatabase(); // Load from CSV/API
        GameState state = new GameState();
        controller = new GameController(state, movieDB, view);
    }

    private void gameLoop() throws IOException {
        while (true) {
            KeyStroke key = view.getInput();
            controller.handleInput(key);
            view.render();
        }
    }

    public static void main(String[] args) throws IOException {
        new MovieNameGame().startGame();
    }
 
}
