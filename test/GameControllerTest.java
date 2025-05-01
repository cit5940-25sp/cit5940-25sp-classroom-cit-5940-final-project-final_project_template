import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class GameControllerTest {

    @Mock
    private MovieDatabase mockMovieDb;
    
    @Mock
    private GameView mockView;
    
    @Mock
    private WinCondition mockWinCondition;
    
    private GameController controller;
    private Movie godfather;
    private Movie starWars;
    private Movie titanic;
    
    @Before
    public void setUp() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);
        
        // Create the controller with mocked dependencies
        controller = new GameController("dummy-api-key");
        
        // Replace the actual dependencies with mocks using reflection
        try {
            java.lang.reflect.Field movieDbField = GameController.class.getDeclaredField("movieDb");
            movieDbField.setAccessible(true);
            movieDbField.set(controller, mockMovieDb);
            
            java.lang.reflect.Field viewField = GameController.class.getDeclaredField("view");
            viewField.setAccessible(true);
            viewField.set(controller, mockView);
        } catch (Exception e) {
            fail("Failed to set up mocks: " + e.getMessage());
        }
        
        // Create sample movie objects for testing
        godfather = new Movie(
            1L, 
            "The Godfather", 
            1972, 
            Set.of("Crime", "Drama"), 
            Set.of("Marlon Brando", "Al Pacino"), 
            Set.of("Francis Ford Coppola"), 
            Set.of("Mario Puzo", "Francis Ford Coppola"), 
            Set.of("Nino Rota"), 
            Set.of("Gordon Willis")
        );
        
        starWars = new Movie(
            2L, 
            "Star Wars", 
            1977, 
            Set.of("Sci-Fi", "Adventure"), 
            Set.of("Mark Hamill", "Harrison Ford", "Carrie Fisher"), 
            Set.of("George Lucas"), 
            Set.of("George Lucas"), 
            Set.of("John Williams"), 
            Set.of("Gilbert Taylor")
        );
        
        titanic = new Movie(
            3L, 
            "Titanic", 
            1997, 
            Set.of("Romance", "Drama"), 
            Set.of("Leonardo DiCaprio", "Kate Winslet"), 
            Set.of("James Cameron"), 
            Set.of("James Cameron"), 
            Set.of("James Horner"), 
            Set.of("Russell Carpenter")
        );
    }
    
    @Test
    public void testStartGame_WithValidStartingMovie() {
        // Arrange
        when(mockMovieDb.findByTitle("The Godfather")).thenReturn(godfather);
        when(mockWinCondition.description()).thenReturn("Test Win Condition");
        
        // Act
        controller.startGame("Player1", "Player2", mockWinCondition);
        
        // Assert
        verify(mockMovieDb).findByTitle("The Godfather");
        verify(mockView, times(3)).displayInfo(anyString());
        verify(mockView).render(any(GameState.class));
        
        // Verify game state was initialized with the correct players
        ArgumentCaptor<GameState> gameStateCaptor = ArgumentCaptor.forClass(GameState.class);
        verify(mockView).render(gameStateCaptor.capture());
        GameState capturedState = gameStateCaptor.getValue();
        
        assertEquals("Player1", capturedState.getCurrentPlayer().getName());
    }
    
    @Test
    public void testStartGame_FallbackMovie() {
        // Arrange - Godfather not found, but Star Wars is found
        when(mockMovieDb.findByTitle("The Godfather")).thenReturn(null);
        when(mockMovieDb.findByTitle("Star Wars")).thenReturn(starWars);
        when(mockWinCondition.description()).thenReturn("Test Win Condition");
        
        // Act
        controller.startGame("Player1", "Player2", mockWinCondition);
        
        // Assert
        verify(mockMovieDb).findByTitle("The Godfather");
        verify(mockMovieDb).findByTitle("Star Wars");
        verify(mockView).render(any(GameState.class));
    }
    
    @Test
    public void testStartGame_NoMoviesFound() {
        // Arrange - No movies found at all
        when(mockMovieDb.findByTitle(anyString())).thenReturn(null);
        
        // Act
        controller.startGame("Player1", "Player2", mockWinCondition);
        
        // Assert
        verify(mockMovieDb).findByTitle("The Godfather");
        verify(mockMovieDb).findByTitle("Star Wars");
        verify(mockView).displayInfo(contains("Could not find a starting movie"));
        verify(mockView, never()).render(any(GameState.class));
    }
    
    @Test
    public void testProcessTurn_MovieNotFound() {
        // Arrange
        setupGameState();
        when(mockMovieDb.findByTitle("Nonexistent Movie")).thenReturn(null);
        
        // Act
        controller.processTurn("Nonexistent Movie");
        
        // Assert
        verify(mockMovieDb).findByTitle("Nonexistent Movie");
        verify(mockView).displayInfo(contains("Movie not found"));
    }
    
    @Test
    public void testProcessTurn_MovieAlreadyUsed() {
        // Arrange
        GameState gameState = setupGameState();
        when(mockMovieDb.findByTitle("The Godfather")).thenReturn(godfather);
        when(gameState.isMovieUsed(godfather)).thenReturn(true);
        
        // Act
        controller.processTurn("The Godfather");
        
        // Assert
        verify(mockMovieDb).findByTitle("The Godfather");
        verify(mockView).displayInfo(contains("Movie already used"));
    }
    
    @Test
    public void testProcessTurn_NoValidConnection() {
        // Arrange
        GameState gameState = setupGameState();
        when(mockMovieDb.findByTitle("Titanic")).thenReturn(titanic);
        when(gameState.isMovieUsed(titanic)).thenReturn(false);
        when(gameState.getCurrentMovie()).thenReturn(godfather);
        
        // Act
        controller.processTurn("Titanic");
        
        // Assert
        verify(mockMovieDb).findByTitle("Titanic");
        verify(mockView).displayInfo(contains("No valid connection found"));
    }
    
    @Test
    public void testProcessTurn_ValidMove() {
        // Arrange
        GameState gameState = setupGameState();
        Player player1 = mock(Player.class);
        when(player1.getName()).thenReturn("Player1");
        
        when(mockMovieDb.findByTitle("Titanic")).thenReturn(titanic);
        when(gameState.isMovieUsed(titanic)).thenReturn(false);
        when(gameState.getCurrentMovie()).thenReturn(godfather);
        when(gameState.getCurrentPlayer()).thenReturn(player1);
        
        // Create a valid connection
        Connection validConn = new Connection("Francis Ford Coppola", ConnectionType.DIRECTOR);
        List<Connection> connections = new ArrayList<>();
        connections.add(validConn);
        
        // Setup controller to find a valid connection
        doReturn(true).when(controller).isValidConnection(godfather, titanic);
        
        // Use reflection to create findValidConnection method
        try {
            java.lang.reflect.Method findValidConnMethod = GameController.class.getDeclaredMethod(
                "findValidConnection", Movie.class, Movie.class);
            findValidConnMethod.setAccessible(true);
            
            // Create a partial mock of controller to intercept the private method
            GameController spyController = spy(controller);
            doReturn(validConn).when(spyController).findValidConnection(any(Movie.class), any(Movie.class));
            
            // Execute with the spy
            when(gameState.canUseConnection(validConn.getPersonName())).thenReturn(true);
            when(gameState.hasCurrentPlayerWon()).thenReturn(false);
            
            spyController.processTurn("Titanic");
            
            // Verify correct sequence of actions
            verify(gameState).incrementConnectionUsage(validConn.getPersonName());
            verify(gameState).addMovieToHistory(titanic);
            verify(player1).addGuessedMovie(titanic);
            verify(gameState).hasCurrentPlayerWon();
            verify(gameState).switchPlayer();
            verify(mockView).render(gameState);
            
        } catch (Exception e) {
            fail("Test failed: " + e.getMessage());
        }
    }
    
    @Test
    public void testProcessTurn_PlayerWins() {
        // Arrange
        GameState gameState = setupGameState();
        Player player1 = mock(Player.class);
        when(player1.getName()).thenReturn("Player1");
        
        when(mockMovieDb.findByTitle("Titanic")).thenReturn(titanic);
        when(gameState.isMovieUsed(titanic)).thenReturn(false);
        when(gameState.getCurrentMovie()).thenReturn(godfather);
        when(gameState.getCurrentPlayer()).thenReturn(player1);
        
        // Create a valid connection
        Connection validConn = new Connection("Francis Ford Coppola", ConnectionType.DIRECTOR);
        
        // Setup controller to find a valid connection
        doReturn(true).when(controller).isValidConnection(godfather, titanic);
        
        // Use reflection to create findValidConnection method
        try {
            // Create a partial mock of controller to intercept the private method
            GameController spyController = spy(controller);
            doReturn(validConn).when(spyController).findValidConnection(any(Movie.class), any(Movie.class));
            
            // Execute with the spy
            when(gameState.canUseConnection(validConn.getPersonName())).thenReturn(true);
            when(gameState.hasCurrentPlayerWon()).thenReturn(true);
            when(gameState.getWinCondition()).thenReturn(mockWinCondition);
            when(mockWinCondition.description()).thenReturn("Test Win Condition");
            
            spyController.processTurn("Titanic");
            
            // Verify correct sequence of actions
            verify(gameState).incrementConnectionUsage(validConn.getPersonName());
            verify(gameState).addMovieToHistory(titanic);
            verify(player1).addGuessedMovie(titanic);
            verify(gameState).hasCurrentPlayerWon();
            verify(mockView).displayInfo(contains("has won"));
            verify(gameState, never()).switchPlayer(); // Player won, so no switch
            verify(mockView, never()).render(any()); // No render after win
            
        } catch (Exception e) {
            fail("Test failed: " + e.getMessage());
        }
    }
    
    @Test
    public void testIsValidConnection_HasValidConnection() {
        // Arrange
        GameState gameState = setupGameState();
        
        Connection connection = new Connection("John Williams", ConnectionType.COMPOSER);
        List<Connection> connections = new ArrayList<>();
        connections.add(connection);
        
        // Setup mocks
        when(godfather.findConnections(starWars)).thenReturn(connections);
        when(gameState.canUseConnection("John Williams")).thenReturn(true);
        
        // Act
        boolean result = controller.isValidConnection(godfather, starWars);
        
        // Assert
        assertTrue(result);
    }
    
    @Test
    public void testIsValidConnection_NoValidConnections() {
        // Arrange
        GameState gameState = setupGameState();
        
        Connection connection = new Connection("John Williams", ConnectionType.COMPOSER);
        List<Connection> connections = new ArrayList<>();
        connections.add(connection);
        
        // Setup mocks - connection exists but has been used too many times
        when(godfather.findConnections(starWars)).thenReturn(connections);
        when(gameState.canUseConnection("John Williams")).thenReturn(false);
        
        // Act
        boolean result = controller.isValidConnection(godfather, starWars);
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    public void testIsValidConnection_EmptyConnections() {
        // Arrange
        setupGameState();
        
        // Setup mocks - no connections found
        when(godfather.findConnections(starWars)).thenReturn(new ArrayList<>());
        
        // Act
        boolean result = controller.isValidConnection(godfather, starWars);
        
        // Assert
        assertFalse(result);
    }
    
    // Helper method to set up game state for tests
    private GameState setupGameState() {
        GameState gameState = mock(GameState.class);
        
        try {
            java.lang.reflect.Field gameStateField = GameController.class.getDeclaredField("gameState");
            gameStateField.setAccessible(true);
            gameStateField.set(controller, gameState);
        } catch (Exception e) {
            fail("Failed to set up game state: " + e.getMessage());
        }
        
        return gameState;
    }
}

