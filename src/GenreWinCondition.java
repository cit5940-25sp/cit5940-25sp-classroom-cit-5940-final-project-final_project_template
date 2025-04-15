public class GenreWinCondition implements WinCondition {
    private String genre;

    public GenreWinCondition(String genre) {
        this.genre = genre;
    }

    @Override
    public boolean checkWin(Player player) {
       for (Movie movie : player.getMoviesPlayed()) {
            if (movie.getGenres().contains(genre)) {
                return true;
            }
        }
        return false;
    }
}
