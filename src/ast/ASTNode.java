package ast;

/** Base class for all AST nodes. */
public abstract class ASTNode implements Visitable {
    public abstract String toString();
    // common fields (e.g. source location) can be added here later


    @Override
    public abstract <R> R accept(ASTVisitor<R> visitor);
}