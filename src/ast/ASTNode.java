package ast;

/** Base class for all AST nodes. */
public abstract class ASTNode implements Visitable<Object> {
    public abstract String toString();
    // common fields (e.g. source location) can be added here later


    @Override
    public Object accept(ASTVisitor<Object> visitor) {
        return null;
    }
}