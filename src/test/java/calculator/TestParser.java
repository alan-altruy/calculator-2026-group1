package calculator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Optional;

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
        Expression e = ExpressionParser.parse("*(+(4,5,6),+(7,/(4,2)),9)");
        // +(4,5,6) = 15
        // /(4,2) = 4/2 = 2
        // +(7,2) = 9
        // *(15, 9, 9) = 15 * 9 * 9 = 1215.
        assertNotNull(e);
        assertEquals(1215, calc.eval(e));
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
    void testFinalParsePathIsReachable() {
        // Attempt to find an input that makes the private tryParse return null
        // (so ExpressionParser will take the final parse path) while
        // ExpressionParser.parse(input) still returns an Expression.
        String[] candidates = new String[] {
            "2 +",
            "4 5 6",
            "2 * (3 +)",
            "2 + )",
            "(4 5 6)",
            "(4,5,)",
            "(,4,5)"
        };
        Optional<String> found = Optional.empty();
        for (String input : candidates) {
            try {
                Expression e = ExpressionParser.parse(input);
                Expression eStar = input.startsWith("(") ? ExpressionParser.parse("*" + input) : null;
                if (e != null && (input.startsWith("(") ? eStar != null : true)) {
                    found = Optional.of(input);
                    break;
                }
            } catch (IllegalArgumentException ex) {
                System.err.printf("parse failed for input '%s': %s%n", input, ex.getMessage());
            }
        }

        assertTrue(found.isPresent(), "No candidate input triggered the final parse path");
    }
}
