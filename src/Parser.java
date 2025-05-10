import java.util.*;
import ast.*;

public class Parser {
    private final List<Token> tokens;
    private int current = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    // --- Utility Methods ---

    // Check if we've reached the end of token list
    private boolean isAtEnd() {
        return peek().type == TokenType.EOF;
    }

    // Look at current token without consuming it
    private Token peek() {
        return tokens.get(current);
    }

    // Look at previous token
    private Token previous() {
        return tokens.get(current - 1);
    }

    // Consume current token and move to the next
    private Token advance() {
        if (!isAtEnd()) current++;
        return previous();
    }

    // Skip comment tokens
    private void skipNonCode() {
        while (match(TokenType.COMMENT)) { }
    }

    // Check if current token matches expected type
    private boolean check(TokenType type) {
        return !isAtEnd() && peek().type == type;
    }

    // Try to match any of the given token types
    private boolean match(TokenType... types) {
        for (TokenType type : types) {
            if (check(type)) {
                advance();
                return true;
            }
        }
        return false;
    }

    // Consume a token of expected type, otherwise error
    private Token consume(TokenType type, String message) {
        skipNonCode();
        if (check(type)) return advance();
        throw new RuntimeException("Parse error: " + message + " at token '" + peek().lexeme + "'");
    }

    // --- Parse Program and Functions ---

    // Parse the entire program (list of functions)
    public Program parseProgram() {
        tokens.add(new Token(TokenType.EOF, ""));
        List<FunctionDecl> functions = new ArrayList<>();
        skipNonCode();
        while (!isAtEnd()) {
            functions.add(parseFunction());
            skipNonCode();
        }
        return ASTFactory.createProgram(functions);
    }

    // Parse a single function definition
    private FunctionDecl parseFunction() {
        skipNonCode();
        consume(TokenType.FUNCTION, "Expected 'function' keyword.");
        String name = consume(TokenType.IDENTIFIER, "Expected function name.").lexeme;
        consume(TokenType.LPAREN, "Expected '(' after function name.");

        List<String> params = new ArrayList<>();
        if (!check(TokenType.RPAREN)) {
            do {
                params.add(consume(TokenType.IDENTIFIER, "Expected parameter name.").lexeme);
            } while (match(TokenType.COMMA));
        }
        consume(TokenType.RPAREN, "Expected ')' after parameters.");

        Block body = parseBlock();
        return ASTFactory.createFunctionDecl(name, params, body);
    }

    // Parse a block of statements enclosed by '{' '}'
    private Block parseBlock() {
        consume(TokenType.LBRACE, "Expected '{' to begin block.");
        List<Statement> statements = new ArrayList<>();
        skipNonCode();
        while (!check(TokenType.RBRACE) && !isAtEnd()) {
            statements.add(parseStatement());
            skipNonCode();
        }
        consume(TokenType.RBRACE, "Expected '}' after block.");
        return ASTFactory.createBlock(statements);
    }

    // --- Parse Statements ---

    // Parse a single statement
    private Statement parseStatement() {
        if (match(TokenType.VAR))     return parseVarDecl();
        if (match(TokenType.IF))      return parseIf();
        if (match(TokenType.WHILE))   return parseWhile();
        if (match(TokenType.RUN))     return parseRunWhile();
        if (match(TokenType.PRINT))   return parsePrint();
        if (match(TokenType.RETURN))  return parseReturn();
        return parseAssignment();
    }

    // Parse a variable declaration (var x <- expr;)
    private Statement parseVarDecl() {
        List<Statement> decls = new ArrayList<>();
        do {
            String name = consume(TokenType.IDENTIFIER, "Expected variable name.").lexeme;
            consume(TokenType.ASSIGN, "Expected '<-' in variable declaration.");
            Expression expr = parseExpression();
            decls.add(ASTFactory.createVarDecl(name, expr));
        } while (match(TokenType.COMMA));
        consume(TokenType.SEMICOLON, "Expected ';' after variable declaration.");
        return decls.size() == 1 ? decls.get(0) : ASTFactory.createBlock(decls);
    }

    // Parse an assignment (x <- expr;)
    private Statement parseAssignment() {
        String name = consume(TokenType.IDENTIFIER, "Expected variable name.").lexeme;
        consume(TokenType.ASSIGN, "Expected '<-' in assignment.");
        Expression expr = parseExpression();
        consume(TokenType.SEMICOLON, "Expected ';' after assignment.");
        return ASTFactory.createAssignment(name, expr);
    }

    // Parse a print statement (print expr;)
    private Statement parsePrint() {
        Expression expr = parseExpression();
        consume(TokenType.SEMICOLON, "Expected ';' after print statement.");
        return ASTFactory.createPrintStmt(expr);
    }

