package ast;

/** `while (cond) { ... }` loop. */
public class WhileStmt extends Statement {
    public final Expression condition;
    public final Block body;

    public WhileStmt(Expression condition, Block body) {
        this.condition = condition;
        this.body = body;
    }

    @Override
    public String toString() {
        return "while (" + condition.toString() + ") " + body.toString();
    }

    @Override
    public <R> R accept(ASTVisitor<R> visitor) {
        return visitor.visitWhileStmt(this);
    }
}
