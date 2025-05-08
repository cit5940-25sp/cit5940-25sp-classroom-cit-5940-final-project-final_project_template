import java.util.Set;

public class GameView {
    public void displayGameState(Player player1, Player player2, Movie currentMovie, int round) {
        System.out.println("\n===== GAME STATUS =====");
        System.out.println("Current Round: " + round);
        System.out.println("Current Movie: " + currentMovie.getTitle() + " (" + currentMovie.getReleaseYear() + ")");

        System.out.println("\nPlayer 1: " + player1.getName());
        System.out.println("Movies Collected: " + player1.getMoviesPlayed().size());

        System.out.println("\nPlayer 2: " + player2.getName());
        System.out.println("Movies Collected: " + player2.getMoviesPlayed().size());

        System.out.println("===================\n");
    }

    public void displayConnectedMovie(Set<Movie> connectedMovies) {
        System.out.println("\nConnected Movies:");
        for (Movie movie : connectedMovies) {
            System.out.println("- " + movie.getTitle());
        }
    }

    public void displayInvalidMove() {
        System.out.println("\n❌ INVALID MOVE! ❌");
        System.out.println("This move does not comply with the game rules.");
    }

    public void displayWin(Player winner) {
        System.out.println("\n🎉 Congratulations " + winner.getName() + " wins! 🎉");
        System.out.println("Number of movies collected: " + winner.getMoviesPlayed().size());
    }
}
