package calculator.operations.unaryoperations;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Random;

import org.junit.jupiter.api.Test;

import calculator.Main;
import calculator.RandomGenerator;
import calculator.MyNumber;
import calculator.enums.NumberDomain;
import calculator.value.IntegerValue;
import calculator.value.RationalValue;
import calculator.value.RealValue;
import calculator.value.ComplexValue;

class TestRandomOp {

	@Test
	void testIntegerDomainDeterministic() throws Exception {
		NumberDomain prev = Main.getCurrentDomain();
		try {
			Main.setCurrentDomain(NumberDomain.INTEGER);
			int bound = 5;
			long seed = 12345L;
			RandomGenerator.setSeed(seed);

			Random rnd = new Random(seed);
			int expected = rnd.nextInt(bound + 1);

			RandomOp op = new RandomOp(Arrays.asList(new MyNumber(new IntegerValue(bound))));
			IntegerValue res = (IntegerValue) op.unOp(new IntegerValue(bound));
			assertEquals(expected, res.intValue());
		} finally {
			Main.setCurrentDomain(prev);
			RandomGenerator.reset();
		}
	}

	@Test
	void testRationalDomainDeterministic() throws Exception {
		NumberDomain prev = Main.getCurrentDomain();
		try {
			Main.setCurrentDomain(NumberDomain.RATIONAL);
			int bound = 4;
			long seed = 54321L;
			RandomGenerator.setSeed(seed);

			Random rnd = new Random(seed);
			int a = rnd.nextInt(bound + 1);
			int b = rnd.nextInt(bound) + 1;

			RandomOp op = new RandomOp(Arrays.asList(new MyNumber(new IntegerValue(bound))));
			RationalValue res = (RationalValue) op.unOp(new IntegerValue(bound));
			assertTrue(res.getNumerator() >= 0 && res.getNumerator() <= bound);
			assertTrue(res.getDenominator() >= 1 && res.getDenominator() <= bound);
			// expected values should match deterministic RNG
			assertEquals(a, res.getNumerator());
			assertEquals(b, res.getDenominator());
		} finally {
			Main.setCurrentDomain(prev);
			RandomGenerator.reset();
		}
	}

	@Test
	void testRealDomainDeterministic() throws Exception {
		NumberDomain prev = Main.getCurrentDomain();
		try {
			Main.setCurrentDomain(NumberDomain.REAL);
			long seed = 111L;
			RandomGenerator.setSeed(seed);

			Random rnd = new Random(seed);
			double expected = rnd.nextDouble();

			RandomOp op = new RandomOp(Arrays.asList(new MyNumber(new IntegerValue(1))));
			RealValue res = (RealValue) op.unOp(new IntegerValue(1));
			assertEquals(expected, res.getBigDecimal().doubleValue(), 1e-12);
		} finally {
			Main.setCurrentDomain(prev);
			RandomGenerator.reset();
		}
	}

	@Test
	void testComplexDomainDeterministic() throws Exception {
		NumberDomain prev = Main.getCurrentDomain();
		try {
			Main.setCurrentDomain(NumberDomain.COMPLEX);
			long seed = 222L;
			RandomGenerator.setSeed(seed);

			Random rnd = new Random(seed);
			double re = rnd.nextDouble();
			double im = rnd.nextDouble();

			RandomOp op = new RandomOp(Arrays.asList(new MyNumber(new IntegerValue(1))));
			ComplexValue res = (ComplexValue) op.unOp(new IntegerValue(1));
			assertEquals(re, res.getReal().getBigDecimal().doubleValue(), 1e-12);
			assertEquals(im, res.getImag().getBigDecimal().doubleValue(), 1e-12);
		} finally {
			Main.setCurrentDomain(prev);
			RandomGenerator.reset();
		}
	}

	@Test
	void testInvalidBounds() throws Exception {
		NumberDomain prev = Main.getCurrentDomain();
		try {
			Main.setCurrentDomain(NumberDomain.INTEGER);
			RandomOp opInt = new RandomOp(Arrays.asList(new MyNumber(new IntegerValue(-1))));
            IntegerValue toUnOp = new IntegerValue(-1);
			assertThrows(ArithmeticException.class, () -> opInt.unOp(toUnOp));

			Main.setCurrentDomain(NumberDomain.RATIONAL);
			RandomOp opRat = new RandomOp(Arrays.asList(new MyNumber(new IntegerValue(0))));
            IntegerValue toUnOp2 = new IntegerValue(0);
			assertThrows(ArithmeticException.class, () -> opRat.unOp(toUnOp2));
		} finally {
			Main.setCurrentDomain(prev);
			RandomGenerator.reset();
		}
	}

    @Test
    void testPrecedence() throws Exception {
        RandomOp op = new RandomOp(Arrays.asList(new MyNumber(new IntegerValue(1))));
        assertEquals(4, op.getPrecedence());
    }
}
