import ast.*;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class InterpreterTest {

    @Test
    public void testAdditionAndPrint() {
        // var x <- 1 + 2;
        VarDecl decl = new VarDecl("x",
                new BinaryExpr(new IntegerLiteral(1), "+", new IntegerLiteral(2))
        );
        // print x;
        PrintStmt print = new PrintStmt(new VarRef("x"));

        Block body = new Block(List.of(decl, print));
        FunctionDecl entry = new FunctionDecl("entry", new ArrayList<>(), body);
        Program program = new Program(List.of(entry));

        // 捕捉 System.out 的输出
        String output = runWithCapturedOutput(() -> new Interpreter(program));
        assertEquals("3\n", output);
    }

    @Test
    public void testReturnStatement() {
        ReturnStmt ret = new ReturnStmt(new IntegerLiteral(42));
        Block body = new Block(List.of(ret));
        FunctionDecl entry = new FunctionDecl("entry", new ArrayList<>(), body);
        Program program = new Program(List.of(entry));

        Interpreter interp = new Interpreter(program);
        Object result = interp.getResult();
        assertEquals(42, result);
    }

    // 工具函数：捕获 System.out.println 输出内容
    private String runWithCapturedOutput(Runnable runnable) {
        java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
        java.io.PrintStream oldOut = System.out;
        System.setOut(new java.io.PrintStream(out));
        runnable.run();
        System.setOut(oldOut);
        return out.toString();
    }
}
