import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class TestGameModel {
    @Test
    public void testInitializePlayers() {
        GameModel model = new GameModel();
        model.initializePlayers();
        List<IPlayer> players = model.getPlayers();
        assertEquals(2, players.size());
        assertNotNull(players.get(0).getWinConditionStrategy());
        assertNotNull(players.get(1).getWinConditionStrategy());
    }

    @Test
    public void testGetCurrentPlayer() {
        GameModel model = new GameModel();
        model.initializePlayers();
        IPlayer current = model.getCurrentPlayer();
        assertEquals(model.getPlayers().get(0), current);
    }

    @Test
    public void testSwitchToNextPlayerAndRoundCount() {
        GameModel model = new GameModel();
        model.initializePlayers();
        IPlayer first = model.getCurrentPlayer();
        model.switchToNextPlayer();
        IPlayer second = model.getCurrentPlayer();
        assertNotEquals(first, second);
        assertEquals(1, model.getRoundCount());
    }

    @Test
    public void testSetAndGetCurrentMovie() {
        GameModel model = new GameModel();
        IMovie movie = new Movie("Test Movie", 2023, List.of("Action"));
        model.setStartingMovie(movie);
        assertEquals(movie, model.getCurrentMovie());

        model.setCurrentMovie(null);
        assertNull(model.getCurrentMovie());
    }

    @Test
    public void testCheckWinCondition() {
        GameModel model = new GameModel();
        Player player = new Player("Tommy");

        player.setWinConditionStrategy(new IWinConditionStrategy() {
            public boolean checkWin(List<IMovie> movies) { return true; }
            public String getDescription() { return "Always win"; }
        });

        assertTrue(model.checkWinCondition(player));
        assertEquals(player, model.getWinner());
    }

    @Test
    public void testIsGameOverFalseThenTrue() {
        GameModel model = new GameModel();
        assertFalse(model.isGameOver());

        Player player = new Player("Melody");
        player.setWinConditionStrategy(new IWinConditionStrategy() {
            public boolean checkWin(List<IMovie> movies) { return true; }
            public String getDescription() { return ""; }
        });
        model.checkWinCondition(player);
        assertTrue(model.isGameOver());
    }

    @Test
    public void testRecentHistoryLimit() {
        GameModel model = new GameModel();
        model.setStartingMovie(new Movie("Start", 2000, List.of("Genre")));
        model.initializePlayers();

        for (int i = 0; i < 6; i++) {
            IMovie movie = new Movie("M" + i, 2000 + i, List.of("Test"));
            model.setCurrentMovie(movie);
            model.makeMove("M" + i); // just simulating history growth
        }

        List<IMovie> history = model.getRecentHistory();
        assertTrue(history.size() <= 5);
    }

    @Test
    public void testConvertMapToListOfMovies() {
        GameModel model = new GameModel();
        Map<Integer, IMovie> map = new HashMap<>();
        map.put(1, new Movie("A", 2001, List.of("X")));
        map.put(2, new Movie("B", 2002, List.of("Y")));
        List<IMovie> list = model.convertMapToListOfMovies(map);
        assertEquals(2, list.size());
    }

    @Test
    public void testGetPlayer1AndPlayer2() {
        GameModel model = new GameModel();
        model.initializePlayers();
        assertNotNull(model.getPlayer1());
        assertNotNull(model.getPlayer2());
    }

    @Test
    public void testGetMovies() {
        GameModel model = new GameModel();
        model.initializePlayers();
        Map<Integer, IMovie> movies = model.getMovies();
        assertNotNull(movies);
    }
}
