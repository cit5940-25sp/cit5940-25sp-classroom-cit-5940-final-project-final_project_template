package ast;

import java.util.List;
import java.util.stream.Collectors;

/** The root AST node, containing all top‑level function declarations. */
public class Program extends ASTNode {
    public final List<FunctionDecl> functions;

    public Program(List<FunctionDecl> functions) {
        this.functions = functions;
    }

    @Override
    public String toString() {
        return functions.stream()
                .map(FunctionDecl::toString)
                .collect(Collectors.joining("\n\n"));
    }
}