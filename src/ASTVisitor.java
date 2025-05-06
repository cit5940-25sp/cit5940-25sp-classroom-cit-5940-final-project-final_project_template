import ast.*;

public interface ASTVisitor<R> {
    R visitAssignment(Assignment assignment);
    R visitBinaryExpr(BinaryExpr be);
    R visitBlock(Block block);
    R visitElifBranch(ElifBranch elifb);
    R visitExpression(Expression expression);
    R visitFuncCall(FuncCall fc);
    R visitFunctionDecl(FunctionDecl fd);
    R visitIfstmt(IfStmt is);
    R visitIntegerLiteral(IntegerLiteral il);
    R visitPrintStmt(PrintStmt ps);
    R visitProgram(Program program);
    R visitReturnStmt(ReturnStmt rs);
    R visitRunWhileStmt(RunWhileStmt rws);
    R visitStatement(Statement statement);
    R visitVarDecl(VarDecl vd);
    R visitVarRef(VarRef vr);
    R visitWhileStmt(WhileStmt ws);
}
