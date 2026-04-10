package calculator.value;

/**
 * Represents an integer value in the calculator.
 * 
 * @see Value
 */
public class IntegerValue implements Value {

    private final int value;

    private static final String UNSUPPORTED_VALUE_TYPE = "Unsupported value type";

    /** @param value the integer value */
    public IntegerValue(int value) {
        this.value = value;
    }

    @Override
    public Value add(Value other) {
        if (other instanceof IntegerValue iv)
            return new IntegerValue(value + iv.value);
        if (other instanceof RationalValue)
            return new RationalValue(value, 1).add(other);
        throw new ArithmeticException(UNSUPPORTED_VALUE_TYPE);
    }

    @Override
    public Value sub(Value other) {
        if (other instanceof IntegerValue iv)
            return new IntegerValue(value - iv.value);
        if (other instanceof RationalValue)
            return new RationalValue(value, 1).sub(other);
        throw new ArithmeticException(UNSUPPORTED_VALUE_TYPE);
    }

    @Override
    public Value mul(Value other) {
        if (other instanceof IntegerValue iv)
            return new IntegerValue(value * iv.value);
        if (other instanceof RationalValue)
            return new RationalValue(value, 1).mul(other);
        throw new ArithmeticException(UNSUPPORTED_VALUE_TYPE);
    }

    @Override
    public Value div(Value other) {
        if (other instanceof IntegerValue iv) {
            if (iv.value == 0)
                throw new ArithmeticException("Division by zero");
            return new IntegerValue(value / iv.value);
        }
        if (other instanceof RationalValue)
            return new RationalValue(value, 1).div(other);
        throw new ArithmeticException(UNSUPPORTED_VALUE_TYPE);
    }

    @Override
    public Value pow(Value other) {
        if (other instanceof IntegerValue iv)
            return new IntegerValue((int) Math.pow(value, iv.value));
        if (other instanceof RationalValue)
            return new RationalValue(value, 1).pow(other);
        throw new ArithmeticException(UNSUPPORTED_VALUE_TYPE);
    }

    @Override
    public Value abs() {
        return new IntegerValue(Math.abs(value));
    }

    @Override
    public Value mod(Value other) {
        if (other.intValue() == 0) {
            throw new ArithmeticException("Modulo by zero");
        }
        return new IntegerValue(value % other.intValue());
    }

    @Override
    public Value fact() {
        if (value < 0) {
            throw new ArithmeticException("Factorial of negative number is not defined");
        }
        int result = 1;
        for (int i = 2; i <= value; i++) {
            result *= i;
        }
        return new IntegerValue(result);
    }

    @Override
    public int intValue() {
        return value;
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof IntegerValue iv))
            return false;
        return value == iv.value;
    }

    @Override
    public int hashCode() {
        return value;
    }
}
