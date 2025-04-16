public class Token {
    public final TokenType type;
    public final String lexeme; // store the original code (ex. x for IDEN )
//    public final int line;

    public Token(TokenType type, String lexeme) {
        this.type = type;
        this.lexeme = lexeme;
//        this.line = line;
    }
    @Override

    public String toString() {
        return "Token(" + type + ", \"" + lexeme + "\")";
    }

}
