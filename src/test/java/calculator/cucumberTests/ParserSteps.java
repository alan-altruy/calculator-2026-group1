package calculator.cucumberTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import calculator.Expression;
import calculator.ExpressionParser;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class ParserSteps {
    
    private Expression e;
    private CalculatorContext c;

    public ParserSteps(CalculatorContext context) {
        this.c = context;
    }

    @When("I provide the input {string}")
    public void whenIProvideTheInput(String input) {
        e = ExpressionParser.parse(input);
    }

    @Then("the output is {string}")
    public void thenTheOutputIs(String expected) {
        if (e == null) {
            throw new IllegalStateException("Expression should not be null");
        }
        assertNotNull(e);
        int result = c.getCalculator().eval(e);
        assertEquals(Integer.parseInt(expected), result);
    }
}
