package ast;

/** `name <- expr;` assignment. */
public class Assignment extends Statement {
    public final String name;
    public final Expression value;

    public Assignment(String name, Expression value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String toString() {
        return name + " <- " + value.toString() + ";";
    }
}