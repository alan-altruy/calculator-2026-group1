package calculator.value;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ComplexValueTest {

    @Test
    void addSubMul() {
        ComplexValue a = new ComplexValue(1.0, 2.0);
        ComplexValue b = new ComplexValue(3.0, 4.0);

        assertEquals(new ComplexValue(4.0, 6.0), a.add(b));
        assertEquals(new ComplexValue(-2.0, -2.0), a.sub(b));
        // (1+2i)*(3+4i) = (1*3 - 2*4) + (1*4 + 2*3)i = (-5 + 10i)
        assertEquals(new ComplexValue(-5.0, 10.0), a.mul(b));
    }

    @Test
    void divAndToString() {
        ComplexValue a = new ComplexValue(1.0, 2.0);
        ComplexValue b = new ComplexValue(3.0, 0.0);
        Value res = a.div(b);
        assertTrue(res instanceof ComplexValue);
        ComplexValue c = (ComplexValue) res;
        // dividing by 3 should scale real and imag by 1/3
        assertEquals(1.0/3.0, c.getReal().getBigDecimal().doubleValue(), 1e-9);
        assertEquals(2.0/3.0, c.getImag().getBigDecimal().doubleValue(), 1e-9);

        // division by zero should throw
        ComplexValue zero = new ComplexValue(0.0, 0.0);
        assertThrows(ArithmeticException.class, () -> a.div(zero));

        // check special toString cases
        assertEquals("i", new ComplexValue(0.0,1.0).toString());
        assertEquals("-i", new ComplexValue(0.0, -1.0).toString());
        assertEquals("1 + i", new ComplexValue(1.0,1.0).toString());

        // check normal case
        assertEquals("2 - 3 * i", new ComplexValue(2.0, -3.0).toString()); 

        // check case where real part is zero but imag part is not 1 or -1
        assertEquals("2 * i", new ComplexValue(0.0, 2.0).toString());

        // if imaginary part is zero, should just return real part as string
        assertEquals("2", new ComplexValue(2.0, 0.0).toString());
    }

    @Test
    void testAbs() {
        ComplexValue a = new ComplexValue(3.0, 4.0);
        Value abs = a.abs();
        assertTrue(abs instanceof RealValue);
        assertEquals(5.0, ((RealValue) abs).getBigDecimal().doubleValue(), 1e-10);  
    }

    @Test
    void testEquals() {
        ComplexValue a = new ComplexValue(1.0, 2.0);
        ComplexValue b = new ComplexValue(1.0, 2.0);
        ComplexValue c = new ComplexValue(2.0, 1.0);
        assertEquals(a, b);
        assertEquals(c, c);
        assertNotEquals(a, c);
        assertNotEquals(null, a);
        assertNotEquals(a, new RealValue(1.0));

        // Compare real and imaginary parts separately for coverage
        assertTrue(a.getReal().equals(b.getReal()) && a.getImag().equals(b.getImag()));
        assertFalse(a.getReal().equals(c.getReal()) && a.getImag().equals(c.getImag()));
    }

    @Test
    void testEqualsAndHashCodeContract() {
        ComplexValue a = new ComplexValue(4.0, -3.0);
        ComplexValue b = new ComplexValue(4.0, -3.0);
        ComplexValue diffReal = new ComplexValue(5.0, -3.0);
        ComplexValue diffImag = new ComplexValue(4.0, 3.0);

        // equals symmetry and hashCode consistency
        assertEquals(a, b);
        assertEquals(b, a);
        assertEquals(a.hashCode(), b.hashCode());

        // inequality when one component differs
        assertNotEquals(a, diffReal);
        assertNotEquals(a, diffImag);
        assertNotEquals(a.hashCode(), diffReal.hashCode());
    }

    @Test
    void operationsNotSupported() {
        ComplexValue a = new ComplexValue(1.0, 2.0);
        IntegerValue i = new IntegerValue(1);
        assertThrows(ArithmeticException.class, () -> a.intDiv(i));
        assertThrows(ArithmeticException.class, () -> a.mod(i));
        assertThrows(ArithmeticException.class, a::fact);
        assertThrows(ArithmeticException.class, a::intValue);
    }

    @Test
    void operationsNotImplemented() {
        ComplexValue a = new ComplexValue(1.0, 2.0);
        IntegerValue i = new IntegerValue(1);
        assertThrows(ArithmeticException.class, a::ln);
        assertThrows(ArithmeticException.class, a::log);
        assertThrows(ArithmeticException.class, () -> a.pow(i));
        assertThrows(ArithmeticException.class, a::sin);
        assertThrows(ArithmeticException.class, a::cos);
        assertThrows(ArithmeticException.class, a::tan);
        assertThrows(ArithmeticException.class, a::arcsin);
        assertThrows(ArithmeticException.class, a::arccos);
        assertThrows(ArithmeticException.class, a::arctan);
    }

    @Test
    void testToComplexValueThroughDivision() {
        ComplexValue a = new ComplexValue(1.0, 2.0);

        // Test with real divisor
        RealValue b = new RealValue(2.0);
        assertTrue(b instanceof RealValue);
        Value res = a.div(b);
        assertTrue(res instanceof ComplexValue);
        ComplexValue c = (ComplexValue) res;
        assertEquals(0.5, c.getReal().getBigDecimal().doubleValue(), 1e-9);
        assertEquals(1.0, c.getImag().getBigDecimal().doubleValue(), 1e-9);

        // Test with integer divisor
        IntegerValue d = new IntegerValue(2);
        assertTrue(d instanceof IntegerValue);
        res = a.div(d);
        assertTrue(res instanceof ComplexValue);
        c = (ComplexValue) res;
        assertEquals(0.5, c.getReal().getBigDecimal().doubleValue(), 1e-9);
        assertEquals(1.0, c.getImag().getBigDecimal().doubleValue(), 1e-9);

        // Test with rational divisor
        RationalValue e = new RationalValue(1, 2);
        assertTrue(e instanceof RationalValue);
        res = a.div(e);
        assertTrue(res instanceof ComplexValue);
        c = (ComplexValue) res;
        assertEquals(2.0, c.getReal().getBigDecimal().doubleValue(), 1e-9);
        assertEquals(4.0, c.getImag().getBigDecimal().doubleValue(), 1e-9);

        // Test with illegal divisor (should throw)
        class NotANumber implements Value {
            @Override public Value add(Value other) { throw new UnsupportedOperationException(); }
            @Override public Value sub(Value other) { throw new UnsupportedOperationException(); }
            @Override public Value mul(Value other) { throw new UnsupportedOperationException(); }
            @Override public Value div(Value other) { throw new UnsupportedOperationException(); }
            @Override public Value pow(Value other) { throw new UnsupportedOperationException(); }
            @Override public int intValue() { throw new UnsupportedOperationException(); }
        }
        NotANumber notANumber = new NotANumber();
        assertThrows(ArithmeticException.class, () -> a.div(notANumber));

    }
}
