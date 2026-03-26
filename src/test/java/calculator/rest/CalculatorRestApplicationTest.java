package calculator.rest;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = calculator.rest.CalculatorRestApplication.class)
public class CalculatorRestApplicationTest {

    @Test
    void contextLoads() {
        // test that the Spring context for CalculatorRestApplication loads successfully
    }
    @Test
    void mainRuns() {
        calculator.rest.CalculatorRestApplication.main(new String[]{});
    }    
}
