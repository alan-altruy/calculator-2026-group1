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
class TestMod {

	private final int value1 = 8;
	private final int value2 = 6;
	private Mod op;
	private List<Expression> params;

	@BeforeEach
	void setUp() {
		params = Arrays.asList(new MyNumber(value1), new MyNumber(value2));
		try {
			op = new Mod(params);
			op.setNotation(Notation.INFIX);
		} catch (IllegalConstruction e) { fail(); }
	}

	@Test
	void testConstructor1() {
		assertThrows(IllegalConstruction.class, () -> op = new Mod(null));
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
			Mod e = new Mod(p, Notation.INFIX);
			assertEquals(op, e);
		} catch (IllegalConstruction e) { fail(); }
	}

	@Test
	void testHashCode() {
		List<Expression> p = Arrays.asList(new MyNumber(value1), new MyNumber(value2));
		try {
			Mod e = new Mod(p, Notation.INFIX);
			assertEquals(e.hashCode(), op.hashCode());
		} catch (IllegalConstruction e) { fail(); }
	}

	@Test
	void testNullParamList() {
		assertThrows(IllegalConstruction.class, () -> new Mod(null));
	}

	@Test
	void testGetPrecedence() {
		assertEquals(2, op.getPrecedence());
	}

	@Test
	void testModWithRealValues() {
		try {
			Mod m = new Mod(Arrays.asList(new MyNumber(new RealValue(8.0)), new MyNumber(new RealValue(3.0))));
			Calculator calc = new Calculator();
			assertEquals(2, calc.eval(m));
		} catch (IllegalConstruction e) { fail(); }
	}

	@Test
	void testModWithIntegerValuesThrows() {
		try {
			Mod m = new Mod(Arrays.asList(new MyNumber(8), new MyNumber(3)));
			Calculator calc = new Calculator();
			assertThrows(ArithmeticException.class, () -> calc.eval(m));
		} catch (IllegalConstruction e) { fail(); }
	}

}
