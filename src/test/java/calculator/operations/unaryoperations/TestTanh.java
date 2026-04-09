package calculator.operations.unaryoperations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import java.util.List;

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

    @Test
    void testTanhConstructor() {
        Main.setCurrentDomain(NumberDomain.REAL);
        Expression e = ExpressionParser.parse("tanh(10.1)");
        Tanh t = new Tanh(List.of(e));
        assertInstanceOf(Tanh.class, t);
        int prec = t.getPrecedence();
        assertEquals(4, prec);
    }
}
