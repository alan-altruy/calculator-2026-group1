package calculator.cucumberTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Locale;

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

    @Given("I set the angle unit to {string}")
    public void givenISetTheAngleUnitTo(String angleUnit) {
        if (angleUnit.equalsIgnoreCase("RAD") || angleUnit.equalsIgnoreCase("DEG")) {
            Main.setCurrentAngleMode(calculator.enums.AngleMode.valueOf(angleUnit.toUpperCase(Locale.ROOT)));
        } else {
            throw new IllegalArgumentException("Invalid angle unit: " + angleUnit);
        }
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

    @Then("an error is raised with message {string}")
    public void an_error_is_raised_with_message(String s) {
        try {
            c.getCalculator().evalValue(e);
        } catch (IllegalArgumentException | ArithmeticException ex) {
            assertEquals(s, ex.getMessage(), "Expected error message mismatch. Expression: " + e.toString());
            return;
        }
        throw new AssertionError("Expected an exception with message: " + s + " but no exception was thrown. Expression: " + e.toString());
    }
}
