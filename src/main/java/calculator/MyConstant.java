package calculator;

import calculator.value.RealValue;
import calculator.value.Value;

/**
 * Represents a mathematical constant like pi, e, phi.
 * Extends MyNumber to integrate transparently with Visitors.
 */
public class MyConstant extends MyNumber {
    private final String name;

    public MyConstant(String name) {
        super(resolveValue(name));
        this.name = name.toLowerCase();
    }

    private static Value resolveValue(String name) {
        switch (name.toLowerCase()) {
            case "pi": return new RealValue(Math.PI);
            case "e": return new RealValue(Math.E);
            case "phi": return new RealValue((1 + Math.sqrt(5)) / 2);
            default: throw new IllegalArgumentException("Unknown constant: " + name);
        }
    }

    public String getName() { return name; }

    @Override
    public String toString() {
        return name;
    }
}
