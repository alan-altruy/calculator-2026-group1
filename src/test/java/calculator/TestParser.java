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

    @Test
    public void testParsePrefix() {
        Expression e = ExpressionParser.parse("+(4, 5, 6)");
        assertEquals(15, calc.eval(e));
    }

    @Test
    public void testParsePostfix() {
        Expression e = ExpressionParser.parse("(4 5 6)+");
        assertEquals(15, calc.eval(e));
    }

    @Test
    public void testParseComplexPrefix() {
        Expression e = ExpressionParser.parse("*(+(4,5,6),+(7,/(5,2,7)),9)");
        // +(4,5,6) = 15
        // /(5,2,7) = 5/2/7 -> 2/7 -> 0. Wait, 5/2 is 2. 2/7 is 0. So 7+0 = 7.
        // *(15, 7, 9) = 15 * 7 * 9 = 945.
        // Let's assert if parse throws no exception first
        assertNotNull(e);
        assertEquals(945, calc.eval(e));
    }
}
