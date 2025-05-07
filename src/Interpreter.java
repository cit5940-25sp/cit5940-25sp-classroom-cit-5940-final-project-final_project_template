import ast.*;

public class Interpreter implements ASTVisitor<Object> {

    private final Environment env = new Environment();

    // constructor
    public Interpreter(Program program) {

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
        return null;
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
