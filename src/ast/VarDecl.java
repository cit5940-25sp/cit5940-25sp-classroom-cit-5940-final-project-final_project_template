package ast;

/** `var name <- expr;` declaration. */
public class VarDecl extends Statement {
    public final String name;
    public final Expression initializer;

    public VarDecl(String name, Expression initializer) {
        this.name = name;
        this.initializer = initializer;
    }

    @Override
    public String toString() {
        return "var " + name + " <- " + initializer.toString() + ";";
    }
}
