package calculator.operations;

//Import Junit5 libraries for unit testing:
import static org.junit.jupiter.api.Assertions.*;

import calculator.Expression;
import calculator.MyNumber;
import calculator.enums.Notation;
import calculator.exceptions.IllegalConstruction;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("PMD.TestClassWithoutTestCases")
class TestDivides {

	private final int value1 = 8;
	private final int value2 = 6;
	private Divides op;
	private List<Expression> params;

	@BeforeEach
	void setUp() {
		  params = Arrays.asList(new MyNumber(value1), new MyNumber(value2));
		  try {
		  	op = new Divides(params);
			op.setNotation(Notation.INFIX); // reset the notation to infix (which is the default) before each test
		  }
		  catch(IllegalConstruction e) { fail(); }
	}

	@Test
	void testConstructor1() {
		// It should not be possible to create an expression without null parameter list
		assertThrows(IllegalConstruction.class, () -> op = new Divides(null));
	}

	@SuppressWarnings("AssertBetweenInconvertibleTypes")
	@Test
	void testConstructor2() {
		// A Times expression should not be the same as a Divides expression
		try {
			assertNotSame(new Times(new ArrayList<>()), op);
		} catch (IllegalConstruction e) {
			fail();
		}
	}

	@Test
	void testEquals() {
		// Two similar expressions, constructed separately (and using different constructors) should be equal
		List<Expression> p = Arrays.asList(new MyNumber(value1), new MyNumber(value2));
		try {
			Divides d = new Divides(p, Notation.INFIX);
			assertEquals(op, d);
		}
		catch(IllegalConstruction e) { fail(); }
	}

	@Test
	void testHashCode() {
		// Two similar expressions, constructed separately (and using different constructors) should have the same hashcode
		List<Expression> p = Arrays.asList(new MyNumber(value1), new MyNumber(value2));
		try {
			Divides e = new Divides(p, Notation.INFIX);
			assertEquals(e.hashCode(), op.hashCode());
		}
		catch(IllegalConstruction e) { fail(); }
	}

	@Test
	void testNullParamList() {
		assertThrows(IllegalConstruction.class, () -> new Divides(null));
	}

	@Test
	void testGetPrecedence() {
		assertEquals(2, op.getPrecedence());
	}

}
