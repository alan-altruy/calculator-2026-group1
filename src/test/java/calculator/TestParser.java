package calculator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestParser {

    private Calculator calc;

    @BeforeEach
    public void setUp() {
        calc = new Calculator();
    }

    @Test
    public void testParseAddition() {
        Expression e = ExpressionParser.parse("2 + 3");
        assertEquals(5, calc.eval(e));
    }

    @Test
    public void testParseComplexExpression() {
        Expression e = ExpressionParser.parse("2 * (3 + 5) - 4 / 2");
        // 2 * 8 - 2 = 14
        assertEquals(14, calc.eval(e));
    }
}
