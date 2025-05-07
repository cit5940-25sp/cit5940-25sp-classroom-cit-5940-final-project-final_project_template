import ast.*;
import java.util.Map;
import java.util.HashMap;


public class Interpreter implements ASTVisitor<Object> {

    private final Environment globals = new Environment();  // 顶层作用域（保持不变）
    private Environment environment = globals;              // 当前作用域（会切换）
    private final Map<String, FunctionDecl> functionTable = new HashMap<>();

    // constructor
    public Interpreter(Program program) {
        visitProgram(program);
    }

    @Override
    public Object visitAssignment(Assignment assignment) {
        Object value = assignment.value.accept(this);
        environment.assign(assignment.name, (Integer) value);
        return null;
    }

    @Override
    public Object visitBinaryExpr(BinaryExpr be) {
        int left = (int) be.left.accept(this);
        int right = (int) be.right.accept(this);
        return switch (be.operator) {
            case "+" -> left + right;
            case "*" -> left * right;
            case "-" -> left - right;
            case "/" -> left / right;
            case "=" -> left == right ? 1 : 0;
            case "<" -> left < right ? 1 : 0;
            case "~" -> left != right ? 1 : 0;
            default -> throw new RuntimeException("Unknown operator: " + be.operator);
        };
    }

    @Override
    public Object visitBlock(Block block) {
        for (Statement stmt : block.statements) {
            stmt.accept(this);  // 顺序执行语句
        }
        return null;
    }

    @Override
    public Object visitElifBranch(ElifBranch elifb) {
        return null;
    }

    @Override
    public Object visitExpression(Expression expression) {
        return null;
    }

    @Override
    public Object visitFuncCall(FuncCall fc) {
        return null;
    }

    @Override
    public Object visitFunctionDecl(FunctionDecl fd) {
        Environment previous = environment;
        environment = new Environment(previous);  // 创建新作用域

        try {
            fd.body.accept(this);  // 执行函数体（block）
        } catch (ReturnException ret) {
            return ret.value;  // 支持 return X;
        } finally {
            environment = previous; // 恢复作用域
        }

        return null;
    }

    @Override
    public Object visitIfstmt(IfStmt is) {
        return null;
    }

    @Override
    public Object visitIntegerLiteral(IntegerLiteral il) {
        return il.value;
    }

    @Override
    public Object visitPrintStmt(PrintStmt ps) {
        Object value = ps.expression.accept(this);
        System.out.println(value);
        return null;
    }

    @Override
    public Object visitProgram(Program program) {
        // 1. 把所有函数加入函数表
        for (FunctionDecl func : program.functions) {
            functionTable.put(func.name, func);
        }

        // 2. 找到 entry 函数
        FunctionDecl entry = functionTable.get("entry");
        if (entry == null) {
            throw new RuntimeException("No entry() function found.");
        }

        // 3. 调用 entry()
        return visitFunctionDecl(entry);
    }

    @Override
    public Object visitReturnStmt(ReturnStmt rs) {
        return null;
    }

    @Override
    public Object visitRunWhileStmt(RunWhileStmt rws) {
        return null;
    }

    @Override
    public Object visitStatement(Statement statement) {
        return null;
    }

    @Override
    public Object visitVarDecl(VarDecl vd) {
        Object value = vd.initializer.accept(this); // 递归执行右边表达式
        environment.define(vd.name, (Integer) value);
        return null;
    }

    @Override
    public Object visitVarRef(VarRef vr) {
        return environment.get(vr.name);  // 从作用域查变量
    }

    @Override
    public Object visitWhileStmt(WhileStmt ws) {
        return null;
    }
}
