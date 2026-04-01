package calculator.visitor;

import calculator.MyNumber;
import visitor.Printer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TestPrinter {

    @Test
    void testDefaultConstructorPrintsNumber() {
        Printer printer = new Printer();
        assertNotNull(printer);

        MyNumber n = new MyNumber(42);
        n.accept(printer);

        assertEquals("42", printer.getResult());
    }
}
