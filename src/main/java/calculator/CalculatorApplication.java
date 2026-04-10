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
     * Default constructor for the CalculatorApplication class. This constructor is not strictly necessary, as Java provides a default no-argument constructor if no other constructors are defined. However, it is included here for clarity and to explicitly indicate that this class can be instantiated without any parameters.
     */
    public CalculatorApplication() {
        // Default constructor
    }

    /**
     * The main method that serves as the entry point of the application. It uses SpringApplication.run() to launch the application.
     * @param args the command-line arguments passed to the application (not used in this case)
     */
    public static void main(String[] args) {
        SpringApplication.run(CalculatorApplication.class, args);
    }
}

