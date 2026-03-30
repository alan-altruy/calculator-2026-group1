package calculator;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = calculator.CalculatorApplication.class)
public class CalculatorApplicationTest {

    @Test
    void contextLoads() {
        // test that the Spring context for CalculatorApplication loads successfully
    }
    @Test
    void mainRuns() {
        calculator.CalculatorApplication.main(new String[]{});
    }    
}
