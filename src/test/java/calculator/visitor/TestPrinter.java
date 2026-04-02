package calculator.visitor;

import calculator.MyNumber;
import calculator.Operation;
import calculator.Times;
import visitor.Printer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.sql.Time;


class TestPrinter {

    @Test
    void testDefaultConstructorPrintsNumber() {
        Printer printer = new Printer();
        assertNotNull(printer);

        MyNumber n = new MyNumber(42);
        n.accept(printer);

        assertEquals("42", printer.getResult());
    }

    @Test
    void testNullNotationThrowsException() {
        Printer printer = new Printer(null);
        assertNotNull(printer);

        MyNumber n = new MyNumber(42);
        n.accept(printer);

        // Since the notation is null, we expect an IllegalStateException to be thrown.
        // However, since the visit(MyNumber) method does not use the notation, it should still return "42".
        assertEquals("42", printer.getResult());
        Times op = new Times(java.util.List.of(new MyNumber(1), new MyNumber(2)));
        assertThrows(IllegalStateException.class, () -> printer.visit(op));
    }
}
