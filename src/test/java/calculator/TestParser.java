package calculator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.lang.reflect.Method;

class TestParser {

    private Calculator calc;

    @BeforeEach
    void setUp() {
        calc = new Calculator();
    }

    @Test
    void testParseAddition() {
        Expression e = ExpressionParser.parse("2 + 3");
        assertEquals(5, calc.eval(e));
    }

    @Test
    void testParseComplexExpression() {
        Expression e = ExpressionParser.parse("2 * (3 + 5) - 4 / 2");
        // 2 * 8 - 2 = 14
        assertEquals(14, calc.eval(e));
    }

    @Test
    void testParsePrefix() {
        Expression e = ExpressionParser.parse("+(4, 5, 6)");
        assertEquals(15, calc.eval(e));
    }

    @Test
    void testParsePostfix() {
        Expression e = ExpressionParser.parse("(4 5 6)+");
        assertEquals(15, calc.eval(e));
    }

    @Test
    void testParseParenthesesPrefixedMultiplication() {
        calc = new Calculator();
        Expression e = ExpressionParser.parse("(4,5,6)");
        // should be parsed as "*(4,5,6)" by ExpressionParser and evaluate to 4 * 5 * 6 = 120
        assertEquals(120, calc.eval(e));
    }
    @Test
    void testParseComplexPrefix() {
        Expression e = ExpressionParser.parse("*(+(4,5,6),+(7,/(5,2,7)),9)");
        // +(4,5,6) = 15
        // /(5,2,7) = 5/2/7 -> 2/7 -> 0. Wait, 5/2 is 2. 2/7 is 0. So 7+0 = 7.
        // *(15, 7, 9) = 15 * 7 * 9 = 945.
        assertNotNull(e);
        assertEquals(945, calc.eval(e));
    }

    @Test
    void testExpressionParserConstructor() throws Exception {
        // ExpressionParser now has a private constructor that prevents instantiation.
        // Verify that attempting to instantiate via reflection fails with the expected cause.
        java.lang.reflect.Constructor<ExpressionParser> ctor = ExpressionParser.class.getDeclaredConstructor();
        // constructor should be private
        assertTrue(java.lang.reflect.Modifier.isPrivate(ctor.getModifiers()));

        // attempting to invoke without making it accessible should throw IllegalAccessException
        assertThrows(IllegalAccessException.class, ctor::newInstance);
    }

    @Test
    void testFinalParsePathIsReachable() throws Exception {
        // Attempt to find an input that makes the private tryParse return null
        // (so ExpressionParser will take the final parse path) while
        // ExpressionParser.parse(input) still returns an Expression.
        Method tryParse = ExpressionParser.class.getDeclaredMethod("tryParse", String.class);
        tryParse.setAccessible(true);

        String[] candidates = new String[] {
            "2 +",
            "4 5 6",
            "2 * (3 +)",
            "2 + )",
            "(4 5 6)",
            "(4,5,)",
            "(,4,5)"
        };

        String found = null;
        for (String input : candidates) {
            Object r1 = tryParse.invoke(null, input);
            Object r2 = null;
            if (input.startsWith("(")) {
                r2 = tryParse.invoke(null, "*" + input);
            }

            if (r1 == null && (input.startsWith("(") ? r2 == null : true)) {
                try {
                    Expression e = ExpressionParser.parse(input);
                    if (e != null) {
                        found = input;
                        break;
                    }
                } catch (Exception ex) {
                    // parse threw, not usable — continue
                }
            }
        }

        assertNotNull(found, "No candidate input triggered the final parse path");
    }
}
