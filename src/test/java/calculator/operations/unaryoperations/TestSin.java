package calculator.operations.unaryoperations;

import calculator.MyNumber;
import calculator.exceptions.IllegalConstruction;
import calculator.value.RealValue;
import calculator.value.Value;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TestSin {

	@Test
	void testSinConstructionAndOp() throws IllegalConstruction {
		Sin sin = new Sin(List.of(new MyNumber(new RealValue(0.0))));
		assertNotNull(sin);
		assertEquals(4, sin.getPrecedence());
		Value res = sin.op(new RealValue(0.0), null);
		assertNotNull(res);
	}
}
