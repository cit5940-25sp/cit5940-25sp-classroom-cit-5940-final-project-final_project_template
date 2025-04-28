package ast;

import java.util.List;

/** A function declaration (name, parameters, and body). */
public class FunctionDecl extends ASTNode {
    public final String name;
    public final List<String> params;
    public final Block body;

    public FunctionDecl(String name, List<String> params, Block body) {
        this.name = name;
        this.params = params;
        this.body = body;
    }

    @Override
    public String toString() {
        return "function " + name + "(" +
                String.join(", ", params) +
                ") " + body.toString();
    }
}