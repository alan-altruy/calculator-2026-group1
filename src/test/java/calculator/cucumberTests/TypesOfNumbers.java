package calculator.cucumberTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import calculator.Expression;
import calculator.ExpressionParser;
import calculator.Main;
import calculator.enums.NumberDomain;
import calculator.value.Value;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class TypesOfNumbers {
    
    private CalculatorContext c;
    private Expression e;

    public TypesOfNumbers(CalculatorContext context) {
        this.c = context;
    }

    @Given("the domain is {string}")
    public void givenTheDomainIs(String domain) {
        switch (domain) {
            case "INTEGER":
                Main.setCurrentDomain(NumberDomain.INTEGER);
                break;
            case "REAL":
                Main.setCurrentDomain(NumberDomain.REAL);
                break;
            case "RATIONAL":
                Main.setCurrentDomain(NumberDomain.RATIONAL);
                break;
            case "COMPLEX":
                Main.setCurrentDomain(NumberDomain.COMPLEX);
                break;
            default:
                throw new IllegalArgumentException("Unknown domain: " + domain);
        }
    }
    
    @When("I provide the input {string} in the specified domain")
    public void whenIProvideTheInputToTheCalculator(String input) {
        this.e = ExpressionParser.parse(input);
    }

    @Then("the result is {string} in the specified domain")
    public void thenTheResultIsReal(String expected) {
        assertNotNull(e, "Expression should not be null");
        Value v = c.getCalculator().evalValue(e);
        assertNotNull(v, "Evaluated value should not be null");
        String resultStr = v.toString();
        assertEquals(expected, resultStr);
    }
}
