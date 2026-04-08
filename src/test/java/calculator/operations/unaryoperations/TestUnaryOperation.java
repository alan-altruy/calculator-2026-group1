package calculator.operations.unaryoperations;

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
class TestUnaryOperation {

	private List<Expression> params;

	@BeforeEach
	void setUp() {
		params = new ArrayList<>(Arrays.asList(new MyNumber(5)));
	}

	@Test
	void testConstructorWrongArgCount() {
		assertThrows(IllegalConstruction.class, this::constructAbsWithEmptyList);
		assertThrows(IllegalConstruction.class, this::constructAbsWithEmptyListAndNotation);
    }

	private void constructAbsWithEmptyList() throws IllegalConstruction {
		new Abs(new ArrayList<>());
	}

	private void constructAbsWithEmptyListAndNotation() throws IllegalConstruction {
		new Abs(new ArrayList<>(), Notation.PREFIX);
	}

	@Test
	void testConstructorWithNotation() {
		try {
			Abs u = new Abs(params, Notation.PREFIX);
			assertEquals(Notation.PREFIX, u.getNotation());
		} catch (IllegalConstruction e) { fail(); }
	}

	
}
