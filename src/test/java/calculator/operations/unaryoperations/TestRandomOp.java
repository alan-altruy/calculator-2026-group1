package calculator.operations.unaryoperations;

import static org.junit.jupiter.api.Assertions.*;

import java.security.SecureRandom;
import java.util.Arrays;

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

			RandomOp op = new RandomOp(Arrays.asList(new MyNumber(new IntegerValue(bound))));
			IntegerValue res = (IntegerValue) op.unOp(new IntegerValue(bound));
            assertTrue(res.intValue() >= 0 && res.intValue() < bound);
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

			SecureRandom rnd = new SecureRandom();
            rnd.setSeed(seed);

			RandomOp op = new RandomOp(Arrays.asList(new MyNumber(new IntegerValue(bound))));
			RationalValue res = (RationalValue) op.unOp(new IntegerValue(bound));
			assertTrue(res.getNumerator() >= 0 && res.getNumerator() <= bound);
			assertTrue(res.getDenominator() >= 1 && res.getDenominator() <= bound);
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

			RandomOp op = new RandomOp(Arrays.asList(new MyNumber(new IntegerValue(1))));
			RealValue res = (RealValue) op.unOp(new IntegerValue(1));
			assertTrue(res.getBigDecimal().doubleValue() >= 0.0 && res.getBigDecimal().doubleValue() < 1.0);
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

			RandomOp op = new RandomOp(Arrays.asList(new MyNumber(new IntegerValue(1))));
			ComplexValue res = (ComplexValue) op.unOp(new IntegerValue(1));

            assertTrue(res.getReal().getBigDecimal().doubleValue() >= 0.0 && res.getReal().getBigDecimal().doubleValue() < 1.0);
            assertTrue(res.getImag().getBigDecimal().doubleValue() >= 0.0 && res.getImag().getBigDecimal().doubleValue() < 1.0);

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

    @Test
    void testGetSecureRandom() {
        SecureRandom rng1 = RandomGenerator.getSecureRandom();
        SecureRandom rng2 = RandomGenerator.getSecureRandom();
        assertNotNull(rng1);
        assertNotNull(rng2);
        assertNotSame(rng1, rng2); // should return a new instance each time
    }
}
