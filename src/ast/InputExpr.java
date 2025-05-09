package ast;

public class InputExpr extends Expression {

    public int evaluate(Environment env) {
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        return scanner.nextInt();
    }

    @Override
    public String toString() {
        return "input";
    }
}
