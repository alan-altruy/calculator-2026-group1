package calculator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The main application class for the Calculator application. This class is responsible for bootstrapping the Spring Boot application.
 * It contains the main method which serves as the entry point of the application. When run, it will start an embedded web server and
 * initialize the application context, allowing the REST controllers and other components to be available for handling requests.
 */
@SpringBootApplication
public class CalculatorApplication {

    /**
     * The main method that serves as the entry point of the application. It uses SpringApplication.run() to launch the application.
     * @param args the command-line arguments passed to the application (not used in this case)
     */
    public static void main(String[] args) {
        SpringApplication.run(CalculatorApplication.class, args);
    }
}

