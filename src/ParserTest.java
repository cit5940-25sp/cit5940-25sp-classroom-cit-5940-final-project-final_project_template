import ast.*;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

public class ParserTest {

    @Test
    public void testParseProgram() {
        // function foo() {} function entry() {}
        List<Token> tokens = new ArrayList<>();
        tokens.add(new Token(TokenType.FUNCTION, "function"));
        tokens.add(new Token(TokenType.IDENTIFIER, "foo"));
        tokens.add(new Token(TokenType.LPAREN, "("));
        tokens.add(new Token(TokenType.RPAREN, ")"));
        tokens.add(new Token(TokenType.LBRACE, "{"));
        tokens.add(new Token(TokenType.RBRACE, "}"));

        tokens.add(new Token(TokenType.FUNCTION, "function"));
        tokens.add(new Token(TokenType.IDENTIFIER, "entry"));
        tokens.add(new Token(TokenType.LPAREN, "("));
        tokens.add(new Token(TokenType.RPAREN, ")"));
        tokens.add(new Token(TokenType.LBRACE, "{"));
        tokens.add(new Token(TokenType.RBRACE, "}"));

        Program program = new Parser(tokens).parseProgram();
        assertEquals(2, program.functions.size());
        assertEquals("foo", program.functions.get(0).name);
        assertEquals("entry", program.functions.get(1).name);
    }

    @Test
    public void testParseFunctionDecl() {
        // function bar(a,b) {}
        List<Token> t = Arrays.asList(
                new Token(TokenType.FUNCTION, "function"),
                new Token(TokenType.IDENTIFIER, "bar"),
                new Token(TokenType.LPAREN, "("),
                new Token(TokenType.IDENTIFIER, "a"),
                new Token(TokenType.COMMA, ","),
                new Token(TokenType.IDENTIFIER, "b"),
                new Token(TokenType.RPAREN, ")"),
                new Token(TokenType.LBRACE, "{"),
                new Token(TokenType.RBRACE, "}")
        );
        Program p = new Parser(new ArrayList<>(t)).parseProgram();
        FunctionDecl fd = p.functions.get(0);
        assertEquals("bar", fd.name);
        assertEquals(Arrays.asList("a","b"), fd.params);
        assertTrue(fd.body.statements.isEmpty());
    }

    @Test
    public void testParseVarDecl() {
        // function entry() { var x <- 5; }
        List<Token> t = Arrays.asList(
                new Token(TokenType.FUNCTION,"function"),
                new Token(TokenType.IDENTIFIER,"entry"),
                new Token(TokenType.LPAREN,"("), new Token(TokenType.RPAREN,")"),
                new Token(TokenType.LBRACE,"{"),
                new Token(TokenType.VAR,"var"),
                new Token(TokenType.IDENTIFIER,"x"),
                new Token(TokenType.ASSIGN,"<-"),
                new Token(TokenType.INTEGER,"5"),
                new Token(TokenType.SEMICOLON,";"),
                new Token(TokenType.RBRACE,"}")
        );
        VarDecl vd = (VarDecl) new Parser(new ArrayList<>(t)).parseProgram()
                .functions.get(0).body.statements.get(0);
        assertEquals("x", vd.name);
        assertEquals(5, ((IntegerLiteral)vd.initializer).value);
    }

    @Test
    public void testParseAssignment() {
        // function entry() { y <- input; }
        List<Token> t = Arrays.asList(
                new Token(TokenType.FUNCTION,"function"),
                new Token(TokenType.IDENTIFIER,"entry"),
                new Token(TokenType.LPAREN,"("), new Token(TokenType.RPAREN,")"),
                new Token(TokenType.LBRACE,"{"),
                new Token(TokenType.IDENTIFIER,"y"),
                new Token(TokenType.ASSIGN,"<-"),
                new Token(TokenType.INPUT,"input"),
                new Token(TokenType.SEMICOLON,";"),
                new Token(TokenType.RBRACE,"}")
        );
        Assignment a = (Assignment) new Parser(new ArrayList<>(t)).parseProgram()
                .functions.get(0).body.statements.get(0);
        assertEquals("y", a.name);
        assertTrue(a.value instanceof InputExpr);
    }

