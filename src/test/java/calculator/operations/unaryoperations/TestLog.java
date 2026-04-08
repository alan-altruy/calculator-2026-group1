package calculator.operations.unaryoperations;

import calculator.MyNumber;
import calculator.exceptions.IllegalConstruction;
import calculator.value.RealValue;
import calculator.value.Value;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TestLog {

    @Test
    void testLogConstructionAndOp() throws IllegalConstruction {
        Log log = new Log(List.of(new MyNumber(new RealValue(10.0))));
        assertNotNull(log);
        assertEquals(4, log.getPrecedence());
        Value res = log.op(new RealValue(10.0), null);
        assertNotNull(res);
    }
}
