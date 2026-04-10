package calculator.value;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class IntegerValueTest {

    @Test
    void addAndSub() {
        IntegerValue a = new IntegerValue(5);
        IntegerValue b = new IntegerValue(3);

        assertEquals(new IntegerValue(8), a.add(b));
        assertEquals(new IntegerValue(2), a.sub(b));

        // test with rational
        RationalValue r = new RationalValue(1, 2);
        assertEquals(new RationalValue(11, 2), a.add(r));
        assertEquals(new RationalValue(9, 2), a.sub(r));

        // throws for real and complex
        RealValue real = new RealValue(1.5);
        ComplexValue complex = new ComplexValue(1.0, 2.0);
        assertThrows(ArithmeticException.class, () -> a.add(real));
        assertThrows(ArithmeticException.class, () -> a.sub(real));
        assertThrows(ArithmeticException.class, () -> a.add(complex));
        assertThrows(ArithmeticException.class, () -> a.sub(complex));
    }

    @Test
    void mulAndDiv() {
        IntegerValue a = new IntegerValue(5);
        IntegerValue b = new IntegerValue(3);

        IntegerValue expectedMul = new IntegerValue(15);
        IntegerValue expectedDiv = new IntegerValue(1); // integer division truncates towards zero


        Value mulRes = a.mul(b);
        assertTrue(mulRes instanceof IntegerValue);
        assertEquals(expectedMul, mulRes);

        Value divRes = a.div(b);
        assertTrue(divRes instanceof IntegerValue);
        assertEquals(expectedDiv, divRes);

        // test with rational
        RationalValue r = new RationalValue(1, 2);
        assertEquals(new RationalValue(5, 2), a.mul(r));
        assertEquals(new RationalValue(10, 1), a.div(r));

        // throws for real and complex
        RealValue real = new RealValue(1.5);
        ComplexValue complex = new ComplexValue(1.0, 2.0);
        assertThrows(ArithmeticException.class, () -> a.mul(real));
        assertThrows(ArithmeticException.class, () -> a.div(real));
        assertThrows(ArithmeticException.class, () -> a.mul(complex));
        assertThrows(ArithmeticException.class, () -> a.div(complex));
    }

    @Test
    void testPow() {
        IntegerValue a = new IntegerValue(2);
        IntegerValue b = new IntegerValue(3);
        assertEquals(new IntegerValue(8), a.pow(b));

        // test with rational
        RationalValue r = new RationalValue(1, 2);
        assertEquals(new RationalValue(1), a.pow(r));

        // throws for real and complex
        RealValue real = new RealValue(1.5);
        ComplexValue complex = new ComplexValue(1.0, 2.0);
        assertThrows(ArithmeticException.class, () -> a.pow(real));
        assertThrows(ArithmeticException.class, () -> a.pow(complex));
    }

    @Test
    void testAbsModAndFact() {
        IntegerValue a = new IntegerValue(-5);
        assertEquals(new IntegerValue(5), a.abs());

        // mod and fact should throw for integer values
        IntegerValue b = new IntegerValue(5);
        IntegerValue c = new IntegerValue(2);
        assertEquals(new IntegerValue(1), b.mod(c));
        assertEquals(new IntegerValue(120), b.fact());

        IntegerValue zero = new IntegerValue(0);
        assertEquals(new IntegerValue(1), zero.fact());

        assertThrows(ArithmeticException.class, () -> b.mod(zero));
        assertThrows(ArithmeticException.class, a::fact);
    }
    
    @Test
    void testEquals() {
        IntegerValue a = new IntegerValue(5);
        IntegerValue b = new IntegerValue(5);
        IntegerValue c = new IntegerValue(3);
        assertEquals(a, b);
        assertEquals(c, c);
        assertNotEquals(a, c);

        // should not be equal to different types
        RationalValue r = new RationalValue(5, 1);
        assertNotEquals(a, r);
    }
}
