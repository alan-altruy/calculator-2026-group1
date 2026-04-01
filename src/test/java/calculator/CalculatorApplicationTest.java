package calculator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = calculator.CalculatorApplication.class)
class CalculatorApplicationTest {

    @Test
    void contextLoads() {
        // test that the Spring context for CalculatorApplication loads successfully
    }
    @Test
    void mainRuns() {
        Assertions.assertDoesNotThrow(() -> calculator.CalculatorApplication.main(new String[]{}));
    }    
}
