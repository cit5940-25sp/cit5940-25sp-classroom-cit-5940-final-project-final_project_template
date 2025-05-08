package ast;

/** An `elif` clause (condition + body). */
public class ElifBranch extends ASTNode {
    public final Expression condition;
    public final Block body;

    public ElifBranch(Expression condition, Block body) {
        this.condition = condition;
        this.body = body;
    }

    @Override
    public String toString() {
        return "elif " + condition.toString() + " " + body.toString();
    }
}
