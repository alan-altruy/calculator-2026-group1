package calculator.operations.unaryoperations;

import calculator.MyNumber;
import calculator.enums.Notation;
import calculator.exceptions.IllegalConstruction;
import calculator.value.RealValue;
import calculator.value.Value;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TestAbs {

    @Test
    void testAbsConstructionAndOp() throws IllegalConstruction {
        Abs abs = new Abs(List.of(new MyNumber(new RealValue(-2.5))));
        assertNotNull(abs);
        assertEquals(4, abs.getPrecedence());
        Value res = abs.op(new RealValue(-2.5), null);
        assertNotNull(res);
    }

    @Test
    void testConstructorWithNotation() {
        try {
            Abs a = new Abs(List.of(new MyNumber(new RealValue(-2.5))), Notation.PREFIX);
            assertEquals(Notation.PREFIX, a.getNotation());
        } catch (IllegalConstruction e) { fail(); }
    }
}
