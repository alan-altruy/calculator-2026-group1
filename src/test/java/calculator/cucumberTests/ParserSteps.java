package calculator.cucumberTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import calculator.Expression;
import calculator.ExpressionParser;
import calculator.Main;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class ParserSteps {
    
    private Expression e;
    private CalculatorContext c;

    public ParserSteps(CalculatorContext context) {
        this.c = context;
    }

    @Given("I set the current domain to {string}")
    public void givenISetTheCurrentDomainTo(String domain) {
        Main.setCurrentDomain(calculator.enums.NumberDomain.valueOf(domain));
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
        String result = c.getCalculator().evalValue(e).toString();
        double expectedDouble = Double.parseDouble(expected);
        double resultDouble = Double.parseDouble(result);
        assertEquals(expectedDouble, resultDouble, 1e-9, "Expected result mismatch. Expression: " + e.toString());
    }
}
