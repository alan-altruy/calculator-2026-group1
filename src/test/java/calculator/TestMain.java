package calculator;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
import calculator.enums.AngleMode;
import calculator.enums.NumberDomain;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.Handler;
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
        assertTrue(output.contains("Supported ops: +, -, *, /, **, mod, //, !, |x|"));
        assertTrue(output.contains("Supported funcs: sin, cos, tan"));
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

    @Test
    void testLoggingFormatterUsesSystemLineSeparator() {
        ByteArrayInputStream in = new ByteArrayInputStream("quit\n".getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        System.setIn(in);
        System.setOut(new PrintStream(out));

        Main.main(new String[0]);

        Logger root = Logger.getLogger("");
        for (Handler h : root.getHandlers()) {
            Formatter f = h.getFormatter();
            LogRecord lr = new LogRecord(Level.INFO, "TEST_MESSAGE");
            String formatted = f.format(lr);
            assertEquals("TEST_MESSAGE" + System.lineSeparator(), formatted);
        }
    }

    @Test
    void testHandleInputNullOrEmptyReturnsFalse() {
        Calculator calc = new Calculator();
        assertFalse(Main.handleInput(null, calc));
        assertFalse(Main.handleInput("", calc));
    }

    @Test
    void testHandleModePrecisionDomainAndSeedCommands() {
        Calculator calc = new Calculator();

        // save originals
        AngleMode origMode = Main.getCurrentAngleMode();
        int origPrecision = Main.getCurrentPrecision();
        NumberDomain origDomain = Main.getCurrentDomain();
        Long origSeed = 1L; // RandomGenerator does not have a getter for the seed, so we just save a default value to restore later

        try {
            // mode
            boolean resMode = Main.handleInput("mode deg", calc);
            assertFalse(resMode);
            assertEquals(AngleMode.DEG, Main.getCurrentAngleMode());

            resMode = Main.handleInput("mode rad", calc);
            assertFalse(resMode);
            assertEquals(AngleMode.RAD, Main.getCurrentAngleMode());
            AngleMode beforeMode = Main.getCurrentAngleMode();
            // mod with 1 argument should not change mode
            resMode = Main.handleInput("mode ", calc);
            assertFalse(resMode);
            assertEquals(beforeMode, Main.getCurrentAngleMode());

            // precision
            boolean resPrec = Main.handleInput("precision 4", calc);
            assertFalse(resPrec);
            assertEquals(4, Main.getCurrentPrecision());

            // invalid precision should not change
            Main.setCurrentPrecision(origPrecision);
            resPrec = Main.handleInput("precision notanumber", calc);
            assertFalse(resPrec);
            assertEquals(origPrecision, Main.getCurrentPrecision());


            // precision with missing argument should not change
            resPrec = Main.handleInput("precision ", calc);
            assertFalse(resPrec);
            assertEquals(origPrecision, Main.getCurrentPrecision());

            // domain
            boolean resDom = Main.handleInput("domain real", calc);
            assertFalse(resDom);
            assertEquals(NumberDomain.REAL, Main.getCurrentDomain());

            resDom = Main.handleInput("domain complex", calc);
            assertFalse(resDom);
            assertEquals(NumberDomain.COMPLEX, Main.getCurrentDomain());

            resDom = Main.handleInput("domain rational", calc);
            assertFalse(resDom);
            assertEquals(NumberDomain.RATIONAL, Main.getCurrentDomain());

            resDom = Main.handleInput("domain integer", calc);
            assertFalse(resDom);
            assertEquals(NumberDomain.INTEGER, Main.getCurrentDomain());

            // domain with missing argument should not change
            Main.setCurrentDomain(origDomain);
            resDom = Main.handleInput("domain ", calc);
            assertFalse(resDom);
            assertEquals(origDomain , Main.getCurrentDomain());

            // seed
            boolean resSeed = Main.handleInput("seed 12345", calc);
            assertFalse(resSeed);

            // catch number format exception for seed
            resSeed = Main.handleInput("seed notanumber", calc);
            assertFalse(resSeed);

            // return false if args < 2 for seed
            resSeed = Main.handleInput("seed ", calc);
            assertFalse(resSeed);

        } finally {
            // restore
            Main.setCurrentAngleMode(origMode);
            Main.setCurrentPrecision(origPrecision);
            Main.setCurrentDomain(origDomain);
            RandomGenerator.setSeed(origSeed);
        }
    }

    @Test
    void testSettersAndGettersForAngleModeAndPrecision() {
        // save originals
        AngleMode origMode = Main.getCurrentAngleMode();
        int origPrecision = Main.getCurrentPrecision();
        try {
            Main.setCurrentAngleMode(AngleMode.DEG);
            assertEquals(AngleMode.DEG, Main.getCurrentAngleMode());

            Main.setCurrentPrecision(3);
            assertEquals(3, Main.getCurrentPrecision());
        } finally {
            // restore
            Main.setCurrentAngleMode(origMode);
            Main.setCurrentPrecision(origPrecision);
        }
    }
}