public class Game {
    Movies movies;
    Player player1;
    Player player2;

    public void Game(String fileName) {
        this.movies = new Movies(fileName);
        this.player1 = new Player("username", "someGenre", 1);
        this.player2 = new Player("username", "someGenre", 1);
    }
}
