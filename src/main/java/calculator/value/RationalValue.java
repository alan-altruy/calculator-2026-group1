package calculator.value;

import java.util.Objects;

/**
 * Represents a rational number (fraction) as numerator/denominator.
 * Automatically simplifies using GCD and normalizes sign.
 * @see Value
 */
public class RationalValue implements Value {

    private final int numerator;
    private final int denominator;

    /**
     * @param numerator the numerator
     * @param denominator the denominator (must not be zero)
     */
    public RationalValue(int numerator, int denominator) {
        if (denominator == 0) throw new ArithmeticException("Division by zero");
        if (denominator < 0) { numerator = -numerator; denominator = -denominator; }
        int g = gcd(Math.abs(numerator), denominator);
        this.numerator = numerator / g;
        this.denominator = denominator / g;
    }

    /** @param value an integer value (denominator = 1) */
    public RationalValue(int value) { this(value, 1); }

    public int getNumerator() { return numerator; }
    public int getDenominator() { return denominator; }

    private static int gcd(int a, int b) {
        while (b != 0) { int t = b; b = a % b; a = t; }
        return a;
    }

    private static RationalValue toRational(Value v) {
        if (v instanceof RationalValue rv) return rv;
        if (v instanceof IntegerValue) return new RationalValue(v.intValue(), 1);
        throw new ArithmeticException("Cannot convert to rational");
    }

    @Override
    public Value add(Value other) {
        RationalValue r = toRational(other);
        return new RationalValue(
            this.numerator * r.denominator + r.numerator * this.denominator,
            this.denominator * r.denominator);
    }

    @Override
    public Value sub(Value other) {
        RationalValue r = toRational(other);
        return new RationalValue(
            this.numerator * r.denominator - r.numerator * this.denominator,
            this.denominator * r.denominator);
    }

    @Override
    public Value mul(Value other) {
        RationalValue r = toRational(other);
        return new RationalValue(this.numerator * r.numerator, this.denominator * r.denominator);
    }

    @Override
    public Value div(Value other) {
        RationalValue r = toRational(other);
        if (r.numerator == 0) throw new ArithmeticException("Division by zero");
        return new RationalValue(this.numerator * r.denominator, this.denominator * r.numerator);
    }

    @Override
    public Value pow(Value other) {
        int exp = other.intValue();
        if (exp < 0) {
            return new RationalValue(
                (int) Math.pow(denominator, -exp), (int) Math.pow(numerator, -exp));
        }
        return new RationalValue(
            (int) Math.pow(numerator, exp), (int) Math.pow(denominator, exp));
    }

    @Override public int intValue() { return numerator / denominator; }

    @Override
    public String toString() {
        if (denominator == 1) return Integer.toString(numerator);
        return numerator + "/" + denominator;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RationalValue rv)) return false;
        return numerator == rv.numerator && denominator == rv.denominator;
    }

    @Override public int hashCode() { return Objects.hash(numerator, denominator); }
}
