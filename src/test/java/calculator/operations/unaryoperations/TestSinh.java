package calculator.operations.unaryoperations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import java.util.List;

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

    @Test
    void testSinhConstructor() {
        Main.setCurrentDomain(NumberDomain.REAL);
        Expression e = ExpressionParser.parse("sinh(10.1)");
        Sinh s = new Sinh(List.of(e));
        assertInstanceOf(Sinh.class, s);
        int prec = s.getPrecedence();
        assertEquals(4, prec);
    }
}
