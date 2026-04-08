package calculator.operations.unaryoperations;

import calculator.MyNumber;
import calculator.exceptions.IllegalConstruction;
import calculator.value.RealValue;
import calculator.value.Value;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TestCos {

    @Test
    void testCosConstructionAndOp() throws IllegalConstruction {
        Cos cos = new Cos(List.of(new MyNumber(new RealValue(0.0))));
        assertNotNull(cos);
        assertEquals(4, cos.getPrecedence());
        Value res = cos.op(new RealValue(0.0), null);
        assertNotNull(res);
    }
}
