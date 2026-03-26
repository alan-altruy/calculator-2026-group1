package calculator;

//Import Junit5 libraries for unit testing:
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;

class TestEvaluator {

    private Calculator calc;
    private int value1, value2;

    @BeforeEach
    void setUp() {
        calc = new Calculator();
        value1 = 8;
        value2 = 6;
    }

    @Test
    void testEvaluatorMyNumber() {
        assertEquals( value1, calc.eval(new MyNumber(value1)));
    }

    @ParameterizedTest
    @ValueSource(strings = {"*", "+", "/", "-"})
    void testEvaluateOperations(String symbol) {
        List<Expression> params = Arrays.asList(new MyNumber(value1),new MyNumber(value2));
        try {
            //construct another type of operation depending on the input value
            //of the parameterised test
            switch (symbol) {
                case "+"	->	assertEquals( value1 + value2, calc.eval(new Plus(params)));
                case "-"	->	assertEquals( value1 - value2, calc.eval(new Minus(params)));
                case "*"	->	assertEquals( value1 * value2, calc.eval(new Times(params)));
                case "/"	->	assertEquals( value1 / value2, calc.eval(new Divides(params)));
                default		->	fail();
            }
        } catch (IllegalConstruction e) {
            fail();
        }
    }

    @Test
    @SuppressWarnings("PMD.CloseResource")
    void testPrint() {
        List<Expression> params = Arrays.asList(new MyNumber(value1), new MyNumber(value2));
        try {
            Expression e = new Plus(params);
            PrintStream originalOut = System.out;
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            try {
                System.setOut(new PrintStream(output));
                calc.print(e);
            } finally {
                System.setOut(originalOut);
            }

            String printed = output.toString();
            assertTrue(printed.contains("The result of evaluating expression 8 + 6"));
            assertTrue(printed.contains("is: 14."));
        } catch (IllegalConstruction ex) {
            fail();
        }
    }

    @Test
    @SuppressWarnings("PMD.CloseResource")
    void testPrintExpressionDetails() {
        List<Expression> params = Arrays.asList(new MyNumber(value1), new MyNumber(value2));
        try {
            Expression e = new Plus(params);
            PrintStream originalOut = System.out;
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            try {
                System.setOut(new PrintStream(output));
                calc.printExpressionDetails(e);
            } finally {
                System.setOut(originalOut);
            }

            String printed = output.toString();
            assertTrue(printed.contains("The result of evaluating expression 8 + 6"));
            assertTrue(printed.contains("is: 14."));
            assertTrue(printed.contains("It contains 1 levels of nested expressions, 1 operations and 2 numbers."));
        } catch (IllegalConstruction ex) {
            fail();
        }
    }

}
