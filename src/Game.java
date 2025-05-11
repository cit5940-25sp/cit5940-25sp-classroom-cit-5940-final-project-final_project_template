public class Game {
    Movies movies;
    Player player1;
    Player player2;
    String currentMovie;

    public void Game(String fileName, String player1Name, String player2Name, String level) {
        this.movies = new Movies(fileName);






        this.player1 = new Player(player1Name, "someGenre", 1);
        this.player2 = new Player(player2Name, "someGenre", 1);
    }
}
