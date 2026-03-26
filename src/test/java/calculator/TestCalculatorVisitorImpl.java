package calculator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TestCalculatorVisitorImpl {

    private CalculatorVisitorImpl visitor;
    private Calculator calc;
    private Method createOperationMethod;

    @BeforeEach
    void setUp() throws NoSuchMethodException {
        visitor = new CalculatorVisitorImpl();
        createOperationMethod = CalculatorVisitorImpl.class.getDeclaredMethod(
                "createOperation", String.class, List.class, Notation.class);
        createOperationMethod.setAccessible(true);
    }

    private Expression invokeCreateOperation(String op, List<Expression> args, Notation notation)
            throws InvocationTargetException, IllegalAccessException {
        return (Expression) createOperationMethod.invoke(visitor, op, args, notation);
    }

    @Test
    void testCreateOperationPowerCase() throws InvocationTargetException, IllegalAccessException {
        List<Expression> args = Arrays.asList(new MyNumber(2), new MyNumber(3));

        Expression result = invokeCreateOperation("**", args, Notation.INFIX);

        assertNotNull(result);
        assertInstanceOf(Power.class, result);
        assertEquals("2 ** 3", result.toString());
    }

    @Test
    void testCreateOperationDefaultCaseThrowsIllegalArgumentException() {
        List<Expression> args = Arrays.asList(new MyNumber(1), new MyNumber(2));

        InvocationTargetException thrown = assertThrows(InvocationTargetException.class,
                () -> invokeCreateOperation("%", args, Notation.INFIX));

        assertInstanceOf(IllegalArgumentException.class, thrown.getCause());
        assertEquals("Unknown operator: %", thrown.getCause().getMessage());
    }

    @Test
    void testCreateOperationCatchIllegalConstructionWrapsRuntimeException() {
        InvocationTargetException thrown = assertThrows(InvocationTargetException.class,
                () -> invokeCreateOperation("+", null, Notation.INFIX));

        assertInstanceOf(RuntimeException.class, thrown.getCause());
        assertEquals("Invalid operation construction", thrown.getCause().getMessage());
        assertNotNull(thrown.getCause().getCause());
        assertInstanceOf(IllegalConstruction.class, thrown.getCause().getCause());
    }

    @Test
    public void testParseImplicitMultiplication() {
        calc = new Calculator();
        Expression e = ExpressionParser.parse("2(3+4)");
        assertInstanceOf(Times.class, e);
        assertEquals(14, calc.eval(e));
    }

    @Test
    public void testParsePower() {
        calc = new Calculator();
        Expression e = ExpressionParser.parse("2 ** 3 ** 2");
        assertInstanceOf(Power.class, e);
        assertEquals(512, calc.eval(e));
    }
}