    @Test
    public void testParsePrintStmt() {
        // function entry() { print x; }
        List<Token> t = Arrays.asList(
                new Token(TokenType.FUNCTION,"function"),
                new Token(TokenType.IDENTIFIER,"entry"), new Token(TokenType.LPAREN,"("), new Token(TokenType.RPAREN,")"),
                new Token(TokenType.LBRACE,"{"),
                new Token(TokenType.PRINT,"print"),
                new Token(TokenType.IDENTIFIER,"x"),
                new Token(TokenType.SEMICOLON,";"),
                new Token(TokenType.RBRACE,"}")
        );
        PrintStmt ps = (PrintStmt) new Parser(new ArrayList<>(t)).parseProgram()
                .functions.get(0).body.statements.get(0);
        assertTrue(ps.expression instanceof VarRef);
    }

    @Test
    public void testParseReturnStmt() {
        // function entry() { return 10; }
        List<Token> t = Arrays.asList(
                new Token(TokenType.FUNCTION,"function"),
                new Token(TokenType.IDENTIFIER,"entry"), new Token(TokenType.LPAREN,"("), new Token(TokenType.RPAREN,")"),
                new Token(TokenType.LBRACE,"{"),
                new Token(TokenType.RETURN,"return"),
                new Token(TokenType.INTEGER,"10"),
                new Token(TokenType.SEMICOLON,";"),
                new Token(TokenType.RBRACE,"}")
        );
        ReturnStmt rs = (ReturnStmt) new Parser(new ArrayList<>(t)).parseProgram()
                .functions.get(0).body.statements.get(0);
        assertEquals(10, ((IntegerLiteral)rs.expression).value);
    }

    @Test
    public void testParseIfStmt() {
        // function entry() { if (1) { print 1; } }
        List<Token> t = Arrays.asList(
                new Token(TokenType.FUNCTION,"function"),
                new Token(TokenType.IDENTIFIER,"entry"), new Token(TokenType.LPAREN,"("), new Token(TokenType.RPAREN,")"),
                new Token(TokenType.LBRACE,"{"),
                new Token(TokenType.IF,"if"), new Token(TokenType.LPAREN,"("),
                new Token(TokenType.INTEGER,"1"), new Token(TokenType.RPAREN,")"),
                new Token(TokenType.LBRACE,"{"),
                new Token(TokenType.PRINT,"print"), new Token(TokenType.INTEGER,"1"), new Token(TokenType.SEMICOLON,";"),
                new Token(TokenType.RBRACE,"}"),
                new Token(TokenType.RBRACE,"}")
        );
        IfStmt is = (IfStmt) new Parser(new ArrayList<>(t)).parseProgram()
                .functions.get(0).body.statements.get(0);
        assertTrue(is.thenBranch.statements.get(0) instanceof PrintStmt);
    }

    @Test
    public void testParseWhileStmt() {
        // function entry() { while (0) { x <- x; } }
        List<Token> t = Arrays.asList(
                new Token(TokenType.FUNCTION,"function"),
                new Token(TokenType.IDENTIFIER,"entry"), new Token(TokenType.LPAREN,"("), new Token(TokenType.RPAREN,")"),
                new Token(TokenType.LBRACE,"{"),
                new Token(TokenType.WHILE,"while"), new Token(TokenType.LPAREN,"("),
                new Token(TokenType.INTEGER,"0"), new Token(TokenType.RPAREN,")"),
                new Token(TokenType.LBRACE,"{"),
                new Token(TokenType.IDENTIFIER,"x"), new Token(TokenType.ASSIGN,"<-"), new Token(TokenType.IDENTIFIER,"x"), new Token(TokenType.SEMICOLON,";"),
                new Token(TokenType.RBRACE,"}"),
                new Token(TokenType.RBRACE,"}")
        );
        WhileStmt ws = (WhileStmt) new Parser(new ArrayList<>(t)).parseProgram()
                .functions.get(0).body.statements.get(0);
        assertTrue(ws.body.statements.get(0) instanceof Assignment);
    }

    @Test
    public void testParseRunWhileStmt() {
        // function entry() { run { x <- 1; } while (x) }
        List<Token> t = Arrays.asList(
                new Token(TokenType.FUNCTION,"function"),
                new Token(TokenType.IDENTIFIER,"entry"), new Token(TokenType.LPAREN,"("), new Token(TokenType.RPAREN,")"),
                new Token(TokenType.LBRACE,"{"),
                new Token(TokenType.RUN,"run"), new Token(TokenType.LBRACE,"{"),
                new Token(TokenType.IDENTIFIER,"x"), new Token(TokenType.ASSIGN,"<-"), new Token(TokenType.INTEGER,"1"), new Token(TokenType.SEMICOLON,";"),
                new Token(TokenType.RBRACE,"}"), new Token(TokenType.WHILE,"while"), new Token(TokenType.LPAREN,"("),
                new Token(TokenType.IDENTIFIER,"x"), new Token(TokenType.RPAREN,")"),
                new Token(TokenType.RBRACE,"}")
        );
        RunWhileStmt rws = (RunWhileStmt) new Parser(new ArrayList<>(t)).parseProgram()
                .functions.get(0).body.statements.get(0);
        assertTrue(rws.body.statements.get(0) instanceof Assignment);
    }

