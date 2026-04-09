package calculator.value;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class TestValues {
    
    @Test
    void testThrowsForInterfaceClass() {
        Value v = new IntegerValue(5);
        Value other = new RationalValue(1, 2);

        // Binary operations
        assertThrows(ArithmeticException.class, () -> v.mod(other));
        assertThrows(ArithmeticException.class, () -> v.intDiv(other));

        // Unary operations
        assertThrows(ArithmeticException.class, v::ln);
        assertThrows(ArithmeticException.class, v::log);
        assertThrows(ArithmeticException.class, v::sin);
        assertThrows(ArithmeticException.class, v::cos);
        assertThrows(ArithmeticException.class, v::tan);
        assertThrows(ArithmeticException.class, v::arcsin);
        assertThrows(ArithmeticException.class, v::arccos);
        assertThrows(ArithmeticException.class, v::arctan);
        assertThrows(ArithmeticException.class, v::abs);
        assertThrows(ArithmeticException.class, v::fact);
        assertThrows(ArithmeticException.class, v::sinh);
        assertThrows(ArithmeticException.class, v::cosh);
        assertThrows(ArithmeticException.class, v::tanh);
    }
}
