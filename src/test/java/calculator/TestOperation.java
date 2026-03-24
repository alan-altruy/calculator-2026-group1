package calculator;

//Import Junit5 libraries for unit testing:
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import java.util.Arrays;
import java.util.List;

class TestOperation {

	private Operation o;
	private Operation o2;

	static class TestConstructorOperation extends Operation{
		public TestConstructorOperation(List<Expression> param) throws IllegalConstruction {
			super(param);
		}
		@Override
		public int op(int l, int r) {
			return 0;
		}
	}

	@BeforeEach
	void setUp() throws Exception {
		List<Expression> params1 = Arrays.asList(new MyNumber(3), new MyNumber(4), new MyNumber(5));
		List<Expression> params2 = Arrays.asList(new MyNumber(5), new MyNumber(4));
		List<Expression> params3 = Arrays.asList(new Plus(params1), new Minus(params2), new MyNumber(7));
		o = new Divides(params3);
		o2 = new Divides(params3);


	}

	@Test
	void testNotNull(){assertNotEquals(null, o);assertNotEquals(null, o2);}
	@Test
	void testEquals() {
		assertEquals(o,o2);
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
	void testConstructorWithListOnly() throws IllegalConstruction {
		List<Expression> list = List.of();
		Operation op = new TestConstructorOperation(list);
		assertNotNull(op);
	}
}
