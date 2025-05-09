package ast;

public class InputExpr extends Expression {

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitInputExpr(this);
    }

    @Override
    public String toString() {
        return "input";
    }
}
