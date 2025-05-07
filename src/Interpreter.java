import ast.*;
import java.util.Map;
import java.util.HashMap;


public class Interpreter implements ASTVisitor<Object> {

    private final Environment globals = new Environment();  // 顶层作用域（保持不变）
    private Environment environment = globals;              // 当前作用域（会切换）
    private final Map<String, FunctionDecl> functionTable = new HashMap<>();

    // constructor
    public Interpreter(Program program) {
        visitProgram(program);
    }

    @Override
    public Object visitAssignment(Assignment assignment) {
        return null;
    }

    @Override
    public Object visitBinaryExpr(BinaryExpr be) {
        return null;
    }

    @Override
    public Object visitBlock(Block block) {
        return null;
    }

    @Override
    public Object visitElifBranch(ElifBranch elifb) {
        return null;
    }

    @Override
    public Object visitExpression(Expression expression) {
        return null;
    }

    @Override
    public Object visitFuncCall(FuncCall fc) {
        return null;
    }

    @Override
    public Object visitFunctionDecl(FunctionDecl fd) {
        return null;
    }

    @Override
    public Object visitIfstmt(IfStmt is) {
        return null;
    }

    @Override
    public Object visitIntegerLiteral(IntegerLiteral il) {
        return null;
    }

    @Override
    public Object visitPrintStmt(PrintStmt ps) {
        return null;
    }

    @Override
    public Object visitProgram(Program program) {
        // 1. 把所有函数加入函数表
        for (FunctionDecl func : program.functions) {
            functionTable.put(func.name, func);
        }

        // 2. 找到 entry 函数
        FunctionDecl entry = functionTable.get("entry");
        if (entry == null) {
            throw new RuntimeException("No entry() function found.");
        }

        // 3. 调用 entry()
        return visitFunctionDecl(entry);
    }

    @Override
    public Object visitReturnStmt(ReturnStmt rs) {
        return null;
    }

    @Override
    public Object visitRunWhileStmt(RunWhileStmt rws) {
        return null;
    }

    @Override
    public Object visitStatement(Statement statement) {
        return null;
    }

    @Override
    public Object visitVarDecl(VarDecl vd) {
        return null;
    }

    @Override
    public Object visitVarRef(VarRef vr) {
        return null;
    }

    @Override
    public Object visitWhileStmt(WhileStmt ws) {
        return null;
    }
}
