package calculator.operations.unaryoperations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import java.util.List;

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

    @Test
    void testCoshConstructor() {
        Main.setCurrentDomain(NumberDomain.REAL);
        Expression e = ExpressionParser.parse("cosh(10.1)");
        Cosh c = new Cosh(List.of(e));
        assertInstanceOf(Cosh.class, c);
        int prec = c.getPrecedence();
        assertEquals(4, prec);
    }
}
