package services;

import models.Client;
import models.Movie;
import factories.ServiceFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Service class, handles the overall game logic
 */
public class GameService {
    private final ClientService clientService;
    private final MovieService movieService;
    
    private List<Client> players;
    private int currentPlayerIndex;
    private Movie lastMovie;
    private int turnCount;
    private boolean gameOver;
    private Client winner;
    
    /**
     * Constructor
     */
    public GameService(ClientService clientService, MovieService movieService) {
        this.clientService = clientService;
        this.movieService = movieService;
        this.players = new ArrayList<>();
        this.currentPlayerIndex = 0;
        this.turnCount = 1;
        this.gameOver = false;
    }
    
    /**
     * No-arg constructor, automatically gets dependencies from ServiceFactory
     */
    public GameService() {
        this(ServiceFactory.getClientService(), ServiceFactory.getMovieService());
    }
    
    /**
     * Initialize game
     */
    public void initGame(List<Client> players) {
        this.players = new ArrayList<>(players);
        this.currentPlayerIndex = 0;
        this.lastMovie = null;
        this.turnCount = 1;
        this.gameOver = false;
        this.winner = null;
    }
    
    /**
     * Get current player
     */
    public Client getCurrentPlayer() {
        if (players.isEmpty()) {
            return null;
        }
        return players.get(currentPlayerIndex);
    }
    
    /**
     * Get current player index
     */
    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }
    
    /**
     * Get next player
     */
    public void nextPlayer() {
        if (players.isEmpty()) {
            return;
        }
        
        // Clear current player's selected movie flag
        Client currentPlayer = getCurrentPlayer();
        if (currentPlayer != null) {
            currentPlayer.clearSelectedMovie();
        }
        
        // Switch to next player
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        
        // Check current player status
        Client nextPlayer = getCurrentPlayer();
        
        // If player is skipped, clear skip status and switch to next player again
        if (nextPlayer.isSkipped()) {
            clientService.clearSkip(nextPlayer);
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        }
        // If player is blocked, don't clear block status immediately
        // This allows the frontend to see the blocked status, and the player will get an error when trying to select a movie
        else if (nextPlayer.isBlocked()) {
            // Don't immediately clear block status, let frontend detect it
            // Block status will be checked when player tries to select a movie, or cleared next time nextPlayer is called
        }
        
        // If back to first player, increase turn count
        if (currentPlayerIndex == 0) {
            turnCount++;
        }
    }
    
    /**
     * Process movie selection
     */
    public boolean processMovieSelection(int movieId) {
        Movie movie = movieService.getMovieById(movieId);
        if (movie == null) {
            return false;
        }
        
        Client currentPlayer = getCurrentPlayer();
        if (currentPlayer == null) {
            return false;
        }
        
        // Check if player is blocked
        if (currentPlayer.isBlocked()) {
            // Clear block status, but return false to indicate selection failed
            clientService.clearBlock(currentPlayer);
            return false;
        }
        
        // Check if player has already selected a movie this turn
        if (currentPlayer.hasSelectedMovie()) {
            return false;
        }
        
        // Check if movie has already been used
        if (clientService.hasUsedMovie(currentPlayer, movieId)) {
            return false;
        }
        
        // Check if movie is connected to previous movie
        if (lastMovie != null && !movieService.areMoviesConnected(movie, lastMovie)) {
            return false;
        }
        
        // Add movie to player's collection
        clientService.addMovieToClient(currentPlayer, movie);
        lastMovie = movie;
        
        // Mark player as having selected a movie this turn
        currentPlayer.selectMovie();
        
        // Check win condition
        if (clientService.checkWinCondition(currentPlayer)) {
            gameOver = true;
            winner = currentPlayer;
            return true;
        }
        
        // No longer automatically switch to next player, frontend will call nextPlayer API
        return true;
    }
    
    /**
     * Use skip power-up
     */
    public boolean useSkipPowerUp() {
        Client currentPlayer = getCurrentPlayer();
        if (currentPlayer == null) {
            return false;
        }
        
        // Check if player still has skip ability
        if (!currentPlayer.isSkipAvailable()) {
            return false;
        }
        
        // Get next player index
        int nextPlayerIndex = (currentPlayerIndex + 1) % players.size();
        Client nextPlayer = players.get(nextPlayerIndex);
        
        // Activate skip status for next player
        clientService.activateSkip(nextPlayer);
        
        // Mark current player as having used skip ability
        clientService.useSkip(currentPlayer);
        
        // No longer automatically switch to next player, frontend will call nextPlayer API
        return true;
    }
    
    /**
     * Use block power-up
     */
    public boolean useBlockPowerUp() {
        Client currentPlayer = getCurrentPlayer();
        if (currentPlayer == null) {
            return false;
        }
        
        // Check if player still has block ability
        if (!currentPlayer.isBlockAvailable()) {
            return false;
        }
        
        // Get next player index
        int nextPlayerIndex = (currentPlayerIndex + 1) % players.size();
        Client nextPlayer = players.get(nextPlayerIndex);
        
        // Activate block status for next player
        clientService.activateBlock(nextPlayer);
        
        // Mark current player as having used block ability
        clientService.useBlock(currentPlayer);
        
        // No longer automatically switch to next player, frontend will call nextPlayer API
        return true;
    }
    
    /**
     * Get game status
     */
    public boolean isGameOver() {
        return gameOver;
    }
    
    /**
     * Get winner
     */
    public Client getWinner() {
        return winner;
    }
    
    /**
     * Get current turn count
     */
    public int getTurnCount() {
        return turnCount;
    }
    
    /**
     * Get last selected movie
     */
    public Movie getLastMovie() {
        return lastMovie;
    }
    
    /**
     * Get all players
     */
    public List<Client> getPlayers() {
        return new ArrayList<>(players);
    }
}
