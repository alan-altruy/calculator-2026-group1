package calculator;

import java.util.Locale;

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
        this.name = name.toLowerCase(Locale.ROOT);
    }

    private static Value resolveValue(String name) {
        switch (name.toLowerCase(Locale.ROOT)) {
            case "pi": return new RealValue(Math.PI);
            case "e": return new RealValue(Math.E);
            case "phi": return new RealValue((1 + Math.sqrt(5)) / 2);
            case "i": return new calculator.value.ComplexValue(0, 1);
            default: throw new IllegalArgumentException("Unknown constant: " + name);
        }
    }

    public String getName() { return name; }

    @Override
    public String toString() {
        return name;
    }
}
