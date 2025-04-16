public enum TokenType {
    // Keywords
    VAR, FUNCTION, RETURN, IF, ELIF, ELSE, WHILE, RUN, PRINT, INPUT,

    // Literals
    INTEGER,
    STRING,
    FLOAT,
    DOUBLE,

    // Identifiers
    IDENTIFIER,

    // Symbols and operators
    ASSIGN,        // <-
    PLUS, MINUS, STAR, SLASH, MOD,       // + - * / %
    LT, GT, LE, GE, EQ, NEQ,             // < > <= >= = ~
    LPAREN, RPAREN, LBRACE, RBRACE,     // ( ) { }
    COMMA, SEMICOLON,                   // , ;

    // End of input
    EOF

}
