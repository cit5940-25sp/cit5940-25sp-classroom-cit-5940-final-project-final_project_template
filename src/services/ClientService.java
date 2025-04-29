package services;

import models.Client;
import models.Movie;

/**
 * Service class, handles client/player-related business logic
 */
public class ClientService {
    
    /**
     * Check if player has met win condition
     */
    public boolean checkWinCondition(Client client) {
        return client.hasMetWinCondition();
    }
    
    /**
     * Add movie to player's collection
     */
    public void addMovieToClient(Client client, Movie movie) {
        client.addMovie(movie);
    }
    
    /**
     * Activate player's skip turn status
     */
    public void activateSkip(Client client) {
        client.activateSkip();
    }
    
    /**
     * Clear player's skip status
     */
    public void clearSkip(Client client) {
        client.clearSkip();
    }

    /**
     * Activate player's blocked status
     */
    public void activateBlock(Client client) {
        client.activateBlock();
    }

    /**
     * Clear player's block status
     */
    public void clearBlock(Client client) {
        client.clearBlock();
    }

    /**
     * Use block ability
     */
    public void useBlock(Client client) {
        client.useBlock();
    }
    
    /**
     * Use skip ability
     */
    public void useSkip(Client client) {
        client.useBlock(); // Both skip and block use the same underlying mechanism to mark as used
    }
    
    /**
     * Check if player has already used the specified movie
     */
    public boolean hasUsedMovie(Client client, int movieId) {
        return client.hasUsedMovie(movieId);
    }
}
