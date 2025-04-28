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
}
