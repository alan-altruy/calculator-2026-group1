package calculator.value;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * Represents a complex number defined as a + bi
 * where a is the real part and b is the imaginary part.
 * Operations are implemented according to standard complex arithmetic rules.
 */
public class ComplexValue implements Value {

    private final RealValue real;
    private final RealValue imag;

    public ComplexValue(double real, double imag) {
        this.real = new RealValue(real);
        this.imag = new RealValue(imag);
    }

    public ComplexValue(RealValue real, RealValue imag) {
        this.real = real;
        this.imag = imag;
    }

    public RealValue getReal() {
        return real;
    }

    public RealValue getImag() {
        return imag;
    }

    private static ComplexValue toComplex(Value v) {
        if (v instanceof ComplexValue cv) return cv;
        if (v instanceof RealValue rv) return new ComplexValue(rv, new RealValue(0.0));
        if (v instanceof RationalValue rat) {
            BigDecimal num = BigDecimal.valueOf(rat.getNumerator());
            BigDecimal den = BigDecimal.valueOf(rat.getDenominator());
            return new ComplexValue(new RealValue(num.divide(den, MathContext.DECIMAL64)), new RealValue(0.0));
        }
        if (v instanceof IntegerValue iv) {
            return new ComplexValue(new RealValue(iv.intValue()), new RealValue(0.0));
        }
        throw new ArithmeticException("Cannot convert to ComplexValue");
    }

    private static MathContext getContext() {
        return new MathContext(calculator.Main.getCurrentPrecision());
    }

    @Override
    public Value add(Value other) {
        ComplexValue o = toComplex(other);
        return new ComplexValue((RealValue) this.real.add(o.real), (RealValue) this.imag.add(o.imag));
    }

    @Override
    public Value sub(Value other) {
        ComplexValue o = toComplex(other);
        return new ComplexValue((RealValue) this.real.sub(o.real), (RealValue) this.imag.sub(o.imag));
    }

    @Override
    public Value mul(Value other) {
        ComplexValue o = toComplex(other);
        // (a + bi)(c + di) = (ac - bd) + (ad + bc)i
        Value ac = this.real.mul(o.real);
        Value bd = this.imag.mul(o.imag);
        Value ad = this.real.mul(o.imag);
        Value bc = this.imag.mul(o.real);

        return new ComplexValue((RealValue) ac.sub(bd), (RealValue) ad.add(bc));
    }

    @Override
    public Value div(Value other) {
        ComplexValue o = toComplex(other);
        // (a + bi)/(c + di) = ((ac + bd) + (bc - ad)i) / (c^2 + d^2)
        BigDecimal c = o.real.getBigDecimal();
        BigDecimal d = o.imag.getBigDecimal();
        BigDecimal denom = c.pow(2, getContext()).add(d.pow(2, getContext()), getContext());

        if (denom.compareTo(BigDecimal.ZERO) == 0) {
            throw new ArithmeticException("Division by zero (complex)");
        }

        Value ac = this.real.mul(o.real);
        Value bd = this.imag.mul(o.imag);
        Value bc = this.imag.mul(o.real);
        Value ad = this.real.mul(o.imag);

        RealValue numReal = (RealValue) ac.add(bd);
        RealValue numImag = (RealValue) bc.sub(ad);

        BigDecimal newReal = numReal.getBigDecimal().divide(denom, getContext());
        BigDecimal newImag = numImag.getBigDecimal().divide(denom, getContext());

        return new ComplexValue(new RealValue(newReal), new RealValue(newImag));
    }

    @Override
    public Value pow(Value other) {
        throw new ArithmeticException("Power operation not implemented for complex numbers");
    }

    @Override
    public Value mod(Value other) {
        throw new ArithmeticException("Modulo not supported for complex numbers");
    }

    @Override
    public Value intDiv(Value other) {
        throw new ArithmeticException("Integer division not supported for complex numbers");
    }

    @Override
    public Value ln() {
        throw new ArithmeticException("Natural Log not implemented for complex numbers");
    }

    @Override
    public Value log() {
        throw new ArithmeticException("Log10 not implemented for complex numbers");
    }

    @Override
    public Value sin() {
        throw new ArithmeticException("Sin not implemented for complex numbers");
    }

    @Override
    public Value cos() {
        throw new ArithmeticException("Cos not implemented for complex numbers");
    }

    @Override
    public Value tan() {
        throw new ArithmeticException("Tan not implemented for complex numbers");
    }

    @Override
    public Value arcsin() {
        throw new ArithmeticException("ArcSin not implemented for complex numbers");
    }

    @Override
    public Value arccos() {
        throw new ArithmeticException("ArcCos not implemented for complex numbers");
    }

    @Override
    public Value arctan() {
        throw new ArithmeticException("ArcTan not implemented for complex numbers");
    }

    @Override
    public Value abs() {
        // Absolute value (magnitude) of a + bi is sqrt(a^2 + b^2)
        BigDecimal a = real.getBigDecimal();
        BigDecimal b = imag.getBigDecimal();
        BigDecimal a2 = a.pow(2, getContext());
        BigDecimal b2 = b.pow(2, getContext());
        double mag = Math.sqrt(a2.add(b2, getContext()).doubleValue());
        return new RealValue(mag);
    }

    @Override
    public Value fact() {
        throw new ArithmeticException("Factorial not supported for complex numbers");
    }

    @Override
    public int intValue() {
        throw new ArithmeticException("Cannot convert complex number to integer");
    }

    @Override
    public String toString() {
        BigDecimal a = real.getBigDecimal().stripTrailingZeros();
        BigDecimal b = imag.getBigDecimal().stripTrailingZeros();
        
        if (b.compareTo(BigDecimal.ZERO) == 0) return a.toPlainString();
        if (a.compareTo(BigDecimal.ZERO) == 0) {
            if (b.compareTo(BigDecimal.ONE) == 0) return "i";
            if (b.compareTo(BigDecimal.ONE.negate()) == 0) return "-i";
            return b.toPlainString() + " * i";
        }
        
        String bStr = b.abs().compareTo(BigDecimal.ONE) == 0 ? "i" : b.abs().toPlainString() + " * i";
        String sign = b.compareTo(BigDecimal.ZERO) > 0 ? " + " : " - ";
        return a.toPlainString() + sign + bStr;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ComplexValue cv)) return false;
        return this.real.equals(cv.real) && this.imag.equals(cv.imag);
    }

    @Override
    public int hashCode() {
        return 31 * real.hashCode() + imag.hashCode();
    }
}
