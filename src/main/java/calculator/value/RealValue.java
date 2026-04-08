package calculator.value;

import calculator.enums.AngleMode;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * Represents a real number using java.math.BigDecimal.
 * @see Value
 */
public class RealValue implements Value {

    private final BigDecimal value;

    public RealValue(BigDecimal value) {
        this.value = value;
    }

    public RealValue(String value) {
        this.value = new BigDecimal(value);
    }

    public RealValue(double value) {
        this.value = BigDecimal.valueOf(value);
    }

    public BigDecimal getBigDecimal() {
        return value;
    }

    private static MathContext getContext() {
        return new MathContext(calculator.Main.getCurrentPrecision());
    }

    private static RealValue toReal(Value v) {
        if (v instanceof RealValue rv) return rv;
        if (v instanceof RationalValue rat) {
            BigDecimal num = BigDecimal.valueOf(rat.getNumerator());
            BigDecimal den = BigDecimal.valueOf(rat.getDenominator());
            return new RealValue(num.divide(den, getContext()));
        }
        if (v instanceof IntegerValue iv) {
            return new RealValue(BigDecimal.valueOf(iv.intValue()));
        }
        throw new ArithmeticException("Cannot convert to real");
    }

    @Override
    public Value add(Value other) {
        return new RealValue(this.value.add(toReal(other).value, getContext()));
    }

    @Override
    public Value sub(Value other) {
        return new RealValue(this.value.subtract(toReal(other).value, getContext()));
    }

    @Override
    public Value mul(Value other) {
        return new RealValue(this.value.multiply(toReal(other).value, getContext()));
    }

    @Override
    public Value div(Value other) {
        BigDecimal otherVal = toReal(other).value;
        if (otherVal.compareTo(BigDecimal.ZERO) == 0) {
            throw new ArithmeticException("Division by zero");
        }
        return new RealValue(this.value.divide(otherVal, getContext()));
    }

    @Override
    public Value pow(Value other) {
        RealValue otherR = toReal(other);
        double result = Math.pow(this.value.doubleValue(), otherR.value.doubleValue());
        if (Double.isNaN(result) || Double.isInfinite(result)) {
            throw new ArithmeticException("Exponentiation resulted in NaN or Infinity");
        }
        return new RealValue(result);
    }

    @Override
    public Value mod(Value other) {
        BigDecimal otherVal = toReal(other).value;
        if (otherVal.compareTo(BigDecimal.ZERO) == 0) {
            throw new ArithmeticException("Modulo by zero");
        }
        return new RealValue(this.value.remainder(otherVal, getContext()));
    }

    @Override
    public Value intDiv(Value other) {
        BigDecimal otherVal = toReal(other).value;
        if (otherVal.compareTo(BigDecimal.ZERO) == 0) {
            throw new ArithmeticException("Integer division by zero");
        }
        return new RealValue(this.value.divideToIntegralValue(otherVal, getContext()));
    }

    @Override
    public Value ln() {
        if (this.value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ArithmeticException("Logarithm of non-positive number");
        }
        return new RealValue(Math.log(this.value.doubleValue()));
    }

    @Override
    public Value log() {
        if (this.value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ArithmeticException("Logarithm of non-positive number");
        }
        return new RealValue(Math.log10(this.value.doubleValue()));
    }

    private double getAngle() {
        if (calculator.Main.getCurrentAngleMode() == AngleMode.DEG) {
            return Math.toRadians(this.value.doubleValue());
        }
        return this.value.doubleValue();
    }

    private RealValue fromAngle(double rads) {
        if (calculator.Main.getCurrentAngleMode() == AngleMode.DEG) {
            return new RealValue(Math.toDegrees(rads));
        }
        return new RealValue(rads);
    }

    @Override
    public Value sin() {
        return new RealValue(Math.sin(getAngle()));
    }

    @Override
    public Value cos() {
        return new RealValue(Math.cos(getAngle()));
    }

    @Override
    public Value tan() {
        return new RealValue(Math.tan(getAngle()));
    }

    @Override
    public Value arcsin() {
        return fromAngle(Math.asin(this.value.doubleValue()));
    }

    @Override
    public Value arccos() {
        return fromAngle(Math.acos(this.value.doubleValue()));
    }

    @Override
    public Value arctan() {
        return fromAngle(Math.atan(this.value.doubleValue()));
    }

    @Override
    public Value abs() {
        return new RealValue(this.value.abs(getContext()));
    }

    @Override
    public Value fact() {
        int n = this.value.intValueExact(); // must be integer
        if (n < 0) throw new ArithmeticException("Factorial of negative integer");
        BigDecimal fact = BigDecimal.ONE;
        for (int i = 2; i <= n; i++) {
            fact = fact.multiply(BigDecimal.valueOf(i));
        }
        return new RealValue(fact);
    }

    @Override
    public int intValue() {
        return value.intValue();
    }

    @Override
    public String toString() {
        return value.stripTrailingZeros().toPlainString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RealValue rv)) return false;
        return this.value.compareTo(rv.value) == 0;
    }

    @Override
    public int hashCode() {
        return value.stripTrailingZeros().hashCode();
    }
}
