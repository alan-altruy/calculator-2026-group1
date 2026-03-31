package calculator;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FilterInputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.Arguments;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.stream.Stream;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class TestMain {

    private static final String GOODBYE = "Goodbye!";

    private static final class TrackableInputStream extends FilterInputStream {
        private boolean closed;

        TrackableInputStream(InputStream in) {
            super(in);
        }

        @Override
        public void close() throws java.io.IOException {
            closed = true;
            super.close();
        }

        boolean isClosed() {
            return closed;
        }
    }

    private InputStream originalIn;
    private PrintStream originalOut;

    @BeforeEach
    void setUp() {
        originalIn = System.in;
        originalOut = System.out;
    }

    @AfterEach
    void tearDown() {
        System.setIn(originalIn);
        System.setOut(originalOut);
    }

    private String runMainWithInput(String input) {
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        System.setIn(in);
        System.setOut(new PrintStream(out));

        Main.main(new String[0]);
        return out.toString();
    }

    @Test
    void testMainConstructor() {
        Main main = new Main();
        assertNotNull(main);
    }

    @Test
    void testMainQuitImmediately() {
        String output = runMainWithInput("quit\n");

        assertTrue(output.contains("Calculator CLI"));
        assertTrue(output.contains("Type 'help' for instructions, or 'quit'/'exit' to exit."));
        assertTrue(output.contains("> "));
        assertTrue(output.contains(GOODBYE));
    }

    @Test
    void testMainHelpThenQuit() {
        String output = runMainWithInput("help\nquit\n");

        assertTrue(output.contains("--- Calculator Help ---"));
        assertTrue(output.contains("Supported ops: +, -, *, /, ** (Power)."));
        assertTrue(output.contains("Implicit multiplication"));
        assertTrue(output.contains(GOODBYE));
    }
    @ParameterizedTest
    @MethodSource("inputAndExpected")
    void testMainVariousInputs(String input, String expectedSubstring) {
        String output = runMainWithInput(input);

        assertTrue(output.contains(expectedSubstring));
        assertTrue(output.contains(GOODBYE));
    }

    private static Stream<Arguments> inputAndExpected() {
        return Stream.of(
            arguments("", "Calculator CLI"),
            arguments("2 + 3\nquit\n", "The result of evaluating expression 2 + 3"),
            arguments("\n2 + 3\nquit\n", "The result of evaluating expression 2 + 3")
        );
    }

    @Test
    void testMainExitCommand() {
        String output = runMainWithInput("exit\n");

        assertTrue(output.contains("Calculator CLI"));
        assertTrue(output.contains(GOODBYE));
    }

    @Test
    void testMainCatchBranchWithInvalidExpression() {
        String output = runMainWithInput("999999999999999999999999999\nquit\n");

        assertTrue(output.contains("Error:"));
        assertTrue(output.contains(GOODBYE));
    }

    @Test
    void testMainSkipsPrintWhenParsedExpressionIsNull() {
        ByteArrayInputStream in = new ByteArrayInputStream("2 + 3\nquit\n".getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        System.setIn(in);
        System.setOut(new PrintStream(out));

        try (MockedStatic<ExpressionParser> parserMock = Mockito.mockStatic(ExpressionParser.class)) {
            parserMock.when(() -> ExpressionParser.parse("2 + 3")).thenReturn(null);
            Main.main(new String[0]);
        }

        String output = out.toString();
        assertFalse(output.contains("The result of evaluating expression"));
        assertTrue(output.contains(GOODBYE));
    }

    @Test
    void testMainClosesScannerWhenExiting() {
        TrackableInputStream in = new TrackableInputStream(
            new ByteArrayInputStream("quit\n".getBytes(StandardCharsets.UTF_8))
        );
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        System.setIn(in);
        System.setOut(new PrintStream(out));

        Main.main(new String[0]);

        assertTrue(in.isClosed());
        assertTrue(out.toString().contains("Goodbye!"));
    }
}