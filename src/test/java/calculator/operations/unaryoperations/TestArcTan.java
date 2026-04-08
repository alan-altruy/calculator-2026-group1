package calculator.operations.unaryoperations;

import calculator.MyNumber;
import calculator.exceptions.IllegalConstruction;
import calculator.value.RealValue;
import calculator.value.Value;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TestArcTan {

    @Test
    void testArcTanConstructionAndOp() throws IllegalConstruction {
        ArcTan a = new ArcTan(List.of(new MyNumber(new RealValue(0.0))));
        assertNotNull(a);
        assertEquals(4, a.getPrecedence());
        Value res = a.op(new RealValue(0.0), null);
        assertNotNull(res);
    }
}
