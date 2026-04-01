package calculator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.List;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import org.junit.jupiter.api.Test;

class TestCalculatorVisitorImpl {

    private Calculator calc;

    private CalculatorVisitorImpl visitor;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        visitor = new CalculatorVisitorImpl();
    }

    private MethodHandle getCreateOperationHandle() throws Exception {
        MethodHandles.Lookup lookup = MethodHandles.privateLookupIn(CalculatorVisitorImpl.class, MethodHandles.lookup());
        return lookup.findVirtual(CalculatorVisitorImpl.class, "createOperation",
                MethodType.methodType(Expression.class, String.class, List.class, Notation.class));
    }

    @Test
    void testCreateOperationPowerCase() throws IllegalConstruction {
        List<Expression> args = Arrays.asList(new MyNumber(2), new MyNumber(3));

        Expression result = new Power(args, Notation.INFIX);

        assertNotNull(result);
        assertInstanceOf(Power.class, result);
        assertEquals("2 ** 3", result.toString());
    }

    @Test
    void testCreateOperationCatchIllegalConstructionThrowsIllegalConstruction() {
        assertThrows(IllegalConstruction.class, () -> new Plus(null));
    }

    @Test
    void testCreateOperationDefaultCaseThrowsIllegalArgumentException() throws Throwable {
        List<Expression> args = Arrays.asList(new MyNumber(1), new MyNumber(2));

        MethodHandle mh = getCreateOperationHandle();

        Throwable thrown = org.junit.jupiter.api.Assertions.assertThrows(Throwable.class,
            () -> mh.invoke(visitor, "%", args, Notation.INFIX));

        org.junit.jupiter.api.Assertions.assertInstanceOf(IllegalArgumentException.class, thrown);
        org.junit.jupiter.api.Assertions.assertEquals("Unknown operator: %", thrown.getMessage());
    }

    @Test
    void testCreateOperationCatchIllegalConstructionWrapsRuntimeException() throws Throwable {
        MethodHandle mh = getCreateOperationHandle();

        Throwable thrown = org.junit.jupiter.api.Assertions.assertThrows(Throwable.class,
            () -> mh.invoke(visitor, "+", null, Notation.INFIX));

        org.junit.jupiter.api.Assertions.assertInstanceOf(RuntimeException.class, thrown);
        org.junit.jupiter.api.Assertions.assertEquals("Invalid operation construction", thrown.getMessage());
        org.junit.jupiter.api.Assertions.assertNotNull(thrown.getCause());
        org.junit.jupiter.api.Assertions.assertInstanceOf(IllegalConstruction.class, thrown.getCause());
    }

    @Test
    void testParseImplicitMultiplication() {
        calc = new Calculator();
        Expression e = ExpressionParser.parse("2(3+4)");
        assertInstanceOf(Times.class, e);
        assertEquals(14, calc.eval(e));
    }

    @Test
    void testParsePower() {
        calc = new Calculator();
        Expression e = ExpressionParser.parse("2 ** 3 ** 2");
        assertInstanceOf(Power.class, e);
        assertEquals(512, calc.eval(e));
    }
}