package calculator;

import static org.junit.jupiter.api.Assertions.*;

import calculator.value.ComplexValue;
import calculator.value.RealValue;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link MyConstant}.
 * Tests grouped to keep coverage clear and avoid duplication.
 */
class TestMyConstant {

	@Test
	void equalsAndHashCode() {
		MyConstant piLower = new MyConstant("pi");
		MyConstant piUpper = new MyConstant("PI");
		MyConstant e = new MyConstant("e");

		assertEquals(piLower, piUpper);
		assertEquals(piLower.hashCode(), piUpper.hashCode());

		assertNotEquals(piLower, e);
		assertNotEquals(piLower.hashCode(), e.hashCode());

		// different type and null
		assertNotEquals(null, piLower);
		assertNotEquals(null, piLower);
		assertEquals(false, piLower.equals(new Object()));
	}

	@Test
	void valueObjectAndUnknownConstant() {
		MyConstant phi = new MyConstant("phi");
		RealValue expectedPhi = new RealValue((1 + Math.sqrt(5)) / 2);
		assertEquals(expectedPhi, phi.getValueObject());

		MyConstant i = new MyConstant("i");
		ComplexValue expectedI = new ComplexValue(0, 1);
		assertEquals(expectedI, i.getValueObject());

		assertThrows(IllegalArgumentException.class, () -> new MyConstant("this_constant_does_not_exist"));
	}

	@Test
	void nameAndToString() {
		MyConstant c = new MyConstant("PI");
		assertEquals("pi", c.getName());
		assertEquals("pi", c.toString());

		MyConstant phi = new MyConstant("Phi");
		assertEquals("phi", phi.getName());
	}

	@Test
	void equalsIdentityAndNameComparison() {
		MyConstant a = new MyConstant("pi");
		MyConstant same = new MyConstant("PI");

		// identity
		assertEquals(true, a.equals(a));

		// null
		assertNotEquals(null, a);

		// same semantic constant
		assertEquals(true, a.equals(same));
	}

	@Test
	void equalsNameNullBranches() {
		MyConstant a = new MyConstant("pi");
		// create b with same value but null name using package-private constructor
		MyConstant b = new MyConstant(a.getValueObject(), null);

		// With b.name == null and a.name == "pi" -> not equal
		assertEquals(false, a.equals(b));

		// Now create both with null names -> equal
		MyConstant aNull = new MyConstant(a.getValueObject(), null);
		assertEquals(true, aNull.equals(b));

        Object c = null;
        assertEquals(false, a.equals(c)); // a is MyConstant, c is null -> not equal

		// If one null and other non-null -> false
		MyConstant bNonNull = new MyConstant(a.getValueObject(), "pi");
		assertEquals(false, aNull.equals(bNonNull));
	}

	@Test
	void hashCodeUsesNameWhenPresentAndZeroWhenNull() {
		MyConstant withName = new MyConstant("pi");
		int base = withName.getValueObject().hashCode();
		int expectedWithName = 31 * base + withName.getName().hashCode();
		assertEquals(expectedWithName, withName.hashCode());

		MyConstant nullName = new MyConstant(withName.getValueObject(), null);
		int expectedNullName = 31 * base + 0;
		assertEquals(expectedNullName, nullName.hashCode());
	}

}
