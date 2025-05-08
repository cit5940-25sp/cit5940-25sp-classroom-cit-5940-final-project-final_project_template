import ast.*;

import java.util.List;

// To try this, use the commands below:

// javac -d . src/SPROLARunner.java src/Lexer.java src/Parser.java src/Token.java src/TokenType.java src/ast/*.java
// then run java SPROLARunner <my_file>.

// after testing, run the commands below:

// find . -name "*.class" -type f -delete
// rm -r ast

public class SPROLARunner {
    public static void main(String[] args) {

        // Check usage
        if (args.length != 1) {
            System.err.println("Usage: java SPROLARunner <input_file>");
            System.exit(1);
        }

        // Invoke lexer and parser
        String filename = args[0];
        Lexer lexer = new Lexer(filename);
        List<Token> tokens = lexer.tokenize(filename);
        System.out.println(tokens);

        Parser parser = new Parser(tokens);
        Program p = parser.parseProgram();

        // reverse-engineer the original code for checking
        System.out.println(p);
    }
}
