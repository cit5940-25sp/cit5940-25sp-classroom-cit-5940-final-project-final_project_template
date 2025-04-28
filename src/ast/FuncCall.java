package ast;

import java.util.List;
import java.util.stream.Collectors;

/** Function call, e.g. `f(a, b)`. */
public class FuncCall extends Expression {
    public final String name;
    public final List<Expression> arguments;

    public FuncCall(String name, List<Expression> arguments) {
        this.name = name;
        this.arguments = arguments;
    }

    @Override
    public String toString() {
        return name + "(" +
                arguments.stream()
                        .map(Expression::toString)
                        .collect(Collectors.joining(", ")) +
                ")";
    }
}