    // Parse a return statement (return expr;)
    private Statement parseReturn() {
        Expression expr = parseExpression();
        consume(TokenType.SEMICOLON, "Expected ';' after return statement.");
        return ASTFactory.createReturnStmt(expr);
    }

    // Parse an if-elif-else statement
    private Statement parseIf() {
        consume(TokenType.LPAREN, "Expected '(' after 'if'.");
        Expression condition = parseExpression();
        consume(TokenType.RPAREN, "Expected ')' after if condition.");
        Block thenBranch = parseBlock();

        List<ElifBranch> elifs = new ArrayList<>();
        while (match(TokenType.ELIF)) {
            consume(TokenType.LPAREN, "Expected '(' after 'elif'.");
            Expression elifCond = parseExpression();
            consume(TokenType.RPAREN, "Expected ')' after elif condition.");
            Block elifBlock = parseBlock();
            elifs.add(ASTFactory.createElifBranch(elifCond, elifBlock));
        }

        Block elseBranch = null;
        if (match(TokenType.ELSE)) {
            elseBranch = parseBlock();
        }
        return ASTFactory.createIfStmt(condition, thenBranch, elifs, elseBranch);
    }

    // Parse a while loop
    private Statement parseWhile() {
        consume(TokenType.LPAREN, "Expected '(' after 'while'.");
        Expression condition = parseExpression();
        consume(TokenType.RPAREN, "Expected ')' after while condition.");
        Block body = parseBlock();
        return ASTFactory.createWhileStmt(condition, body);
    }

    // Parse a run-while loop (do-while style)
    private Statement parseRunWhile() {
        Block body = parseBlock();
        consume(TokenType.WHILE, "Expected 'while' after 'run' block.");
        consume(TokenType.LPAREN, "Expected '(' after 'while'.");
        Expression condition = parseExpression();
        consume(TokenType.RPAREN, "Expected ')' after run-while condition.");
        return ASTFactory.createRunWhileStmt(body, condition);
    }

    // --- Parse Expressions ---

    // Parse an expression (start from lowest precedence)
    private Expression parseExpression() {
        return parseEquality();
    }

    private Expression parseEquality() {
        Expression expr = parseComparison();
        while (match(TokenType.EQ, TokenType.NEQ)) {
            String op = previous().lexeme;
            Expression right = parseComparison();
            expr = ASTFactory.createBinaryExpr(expr, op, right);
        }
        return expr;
    }

    private Expression parseComparison() {
        Expression expr = parseTerm();
        while (match(TokenType.LT, TokenType.LE, TokenType.GT, TokenType.GE)) {
            String op = previous().lexeme;
            Expression right = parseTerm();
            expr = ASTFactory.createBinaryExpr(expr, op, right);
        }
        return expr;
    }

    private Expression parseTerm() {
        Expression expr = parseFactor();
        while (match(TokenType.PLUS, TokenType.MINUS)) {
            String op = previous().lexeme;
            Expression right = parseFactor();
            expr = ASTFactory.createBinaryExpr(expr, op, right);
        }
        return expr;
    }

    private Expression parseFactor() {
        Expression expr = parseUnary();
        while (match(TokenType.STAR, TokenType.SLASH, TokenType.MOD)) {
            String op = previous().lexeme;
            Expression right = parseUnary();
            expr = ASTFactory.createBinaryExpr(expr, op, right);
        }
        return expr;
    }

    // Handle unary expressions like "-5"
    private Expression parseUnary() {
        if (match(TokenType.MINUS)) {
            String op = previous().lexeme;
            Expression right = parseUnary();
            return ASTFactory.createBinaryExpr(ASTFactory.createIntegerLiteral(0), op, right);
        }
        return parsePrimary();
    }

    // Parse primary expressions: literals, identifiers, calls, parentheses
    private Expression parsePrimary() {
        if (match(TokenType.INTEGER)) {
            return ASTFactory.createIntegerLiteral(Integer.parseInt(previous().lexeme));
        }

        if (match(TokenType.INPUT)) {
            return ASTFactory.createInputExpr();
        }

        if (match(TokenType.IDENTIFIER)) {
            String name = previous().lexeme;
            if (match(TokenType.LPAREN)) {
                List<Expression> args = new ArrayList<>();
                if (!check(TokenType.RPAREN)) {
                    do { args.add(parseExpression()); } while (match(TokenType.COMMA));
                }
                consume(TokenType.RPAREN, "Expected ')' after arguments.");
                return ASTFactory.createFuncCall(name, args);
            }
            return ASTFactory.createVarRef(name);
        }

        if (match(TokenType.LPAREN)) {
            Expression expr = parseExpression();
            consume(TokenType.RPAREN, "Expected ')' after expression.");
            return expr;
        }

        throw new RuntimeException("Unexpected token: " + peek().lexeme);
    }
}
