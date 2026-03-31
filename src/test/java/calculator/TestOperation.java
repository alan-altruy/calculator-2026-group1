package calculator;

//Import Junit5 libraries for unit testing:
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("PMD.TestClassWithoutTestCases")
class TestOperation {

	private Operation o;
	private Operation o2;

	@BeforeEach
	void setUp() throws Exception {
		List<Expression> params1 = Arrays.asList(new MyNumber(3), new MyNumber(4), new MyNumber(5));
		List<Expression> params2 = Arrays.asList(new MyNumber(5), new MyNumber(4));
		List<Expression> params3 = Arrays.asList(new Plus(params1), new Minus(params2), new MyNumber(7));
		o = new Divides(params3);
		o2 = new Divides(params3);
	}

	@Test
	void testEquals() {
		assertEquals(o,o2);
		assertNotNull(o);
	}

	@Test
	void testEqualsNullBranch() {
		Boolean condition = Objects.equals(o, null);
		assertFalse(condition);
	}

	@Test
	void testCountDepth() {
		assertEquals(2, o.countDepth());
	}

	@Test
	void testCountOps() {
		assertEquals(3, o.countOps());
	}

	@Test
	void testCountNbs() {
		assertEquals(Integer.valueOf(6), o.countNbs());
	}

	@Test
	void testCountOpsEmptyArgs() throws IllegalConstruction {
		List<Expression> emptyList = new ArrayList<>();

		Operation op = new Plus(emptyList, Notation.INFIX);

		assertEquals(1, op.countOps());
	}

	@Test
	void testToStringInfixParenthesizesLowerPrecedenceChild() throws IllegalConstruction {
		Operation child = new Plus(Arrays.asList(new MyNumber(1), new MyNumber(2)), Notation.INFIX);
		Operation parent = new Times(Arrays.asList(child, new MyNumber(3)), Notation.INFIX);
		assertEquals("( 1 + 2 ) * 3", parent.toString(Notation.INFIX));
	}

	@Test
	void testToStringInfixNoParenthesesForHigherPrecedenceChild() throws IllegalConstruction {
		Operation child = new Times(Arrays.asList(new MyNumber(2), new MyNumber(3)), Notation.INFIX);
		Operation parent = new Plus(Arrays.asList(child, new MyNumber(4)), Notation.INFIX);
		assertEquals("2 * 3 + 4", parent.toString(Notation.INFIX));
	}

	@Test
	void testToStringInfixMinusRightChildSamePrecedenceNeedsParentheses() throws IllegalConstruction {
		Operation rightChild = new Minus(Arrays.asList(new MyNumber(4), new MyNumber(2)), Notation.INFIX);
		Operation parent = new Minus(Arrays.asList(new MyNumber(8), rightChild), Notation.INFIX);
		assertEquals("8 - ( 4 - 2 )", parent.toString(Notation.INFIX));
	}

	@Test
	void testToStringInfixMinusLeftChildSamePrecedenceNoParentheses() throws IllegalConstruction {
		Operation leftChild = new Minus(Arrays.asList(new MyNumber(8), new MyNumber(4)), Notation.INFIX);
		Operation parent = new Minus(Arrays.asList(leftChild, new MyNumber(2)), Notation.INFIX);
		assertEquals("8 - 4 - 2", parent.toString(Notation.INFIX));
	}

	@Test
	void testToStringInfixDividesRightChildSamePrecedenceNeedsParentheses() throws IllegalConstruction {
		Operation rightChild = new Divides(Arrays.asList(new MyNumber(8), new MyNumber(2)), Notation.INFIX);
		Operation parent = new Divides(Arrays.asList(new MyNumber(16), rightChild), Notation.INFIX);
		assertEquals("16 / ( 8 / 2 )", parent.toString(Notation.INFIX));
	}

	@Test
	void testToStringInfixPowerLeftChildSamePrecedenceNeedsParentheses() throws IllegalConstruction {
		Operation leftChild = new Power(Arrays.asList(new MyNumber(2), new MyNumber(3)), Notation.INFIX);
		Operation parent = new Power(Arrays.asList(leftChild, new MyNumber(2)), Notation.INFIX);
		assertEquals("( 2 ** 3 ) ** 2", parent.toString(Notation.INFIX));
	}

	@Test
	void testToStringInfixPowerRightChildSamePrecedenceNoParentheses() throws IllegalConstruction {
		Operation rightChild = new Power(Arrays.asList(new MyNumber(3), new MyNumber(2)), Notation.INFIX);
		Operation parent = new Power(Arrays.asList(new MyNumber(2), rightChild), Notation.INFIX);
		assertEquals("2 ** 3 ** 2", parent.toString(Notation.INFIX));
	}

	@Test
	void testToStringInfixChildWithNonInfixNotationNoParentheses() throws IllegalConstruction {
		Operation child = new Plus(Arrays.asList(new MyNumber(1), new MyNumber(2)), Notation.PREFIX);
		Operation parent = new Plus(Arrays.asList(child, new MyNumber(3)), Notation.INFIX);
		assertEquals("+ (1, 2) + 3", parent.toString(Notation.INFIX));
	}

}
