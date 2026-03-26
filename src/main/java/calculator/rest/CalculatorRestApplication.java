package calculator.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"calculator","calculator.rest"})
public class CalculatorRestApplication {
    public static void main(String[] args) {
        SpringApplication.run(CalculatorRestApplication.class, args);
    }
}
