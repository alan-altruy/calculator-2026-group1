package calculator.value;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class TestValues {
    
    @Test
    void testThrowsForInterfaceClass() {

        // create a value class that implements Value but does not implement any of the operations, to test that all operations throw an exception
        class DummyValue implements Value {
            private static final String UNSUPPORTED_VALUE_TYPE = "Unsupported value type";  

            @Override
            public Value add(Value other) { throw new ArithmeticException(UNSUPPORTED_VALUE_TYPE); }

            @Override
            public Value sub(Value other) { throw new ArithmeticException(UNSUPPORTED_VALUE_TYPE); }

            @Override
            public Value mul(Value other) { throw new ArithmeticException(UNSUPPORTED_VALUE_TYPE); }

            @Override
            public Value div(Value other) { throw new ArithmeticException(UNSUPPORTED_VALUE_TYPE); }

            @Override
            public Value pow(Value other) { throw new ArithmeticException(UNSUPPORTED_VALUE_TYPE); }

            @Override
            public int intValue() { throw new ArithmeticException(UNSUPPORTED_VALUE_TYPE); }
        }
        Value v = new DummyValue();
        Value other = new DummyValue();

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
        assertThrows(ArithmeticException.class, v::fact);
        assertThrows(ArithmeticException.class, v::abs);
        assertThrows(ArithmeticException.class, v::sinh);
        assertThrows(ArithmeticException.class, v::cosh);
        assertThrows(ArithmeticException.class, v::tanh);
    }
}
