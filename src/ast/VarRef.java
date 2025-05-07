package ast;

/** Reference to a variable, e.g. `x`. */
public class VarRef extends Expression {
    public final String name;

    public VarRef(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public <R> R accept(ASTVisitor<R> visitor) {
        return visitor.visitVarRef(this);
    }
}
