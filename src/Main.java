import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        String apiKey = ConfigLoader.get("tmdb.api.key");
        GameController controller = new GameController(apiKey);
        try {
            TerminalWithSuggestions tui = new TerminalWithSuggestions(controller);
            tui.run();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
