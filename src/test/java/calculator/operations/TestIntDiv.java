package calculator.operations;

import static org.junit.jupiter.api.Assertions.*;

import calculator.Expression;
import calculator.MyNumber;
import calculator.enums.Notation;
import calculator.exceptions.IllegalConstruction;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import calculator.value.RealValue;
import calculator.Calculator;

@SuppressWarnings("PMD.TestClassWithoutTestCases")
class TestIntDiv {

    private final int value1 = 8;
    private final int value2 = 6;
    private IntDiv op;
    private List<Expression> params;

    @BeforeEach
    void setUp() {
        params = Arrays.asList(new MyNumber(value1), new MyNumber(value2));
        try {
            op = new IntDiv(params);
            op.setNotation(Notation.INFIX);
        } catch (IllegalConstruction e) { fail(); }
    }

    @Test
    void testConstructor1() {
        assertThrows(IllegalConstruction.class, () -> op = new IntDiv(null));
    }

    @SuppressWarnings("AssertBetweenInconvertibleTypes")
    @Test
    void testConstructor2() {
        try {
            assertNotSame(new Times(new ArrayList<>()), op);
        } catch (IllegalConstruction e) { fail(); }
    }

    @Test
    void testEquals() {
        List<Expression> p = Arrays.asList(new MyNumber(value1), new MyNumber(value2));
        try {
            IntDiv e = new IntDiv(p, Notation.INFIX);
            assertEquals(op, e);
        } catch (IllegalConstruction e) { fail(); }
    }

    @Test
    void testHashCode() {
        List<Expression> p = Arrays.asList(new MyNumber(value1), new MyNumber(value2));
        try {
            IntDiv e = new IntDiv(p, Notation.INFIX);
            assertEquals(e.hashCode(), op.hashCode());
        } catch (IllegalConstruction e) { fail(); }
    }

    @Test
    void testNullParamList() {
        assertThrows(IllegalConstruction.class, () -> new IntDiv(null));
    }

    @Test
    void testGetPrecedence() {
        assertEquals(2, op.getPrecedence());
    }

    @Test
    void testIntDivWithRealValues() {
        try {
            IntDiv d = new IntDiv(Arrays.asList(new MyNumber(new RealValue(8.0)), new MyNumber(new RealValue(3.0))));
            Calculator calc = new Calculator();
            assertEquals(2, calc.eval(d));
        } catch (IllegalConstruction e) { fail(); }
    }

    @Test
    void testIntDivWithIntegerValuesThrows() {
        try {
            IntDiv d = new IntDiv(Arrays.asList(new MyNumber(8), new MyNumber(3)));
            Calculator calc = new Calculator();
            assertThrows(ArithmeticException.class, () -> calc.eval(d));
        } catch (IllegalConstruction e) { fail(); }
    }

}
