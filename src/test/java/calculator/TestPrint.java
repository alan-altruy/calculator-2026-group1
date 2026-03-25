package calculator;

//Import Junit5 libraries for unit testing:
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

public class TestPrint {


    private Calculator c;

    @BeforeEach
    void setup(){
        c = new Calculator();
    }

    @Test
    void testPrintExpression(){
        Expression e = new MyNumber(67);
        c.print(e);
        assertTrue(true);
    }

    @Test
    void testPrintExpressionDetail(){
        Expression e = new MyNumber(67);
        c.printExpressionDetails(e);
        assertTrue(true);
    }
}
