package calculator.operations.unaryoperations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import calculator.*;
import calculator.enums.NumberDomain;
import org.junit.jupiter.api.Test;

class TestCosh {
    @Test
    void testCoshOfZero() {
        Main.setCurrentDomain(NumberDomain.REAL);
        Calculator calc = new Calculator();
        Expression e = ExpressionParser.parse("cosh(0)");
        assertInstanceOf(Cosh.class, e);
        assertEquals(1, calc.eval(e));
    }
}
