package calculator.visitor;

//Import Junit5 libraries for unit testing:
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import calculator.Calculator;
import calculator.operations.Divides;
import calculator.Expression;
import calculator.exceptions.IllegalConstruction;
import calculator.operations.Minus;
import calculator.MyNumber;
import calculator.operations.Plus;
import calculator.operations.Times;
import calculator.operations.unaryoperations.Sin;
import calculator.value.RealValue;
import visitor.Evaluator;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.StreamHandler;

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
    @SuppressWarnings("PMD.CloseResource") // We intentionally do not close the PrintStream since it wraps System.out
    void testPrint() {
        List<Expression> params = Arrays.asList(new MyNumber(value1), new MyNumber(value2));
        try {
            Expression e = new Plus(params);
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            Logger logger = Logger.getLogger(Calculator.class.getName());
            Formatter fmt = new Formatter() {
                @Override
                public String format(LogRecord logRecord) {
                    return logRecord.getMessage() + System.lineSeparator();
                }
            };
            StreamHandler sh = new StreamHandler(output, fmt);
            Level previousLevel = logger.getLevel();
            logger.setLevel(Level.INFO);
            logger.addHandler(sh);
            try {
                calc.print(e);
                sh.flush();
            } finally {
                logger.removeHandler(sh);
                logger.setLevel(previousLevel);
            }

            String printed = output.toString();
            assertTrue(printed.contains("The result of evaluating expression 8 + 6"));
            assertTrue(printed.contains("is: 14."));
        } catch (IllegalConstruction ex) {
            fail();
        }
    }

    @Test
    @SuppressWarnings("PMD.CloseResource") // We intentionally do not close the PrintStream since it wraps System.out
    void testPrintExpressionDetails() {
        List<Expression> params = Arrays.asList(new MyNumber(value1), new MyNumber(value2));
        try {
            Expression e = new Plus(params);
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            Logger logger = Logger.getLogger(Calculator.class.getName());
            Formatter fmt = new Formatter() {
                @Override
                public String format(LogRecord logRecord) {
                    return logRecord.getMessage() + System.lineSeparator();
                }
            };
            StreamHandler sh = new StreamHandler(output, fmt);
            Level previousLevel = logger.getLevel();
            logger.setLevel(Level.INFO);
            logger.addHandler(sh);
            try {
                calc.printExpressionDetails(e);
                sh.flush();
            } finally {
                logger.removeHandler(sh);
                logger.setLevel(previousLevel);
            }

            String printed = output.toString();
            assertTrue(printed.contains("The result of evaluating expression 8 + 6"));
            assertTrue(printed.contains("is: 14."));
            assertTrue(printed.contains("It contains 1 levels of nested expressions, 1 operations and 2 numbers."));
        } catch (IllegalConstruction ex) {
            fail();
        }
    }

    @Test
    void singleArgBranchEvaluatesWithReal() {
        try {
            Sin s = new Sin(List.of(new MyNumber(new RealValue(0.0))));
            Evaluator ev = new Evaluator();
            s.accept(ev);
            assertNotNull(ev.getResult());
            assertEquals(0, ev.getResult().intValue());
        } catch (IllegalConstruction e) { fail(); }
    }
}
