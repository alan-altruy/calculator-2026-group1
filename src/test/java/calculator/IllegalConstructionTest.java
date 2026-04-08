package calculator;

import static org.junit.jupiter.api.Assertions.*;

import calculator.exceptions.IllegalConstruction;
import org.junit.jupiter.api.Test;

class IllegalConstructionTest {

    @Test
    void testIllegalConstructionWithMessage() {
        String msg = "invalid construction";
        IllegalConstruction ex = new IllegalConstruction(msg);
        assertEquals(msg, ex.getMessage());
        assertNull(ex.getCause());
    }

    @Test
    void testIllegalConstructionWithCause() {
        Throwable cause = new RuntimeException("root cause");
        IllegalConstruction ex = new IllegalConstruction(cause);
        assertSame(cause, ex.getCause());
        assertEquals(cause.toString(), ex.getMessage());
    }
}
