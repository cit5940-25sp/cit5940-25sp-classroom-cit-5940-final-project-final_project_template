package ast;

/** `run { ... } while (cond);` loop (do-while style). */
public class RunWhileStmt extends Statement {
    public final Block body;
    public final Expression condition;

    public RunWhileStmt(Block body, Expression condition) {
        this.body = body;
        this.condition = condition;
    }

    @Override
    public String toString() {
        return "run " + body.toString() + "while " + condition.toString();
    }

    @Override
    public <R> R accept(ASTVisitor<R> visitor) {
        return visitor.visitRunWhileStmt(this);
    }
}
