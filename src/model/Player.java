package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a player in the Movie Name Game, tracking the movies they have played.
 */
public class Player {
    private String playerName;
    private List<Movie> playedMovies;

    /**
     * Constructs a Player with the specified name.
     *
     * @param playerName the name of the player
     */
    public Player(String playerName) {
        this.playerName = playerName;
        this.playedMovies = new ArrayList<>();
    }

    /**
     * Returns the player's name.
     *
     * @return the name of the player
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * Returns the list of movies played by the player.
     *
     * @return a list of movies
     */
    public List<Movie> getPlayedMovies() {
        return playedMovies;
    }

    /**
     * Adds a movie to the list of movies played by this player.
     *
     * @param movie the movie to add
     */
    public void addPlayedMovie(Movie movie) {
        playedMovies.add(movie);
    }
}
