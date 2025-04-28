package ast;

import java.util.List;
import java.util.stream.Collectors;

/** A sequence of statements enclosed in `{}`. */
public class Block extends Statement {
    public final List<Statement> statements;

    public Block(List<Statement> statements) {
        this.statements = statements;
    }

    @Override
    public String toString() {
        return "{\n" +
                statements.stream()
                        .map(stmt -> "  " + stmt.toString())
                        .collect(Collectors.joining("\n")) +
                "\n}";
    }
}