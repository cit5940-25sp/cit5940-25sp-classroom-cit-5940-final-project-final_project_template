package ast;

/** `return expr;` statement. */
public class ReturnStmt extends Statement {
    public final Expression expression;

    public ReturnStmt(Expression expression) {
        this.expression = expression;
    }

    @Override
    public String toString() {
        return "return " + expression.toString() + ";";
    }

    @Override
    public <R> R accept(ASTVisitor<R> visitor) {
        return visitor.visitReturnStmt(this);
    }
}