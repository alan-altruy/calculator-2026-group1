package calculator.operations.unaryoperations;

import calculator.MyNumber;
import calculator.exceptions.IllegalConstruction;
import calculator.value.RealValue;
import calculator.value.Value;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TestLn {

    @Test
    void testLnConstructionAndOp() throws IllegalConstruction {
        Ln ln = new Ln(List.of(new MyNumber(new RealValue(1.0))));
        assertNotNull(ln);
        assertEquals(4, ln.getPrecedence());
        Value res = ln.op(new RealValue(1.0), null);
        assertNotNull(res);
    }
}
