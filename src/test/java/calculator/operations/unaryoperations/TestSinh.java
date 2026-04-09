package calculator.operations.unaryoperations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import calculator.*;
import calculator.enums.NumberDomain;
import org.junit.jupiter.api.Test;

class TestSinh {
    @Test
    void testSinhOfZero() {
        Main.setCurrentDomain(NumberDomain.REAL);
        Calculator calc = new Calculator();
        Expression e = ExpressionParser.parse("sinh(0)");
        assertInstanceOf(Sinh.class, e);
        assertEquals(0, calc.eval(e));
    }
}
