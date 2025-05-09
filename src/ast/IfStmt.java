package ast;

import java.util.List;

/** `if (cond) { ... } [elif (...) { ... }]* [else { ... }]` */
public class IfStmt extends Statement {
    public final Expression condition;
    public final Block thenBranch;
    public final List<ElifBranch> elifBranches;
    public final Block elseBranch;  // may be null

    public IfStmt(Expression condition,
                  Block thenBranch,
                  List<ElifBranch> elifBranches,
                  Block elseBranch) {
        this.condition = condition;
        this.thenBranch = thenBranch;
        this.elifBranches = elifBranches;
        this.elseBranch = elseBranch;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("if (").append(condition.toString()).append(") ")
                .append(thenBranch.toString());

        for (ElifBranch elif : elifBranches) {
            sb.append("\n").append(elif.toString());
        }

        if (elseBranch != null) {
            sb.append("\nelse ").append(elseBranch.toString());
        }

        return sb.toString();
    }

    @Override
    public <R> R accept(ASTVisitor<R> visitor) {
        return visitor.visitIfstmt(this);
    }
}
