package ast;

/** Binary operation, e.g. `left + right` or `a < b`. */
public class BinaryExpr extends Expression {
    public final Expression left;
    public final String operator;
    public final Expression right;

    public BinaryExpr(Expression left, String operator, Expression right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    @Override
    public String toString() {
        return "(" + left.toString() + " " + operator + " " + right.toString() + ")";
    }

    @Override
    public <R> R accept(ASTVisitor<R> visitor) {
        return visitor.visitBinaryExpr(this);
    }
}