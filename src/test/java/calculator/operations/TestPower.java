package calculator.operations;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Arrays;
import java.util.List;

import calculator.Expression;
import calculator.MyNumber;
import calculator.enums.Notation;
import calculator.exceptions.IllegalConstruction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TestPower {

    private List<Expression> params;
    private Power op;

    @BeforeEach
    void setUp() {
        params = Arrays.asList(new MyNumber(2), new MyNumber(3));
        try {
            op = new Power(params);
        } catch (IllegalConstruction e) {
            fail();
        }
    }

    @Test
    void testConstructorWithNotation() {
        assertDoesNotThrow(() -> {
            Power p = new Power(params, Notation.PREFIX);
            assertEquals(Notation.PREFIX, p.getNotation());
        });
    }

    @Test
    void testOp() {
        assertEquals(8, op.op(new calculator.value.IntegerValue(2), new calculator.value.IntegerValue(3)).intValue());
    }

    @Test
    void testGetPrecedence() {
        assertEquals(3, op.getPrecedence());
    }
}