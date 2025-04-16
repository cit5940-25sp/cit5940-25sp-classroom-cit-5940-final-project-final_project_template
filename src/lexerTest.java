import org.junit.Test;

import java.util.List;

public class lexerTest {
    @Test
    public void testTokenize() {
        Lexer lexer = new Lexer("testFile.txt");
        List<Token> actual = lexer.tokenize("testFile.txt");
        System.out.println(actual);
    }
}
