package ast;

/** `print expr;` statement. */
public class PrintStmt extends Statement {
    public final Expression expression;

    public PrintStmt(Expression expression) {
        this.expression = expression;
    }

    @Override
    public String toString() {
        return "print " + expression.toString() + ";";
    }
}