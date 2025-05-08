package ast;

import java.util.HashMap;
import java.util.Map;

public class Environment {
    private final Map<String, Integer> values = new HashMap<>();
    private final Environment parent;

    public Environment() {
        this.parent = null;
    }

    public Environment(Environment parent) {
        this.parent = parent;
    }

    public void define(String name, Integer value) {
        values.put(name, value);
    }

    public void assign(String name, Integer value) {
        if (values.containsKey(name)) {
            values.put(name, value);
        } else if (parent != null) {
            parent.assign(name, value);
        } else {
            throw new RuntimeException("Undefined variable '" + name + "'");
        }
    }

    public Integer get(String name) {
        if (values.containsKey(name)) {
            return values.get(name);
        } else if (parent != null) {
            return parent.get(name);
        } else {
            throw new RuntimeException("Undefined variable '" + name + "'");
        }
    }
    // 函数是直接存在 Interpreter 的 functionTable 里管理的，和变量作用域是分开的，
    // 所以这里的 defineFunction 和 getFunction 实际不会被用到。
    // public void defineFunction(String name, FunctionDecl func) {}
    // public FunctionDecl getFunction(String name) { return null; }
}