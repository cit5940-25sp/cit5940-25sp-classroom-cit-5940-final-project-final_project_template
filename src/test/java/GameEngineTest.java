import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class MockRandom extends Random {
    private final int[] values;
    private int index = 0;

    public MockRandom(int[] values) {
        this.values = values;
    }

    @Override
    public int nextInt(int bound) {
        return values[index++ % values.length] % bound;
    }
}

public class GameEngineTest {
    private CountryLanguageManager clm;
    private GameEngine engine;

    @BeforeEach
    public void setUp() {
        GameEngine.resetInstance();
        clm = new CountryLanguageManager();
        clm.addLanguage("spanish", 2);
        clm.addLanguage("english", 1);
        clm.addLanguage("french", 3);

        clm.addCountry("chile", "spanish");
        clm.addCountry("colombia", "spanish", "english");
        clm.addCountry("canada", "english", "french");
        clm.addCountry("puerto rico", "english", "spanish");
        engine = GameEngine.getInstance(clm);
        MockRandom random = new MockRandom(new int[]{2}); // country set to colombia
        engine.resetGame(random);
    }

    @Test
    public void testSetSelectedLanguage() {
        engine.setSelectedLanguage(new Language("english", 2));
        assertEquals(new Language("english", 2), engine.getGameState().getCurrentLanguage());
        assertEquals(0, engine.getGameState().getCurrentStreak());
    }

    @Test
    public void testMoveToCountryWithStreak() {
        engine.getGameState().setCurrentStreak(1);
        engine.getGameState().setCurrentLanguage(new Language("english", 2));

        MoveResult mr1 = engine.moveToCountry("CANADA");

        // streak should increment by 1
        assertEquals(2, engine.getGameState().getCurrentStreak());
//        System.out.println(engine.getGameState().getCurrentStreak());
        // score should increment by 2 * 2
        assertEquals(4, engine.getGameState().getTotalScore());

        MoveResult mr2 = engine.moveToCountry("Colombia");

        // streak and score remains the same as colombia was used for prompting
        assertEquals(2, engine.getGameState().getCurrentStreak());
        assertEquals(4, engine.getGameState().getTotalScore());
    }

    @Test
    public void testMoveToUsedCountry() {
        MoveResult mr = engine.moveToCountry("colombia");
        assertFalse(mr.isSuccess());
        assertTrue(mr.getMessage().contains("already used"));
    }


    @Test
    public void testSelectLanguageWithMultipleSharedLanguages() {
        System.out.println(engine.getGameState().getCurrentCountry());
        System.out.println(clm.getAllCountries());
        Language spanish = clm.getLanguage("spanish");
        Country pr = clm.getCountry("puerto rico");
        engine.getGameState().setCurrentLanguage(clm.getLanguage("english"));
//        System.out.println(engine.getGameState().getCurrentStreak());

        engine.setSelectedLanguage(spanish);
//        System.out.println(engine.getGameState().getCurrentStreak());
        MoveResult mr = engine.moveToCountry("puerto rico");
//        System.out.println(engine.getGameState().getCurrentStreak());
        GameState newState = engine.getGameState();

        assertTrue(mr.isSuccess());
        assertEquals(pr, newState.getCurrentCountry());
        assertEquals(2, newState.getMoves().size()); // total move size = 2
        assertEquals(1, newState.getCurrentStreak()); // starts new streak
        assertEquals(spanish, newState.getCurrentLanguage());
    }
}
