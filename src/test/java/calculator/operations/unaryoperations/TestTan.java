package calculator.operations.unaryoperations;

import calculator.MyNumber;
import calculator.exceptions.IllegalConstruction;
import calculator.value.RealValue;
import calculator.value.Value;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TestTan {

    @Test
    void testTanConstructionAndOp() throws IllegalConstruction {
        Tan tan = new Tan(List.of(new MyNumber(new RealValue(0.0))));
        assertNotNull(tan);
        assertEquals(4, tan.getPrecedence());
        Value res = tan.op(new RealValue(0.0), null);
        assertNotNull(res);
    }
}
