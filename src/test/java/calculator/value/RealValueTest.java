package calculator.value;

import static org.junit.jupiter.api.Assertions.*;

import calculator.enums.AngleMode;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class RealValueTest {

    @AfterEach
    void restoreDefaults() {
        calculator.Main.setCurrentAngleMode(AngleMode.RAD);
        calculator.Main.setCurrentPrecision(10);
    }

    @Test
    void addAndDiv() {
        RealValue a = new RealValue("1.5");
        RationalValue r = new RationalValue(1, 2);
        Value sum = a.add(r);
        assertTrue(sum instanceof RealValue);
        assertEquals(2.0, ((RealValue) sum).getBigDecimal().doubleValue(), 1e-10);

        RealValue three = new RealValue(3.0);
        assertEquals(0.5, ((RealValue) three.div(new RealValue(6.0))).getBigDecimal().doubleValue(), 1e-10);

        // division by zero should throw
        RealValue zero = new RealValue(0.0);
        assertThrows(ArithmeticException.class, () -> three.div(zero));
    }

    @Test
    void lnNegativeThrows() {
        RealValue r = new RealValue(-1.0);
        assertThrows(ArithmeticException.class, r::ln);
    }

    @Test
    void trigInDegrees() {
        calculator.Main.setCurrentAngleMode(AngleMode.DEG);
        RealValue thirty = new RealValue(30.0);
        Value sin = thirty.sin();
        assertEquals(0.5, ((RealValue) sin).getBigDecimal().doubleValue(), 1e-10);
    }

    @Test
    void testToRealThroughDiv() {
        RealValue a = new RealValue(1.0);
        IntegerValue b = new IntegerValue(2);
        Value res = a.div(b);
        assertTrue(res instanceof RealValue);
        assertEquals(0.5, ((RealValue) res).getBigDecimal().doubleValue(), 1e-10);

        // test with not allowable types

        ComplexValue c = new ComplexValue(1.0, 1.0);
        assertThrows(ArithmeticException.class, () -> a.div(c));
    }

    @Test
    void testPow() {
        RealValue r = new RealValue(2.0);
        RealValue exp = new RealValue(3.0);
        Value res = r.pow(exp);
        assertTrue(res instanceof RealValue);
        assertEquals(8.0, ((RealValue) res).getBigDecimal().doubleValue(), 1e-10);

        // test result nan or infinite cases
        RealValue zero = new RealValue(0.0);
        RealValue neg = new RealValue(-1.0);
        RealValue sqrt = new RealValue(0.5);
        assertThrows(ArithmeticException.class, () -> zero.pow(neg));
        assertThrows(ArithmeticException.class, () -> neg.pow(sqrt));
    }

    @Test
    void testThrowArithmeticExceptionForModIntDivLog() {
        RealValue r = new RealValue(1.0);
        RealValue zero = new RealValue(0.0);
        assertThrows(ArithmeticException.class, () -> r.mod(zero));
        assertThrows(ArithmeticException.class, () -> r.intDiv(zero));
        assertThrows(ArithmeticException.class, zero::log);
    }

    @Test
    void testFromAngleTroughArcsin() {
        calculator.Main.setCurrentAngleMode(AngleMode.DEG);
        RealValue r = new RealValue(0.5);
        Value arcsin = r.arcsin();
        assertTrue(arcsin instanceof RealValue);
        assertEquals(30.0, ((RealValue) arcsin).getBigDecimal().doubleValue(), 1e-10); 
    }

    @Test
    void testFactThrowsNegative() {
        RealValue r = new RealValue(-1.0);
        assertThrows(ArithmeticException.class, r::fact);
    }

    @Test
    void testEqualsAndHashCode() {
        RealValue a = new RealValue(1.0);
        RealValue b = new RealValue(1.0);
        RealValue c = new RealValue(2.0);
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, c);
        assertNotEquals(a.hashCode(), c.hashCode());

        // also test not equals with different types
        RationalValue r = new RationalValue(1, 1);
        assertNotEquals(a, r);
    }
}
