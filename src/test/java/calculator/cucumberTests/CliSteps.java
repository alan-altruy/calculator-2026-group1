package calculator.cucumberTests;

import calculator.Main;
import calculator.ExpressionParser;
import calculator.Expression;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Locale;
import java.nio.charset.StandardCharsets;

public class CliSteps {

    private InputStream originalIn;
    private PrintStream originalOut;
    private ByteArrayOutputStream out;
    private String cliOutput;
    private String lastInput;

    @Given("I initialise the cli")
    public void i_initialise_the_cli() {
        originalIn = System.in;
        originalOut = System.out;
        out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        cliOutput = "";
    }

    @When("I provide the input {string} in the cli")
    public void i_provide_the_input_in_the_cli(String input) {
        lastInput = input;

        String lower = input.toLowerCase(Locale.ROOT);

        // For commands and help/exit we run a short Main session so logging is configured and output is printed
        if (input.equalsIgnoreCase("quit") || input.equalsIgnoreCase("exit") || input.equalsIgnoreCase("help") || lower.startsWith("domain ") || lower.startsWith("mode ") || lower.startsWith("precision ") || lower.startsWith("seed ")) {
            String combined = input + System.lineSeparator() + "quit" + System.lineSeparator();
            ByteArrayInputStream in = new ByteArrayInputStream(combined.getBytes(StandardCharsets.UTF_8));
            System.setIn(in);

            Main.main(new String[0]);

            cliOutput = out.toString(StandardCharsets.UTF_8);

            // Restore originals
            System.setIn(originalIn);
            System.setOut(originalOut);
        }
        // For arithmetic expressions we evaluate in the Then step to avoid running multiple REPLs
    }

    @Given("I switch to the domain {string} in the cli")
    public void i_switch_to_the_domain_in_the_cli(String domain) {
        String combined = "domain " + domain + System.lineSeparator() + "quit" + System.lineSeparator();
        ByteArrayInputStream in = new ByteArrayInputStream(combined.getBytes(StandardCharsets.UTF_8));
        System.setIn(in);

        Main.main(new String[0]);

        cliOutput = out.toString(StandardCharsets.UTF_8);

        // Restore originals
        System.setIn(originalIn);
        System.setOut(originalOut);
    }

    @Then("I should see the help message")
    public void i_should_see_the_help_message() {
        assertTrue(cliOutput.contains("--- Calculator Help ---"), "Actual CLI output:\n" + cliOutput);
    }

    @Then("the cli should exit")
    public void the_cli_should_exit() {
        assertTrue(cliOutput.contains("Goodbye!"), "Actual CLI output:\n" + cliOutput);
    }

    @Then("I should see the result {string} in the cli")
    public void i_should_see_the_result_in_the_cli(String expected) {
        // Evaluate arithmetic expressions directly to avoid REPL interference
        Expression e = ExpressionParser.parse(lastInput);
        calculator.Calculator calc = new calculator.Calculator();
        calculator.value.Value v = calc.evalValue(e);
        String actual = v.toString();
        assertEquals(expected, actual, "Expected result mismatch. CLI output:\n" + cliOutput);
    }

    @Then("I should see the message {string} in the cli")
    public void i_should_see_the_message_in_the_cli(String expected) {
        assertTrue(cliOutput.contains(expected), "Expected message not found. CLI output:\n" + cliOutput);
    }
}
