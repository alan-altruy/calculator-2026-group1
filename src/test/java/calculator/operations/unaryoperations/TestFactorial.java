package calculator.operations.unaryoperations;

import calculator.MyNumber;
import calculator.exceptions.IllegalConstruction;
import calculator.value.RealValue;
import calculator.value.Value;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TestFactorial {

    @Test
    void testFactorialConstructionAndOp() throws IllegalConstruction {
        Factorial fact = new Factorial(List.of(new MyNumber(new RealValue(5.0))));
        assertNotNull(fact);
        assertEquals(4, fact.getPrecedence());
        Value res = fact.op(new RealValue(5.0), null);
        assertNotNull(res);
    }
}