    @Test
    public void testParseFuncCall() {
        // function entry() { var y <- f(); }
        List<Token> t = Arrays.asList(
                new Token(TokenType.FUNCTION,"function"),
                new Token(TokenType.IDENTIFIER,"entry"), new Token(TokenType.LPAREN,"("), new Token(TokenType.RPAREN,")"),
                new Token(TokenType.LBRACE,"{"),
                new Token(TokenType.VAR,"var"), new Token(TokenType.IDENTIFIER,"y"), new Token(TokenType.ASSIGN,"<-"),
                new Token(TokenType.IDENTIFIER,"f"), new Token(TokenType.LPAREN,"("), new Token(TokenType.RPAREN,")"),
                new Token(TokenType.SEMICOLON,";"),
                new Token(TokenType.RBRACE,"}")
        );
        VarDecl vd = (VarDecl) new Parser(new ArrayList<>(t)).parseProgram()
                .functions.get(0).body.statements.get(0);
        assertTrue(vd.initializer instanceof FuncCall);
        assertEquals("f", ((FuncCall)vd.initializer).name);
    }

    @Test
    public void testParseBinaryPrecedence() {
        // function entry() { var x <- 1 + 2 * 3 - 4 / 2; }
        List<Token> t = Arrays.asList(
                new Token(TokenType.FUNCTION,"function"), new Token(TokenType.IDENTIFIER,"entry"),
                new Token(TokenType.LPAREN,"("), new Token(TokenType.RPAREN,")"), new Token(TokenType.LBRACE,"{"),
                new Token(TokenType.VAR,"var"), new Token(TokenType.IDENTIFIER,"x"), new Token(TokenType.ASSIGN,"<-"),
                new Token(TokenType.INTEGER,"1"), new Token(TokenType.PLUS,"+"),
                new Token(TokenType.INTEGER,"2"), new Token(TokenType.STAR,"*"), new Token(TokenType.INTEGER,"3"),
                new Token(TokenType.MINUS,"-"), new Token(TokenType.INTEGER,"4"),
                new Token(TokenType.SLASH,"/"), new Token(TokenType.INTEGER,"2"),
                new Token(TokenType.SEMICOLON,";"), new Token(TokenType.RBRACE,"}")
        );
        VarDecl vd = (VarDecl) new Parser(new ArrayList<>(t)).parseProgram()
                .functions.get(0).body.statements.get(0);
        Expression expr = vd.initializer;
        // Top level should be BinaryExpr "-"
        assertTrue(expr instanceof BinaryExpr);
        assertEquals("-", ((BinaryExpr)expr).operator);
    }

    @Test
    public void testParseUnaryAndGrouping() {
        // function entry() { var x <- -(1 + 2); }
        List<Token> t = Arrays.asList(
                new Token(TokenType.FUNCTION,"function"), new Token(TokenType.IDENTIFIER,"entry"),
                new Token(TokenType.LPAREN,"("), new Token(TokenType.RPAREN,")"), new Token(TokenType.LBRACE,"{"),
                new Token(TokenType.VAR,"var"), new Token(TokenType.IDENTIFIER,"x"), new Token(TokenType.ASSIGN,"<-"),
                new Token(TokenType.MINUS,"-"), new Token(TokenType.LPAREN,"("),
                new Token(TokenType.INTEGER,"1"), new Token(TokenType.PLUS,"+"), new Token(TokenType.INTEGER,"2"),
                new Token(TokenType.RPAREN,")"), new Token(TokenType.SEMICOLON,";"),
                new Token(TokenType.RBRACE,"}")
        );
        VarDecl vd = (VarDecl) new Parser(new ArrayList<>(t)).parseProgram()
                .functions.get(0).body.statements.get(0);
        // Should be BinaryExpr with operator "-" and left IntegerLiteral(0)
        BinaryExpr unary = (BinaryExpr) vd.initializer;
        assertEquals("-", unary.operator);
        assertTrue(unary.left instanceof IntegerLiteral);
    }
}
