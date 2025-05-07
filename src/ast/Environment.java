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
}