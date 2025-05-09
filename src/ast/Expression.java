package ast;

/** Base class for all expression nodes. */
public abstract class Expression extends ASTNode {
    @Override
    public <R> R accept(ASTVisitor<R> visitor) {
        return visitor.visitExpression(this);
    }
}