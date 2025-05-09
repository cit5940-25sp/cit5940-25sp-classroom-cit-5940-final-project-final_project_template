
import java.util.*;
import ast.*;

public class Parser {
    private final List<Token> tokens;
    private int current = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    // Utility methods
    private boolean isAtEnd() {
        return peek().type == TokenType.EOF;
    }

    private Token peek() {
        return tokens.get(current);
    }

    private Token previous() {
        return tokens.get(current - 1);
    }

    private Token advance() {
        if (!isAtEnd()) current++;
        return previous();
    }

    private void skipNonCode() {
        while (match(TokenType.COMMENT)) {
        }
    }

    private boolean check(TokenType type) {
        return !isAtEnd() && peek().type == type;
    }

    private boolean match(TokenType... types) {
        for (TokenType type : types) {
            if (check(type)) {
                advance();
                return true;
            }
        }
        return false;
    }

    private Token consume(TokenType type, String message) {
        skipNonCode();
        if (check(type)) return advance();
        throw new RuntimeException("Parse error: " + message + " at token '" + peek().lexeme + "'");
    }

    // Parse program and functions
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

    private Block parseBlock() {
        consume(TokenType.LBRACE, "Expected '{' to begin block.");
        List<Statement> statements = new ArrayList<>();
        skipNonCode();
        while (!check(TokenType.RBRACE) && !isAtEnd()) {
            skipNonCode();
            statements.add(parseStatement());
            skipNonCode();
        }
        consume(TokenType.RBRACE, "Expected '}' after block.");
        return ASTFactory.createBlock(statements);
    }

    // Parse statements
    private Statement parseStatement() {
        if (match(TokenType.VAR))     return parseVarDecl();
        if (match(TokenType.IF))      return parseIf();
        if (match(TokenType.WHILE))   return parseWhile();
        if (match(TokenType.RUN))     return parseRunWhile();
        if (match(TokenType.PRINT))   return parsePrint();
        if (match(TokenType.RETURN))  return parseReturn();
        return parseAssignment();
    }

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

    private Statement parseAssignment() {
        String name = consume(TokenType.IDENTIFIER, "Expected variable name.").lexeme;
        consume(TokenType.ASSIGN, "Expected '<-' in assignment.");
        Expression expr = parseExpression();
        consume(TokenType.SEMICOLON, "Expected ';' after assignment.");
        return ASTFactory.createAssignment(name, expr);
    }

    private Statement parsePrint() {
        Expression expr = parseExpression();
        consume(TokenType.SEMICOLON, "Expected ';' after print statement.");
        return ASTFactory.createPrintStmt(expr);
    }

    private Statement parseReturn() {
        Expression expr = parseExpression();
        consume(TokenType.SEMICOLON, "Expected ';' after return statement.");
        return ASTFactory.createReturnStmt(expr);
    }

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

    private Statement parseWhile() {
        consume(TokenType.LPAREN, "Expected '(' after 'while'.");
        Expression condition = parseExpression();
        consume(TokenType.RPAREN, "Expected ')' after while condition.");
        Block body = parseBlock();
        return ASTFactory.createWhileStmt(condition, body);
    }

    private Statement parseRunWhile() {
        Block body = parseBlock();
        consume(TokenType.WHILE, "Expected 'while' after 'run' block.");
        consume(TokenType.LPAREN, "Expected '(' after 'while'.");
        Expression condition = parseExpression();
        consume(TokenType.RPAREN, "Expected ')' after run-while condition.");
//        consume(TokenType.SEMICOLON, "Expected ';' after run-while statement.");
        return ASTFactory.createRunWhileStmt(body, condition);
    }

    // Expression parsing
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

    private Expression parseUnary() {
        if (match(TokenType.MINUS)) {
            String op = previous().lexeme;
            Expression right = parseUnary();
            return ASTFactory.createBinaryExpr(ASTFactory.createIntegerLiteral(0), op, right);
        }
        return parsePrimary();
    }

    private Expression parsePrimary() {
        if (match(TokenType.INTEGER)) {
            return ASTFactory.createIntegerLiteral(Integer.parseInt(previous().lexeme));
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
