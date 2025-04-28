import ast.*;

import java.util.List;

public class main {
    public static void main(String[] args) {
        Lexer lexer = new Lexer("testFile.txt");
        List<Token> tokens = lexer.tokenize("testFile.txt");
        System.out.println(tokens);

        Parser parser = new Parser(tokens);
        Program p = parser.parseProgram();
        System.out.println(p);
    }
}
