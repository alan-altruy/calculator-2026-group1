package calculator.operations.unaryoperations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import calculator.*;
import calculator.enums.NumberDomain;
import org.junit.jupiter.api.Test;

class TestTanh {
    @Test
    void testTanhOfZero() {
        Main.setCurrentDomain(NumberDomain.REAL);
        Calculator calc = new Calculator();
        Expression e = ExpressionParser.parse("tanh(0)");
        assertInstanceOf(Tanh.class, e);
        assertEquals(0, calc.eval(e));
    }
}
