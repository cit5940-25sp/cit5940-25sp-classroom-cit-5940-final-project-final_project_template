package ast;

import java.util.List;

/** Factory for constructing AST nodes. */
public class ASTFactory {
    public static Program createProgram(List<FunctionDecl> functions) {
        return new Program(functions);
    }
    public static FunctionDecl createFunctionDecl(String name, List<String> params, Block body) {
        return new FunctionDecl(name, params, body);
    }
    public static Block createBlock(List<Statement> stmts) {
        return new Block(stmts);
    }
    public static VarDecl createVarDecl(String name, Expression init) {
        return new VarDecl(name, init);
    }
    public static Assignment createAssignment(String name, Expression val) {
        return new Assignment(name, val);
    }
    public static PrintStmt createPrintStmt(Expression expr) {
        return new PrintStmt(expr);
    }
    public static ReturnStmt createReturnStmt(Expression expr) {
        return new ReturnStmt(expr);
    }
    public static IfStmt createIfStmt(Expression cond, Block thenB, List<ElifBranch> elifs, Block elseB) {
        return new IfStmt(cond, thenB, elifs, elseB);
    }
    public static WhileStmt createWhileStmt(Expression cond, Block body) {
        return new WhileStmt(cond, body);
    }
    public static RunWhileStmt createRunWhileStmt(Block body, Expression cond) {
        return new RunWhileStmt(body, cond);
    }
    public static ElifBranch createElifBranch(Expression cond, Block body) {
        return new ElifBranch(cond, body);
    }
    public static IntegerLiteral createIntegerLiteral(int value) {
        return new IntegerLiteral(value);
    }
    public static VarRef createVarRef(String name) {
        return new VarRef(name);
    }
    public static FuncCall createFuncCall(String name, List<Expression> args) {
        return new FuncCall(name, args);
    }
    public static BinaryExpr createBinaryExpr(Expression left, String op, Expression right) {
        return new BinaryExpr(left, op, right);
    }
}