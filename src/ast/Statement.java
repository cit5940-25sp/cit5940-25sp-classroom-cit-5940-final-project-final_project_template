package ast;

/** Base class for all statement nodes. */
public abstract class Statement extends ASTNode {
    @Override
    public <R> R accept(ASTVisitor<R> visitor) {
        return visitor.visitStatement(this);
    }
}