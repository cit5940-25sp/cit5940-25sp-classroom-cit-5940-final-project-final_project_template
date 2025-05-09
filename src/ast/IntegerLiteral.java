package ast;

/** Literal integer, e.g. `42`. */
public class IntegerLiteral extends Expression {
    public final int value;

    public IntegerLiteral(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }

    @Override
    public <R> R accept(ASTVisitor<R> visitor) {
        return visitor.visitIntegerLiteral(this);
    }
}
