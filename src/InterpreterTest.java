import ast.*;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class InterpreterTest {

//        function entry() {
//            var x <- 1 + 2;
//            print x;
//        }

    //Program
    //└── FunctionDecl("entry")
    //    └── Block
    //        ├── VarDecl("x")
    //        │   └── BinaryExpr("+")
    //        │       ├── IntegerLiteral(1)
    //        │       └── IntegerLiteral(2)
    //        └── PrintStmt
    //            └── VarRef("x")



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

//    function entry() {
//        if (0) {
//            print 100;
//        } elif (0) {
//            print 200;
//        } else {
//            print 300;
//        }
//    }
    @Test
    public void testVisitIfstmt() {
        // 构建 else block: print 300;
        PrintStmt elsePrint = new PrintStmt(new IntegerLiteral(300));
        Block elseBlock = new Block(List.of(elsePrint));

        // 构建 elif: elif (0) { print 200; }
        PrintStmt elifPrint = new PrintStmt(new IntegerLiteral(200));
        Block elifBlock = new Block(List.of(elifPrint));
        ElifBranch elif = new ElifBranch(new IntegerLiteral(0), elifBlock);
        List<ElifBranch> elifs = List.of(elif);

        // 构建 if: if (0) { print 100; }
        PrintStmt ifPrint = new PrintStmt(new IntegerLiteral(100));
        Block ifBlock = new Block(List.of(ifPrint));
        IfStmt ifStmt = new IfStmt(new IntegerLiteral(0), ifBlock, elifs, elseBlock);

        // 构建函数体和程序
        FunctionDecl entry = new FunctionDecl("entry", new ArrayList<>(), new Block(List.of(ifStmt)));
        Program program = new Program(List.of(entry));

        // 捕捉输出
        String output = runWithCapturedOutput(() -> new Interpreter(program));
        assertEquals("300\n", output);
    }


//    function double(n) {
//        return n + n;
//    }
//
//    function entry() {
//        var result <- double(5);
//        print result;
//    }
    @Test
    public void testFunctionCall() {
        // function double(n) { return n + n; }
        String funcName = "double";
        List<String> params = List.of("n");
        Expression bodyExpr = new BinaryExpr(new VarRef("n"), "+", new VarRef("n"));
        ReturnStmt returnStmt = new ReturnStmt(bodyExpr);
        Block funcBody = new Block(List.of(returnStmt));
        FunctionDecl doubleFunc = new FunctionDecl(funcName, params, funcBody);

        // function entry() {
        //     var result <- double(5);
        //     print result;
        // }
        FuncCall callDouble = new FuncCall("double", List.of(new IntegerLiteral(5)));
        VarDecl resultDecl = new VarDecl("result", callDouble);
        PrintStmt printResult = new PrintStmt(new VarRef("result"));
        Block entryBody = new Block(List.of(resultDecl, printResult));
        FunctionDecl entry = new FunctionDecl("entry", new ArrayList<>(), entryBody);

        // Create Program and run interpreter
        Program program = new Program(List.of(doubleFunc, entry));
        String output = runWithCapturedOutput(() -> new Interpreter(program));

        assertEquals("10\n", output);
    }

    // helper 函数：捕获 System.out.println 输出内容
    private String runWithCapturedOutput(Runnable runnable) {
        java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
        java.io.PrintStream oldOut = System.out;
        System.setOut(new java.io.PrintStream(out));
        runnable.run();
        System.setOut(oldOut);
        return out.toString();
    }
}
