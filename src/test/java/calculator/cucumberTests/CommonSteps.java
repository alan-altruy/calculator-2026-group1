package calculator.cucumberTests;

import calculator.Calculator;
import io.cucumber.java.en.Given;

public class CommonSteps {
    
    private final CalculatorContext context;

    // PicoContainer va automatiquement injecter le CalculatorContext ici
    public CommonSteps(CalculatorContext context) {
        this.context = context;
    }

    @Given("I initialise a calculator")
    public void a_calculator() {
        // On initialise la calculatrice et on la stocke dans le contexte partagé
        context.setCalculator(new Calculator());
    }
}