package calculator.value;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class RationalValueTest {

    @Test
    void testConstructor() {
        assertThrows(ArithmeticException.class, () -> new RationalValue(1, 0));

        RationalValue r = new RationalValue(2, -4);
        int num = r.getNumerator();
        int denom = r.getDenominator();
        assertEquals(-1, num);
        assertEquals(2, denom);
    }

    @Test
    void testDivThrows() {
        RationalValue r = new RationalValue(1, 2);
        RationalValue zero = new RationalValue(0, 1);
        assertThrows(ArithmeticException.class, () -> r.div(zero));
    }

    @Test
    void testPowNegative() {
        RationalValue r = new RationalValue(1, 2);
        RationalValue res = (RationalValue) r.pow(new IntegerValue(-1));
        assertEquals(2, res.getNumerator());
        assertEquals(1, res.getDenominator());
    }

    @Test
    void testToString() {
        RationalValue r = new RationalValue(2, 3);
        assertEquals("2/3", r.toString());

        RationalValue r2 = new RationalValue(4, 2);
        assertEquals("2", r2.toString());
    }

    @Test
    void testEquals() {
        RationalValue r1 = new RationalValue(2, 3);
        RationalValue r2 = new RationalValue(4, 6);
        RationalValue r3 = new RationalValue(1, 2);
        RationalValue r4 = new RationalValue(1, 3);
        
        assertEquals(r1, r1);
        assertEquals(r1, r2);
        assertNotEquals(r1, r3);
        assertNotEquals(r4, r3);

        // Should not be equal to an IntegerValue with the same intValue
        IntegerValue i = new IntegerValue(0);
        assertNotEquals(r1, i);
    }

    @Test
    void testHashCode() {
        RationalValue r = new RationalValue(2, 3);
        RationalValue rSame = new RationalValue(4, 6);
        RationalValue rDiff = new RationalValue(1, 2);
        assertEquals(r.hashCode(), rSame.hashCode());
        assertNotEquals(r.hashCode(), rDiff.hashCode());
    }

    @Test
    void testToRationalThroughAdd() {
        RationalValue r = new RationalValue(1, 2);
        IntegerValue i = new IntegerValue(1);
        Value res = r.add(i);
        assertTrue(res instanceof RationalValue);
        RationalValue rRes = (RationalValue) res;
        assertEquals(3, rRes.getNumerator());
        assertEquals(2, rRes.getDenominator());

        // Sould not work with a type that cannot be converted to RationalValue
        RealValue real = new RealValue(1.0);
        assertThrows(ArithmeticException.class, () -> r.add(real));
    }
}
