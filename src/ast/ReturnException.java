package ast;

// 1. 用来在 visitReturnStmt() 中终止当前函数执行并携带返回值
// 2. 用来在 visitFunctionDecl() 中用 try { ... } catch (ReturnException) 来接收
// PS：运行时行为的一部分，不是语法树结构的一部分
public class ReturnException extends RuntimeException {
    public final int value;

    public ReturnException(int value) {
        this.value = value;
    }
}
