package calculator;

import calculator.enums.AngleMode;
import calculator.enums.NumberDomain;

import java.util.Locale;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * A very simple calculator in Java
 * University of Mons - UMONS
 * Software Engineering Lab
 * Faculty of Sciences
 *
 * @author tommens
 */
public class Main {

	private static final Logger LOGGER = Logger.getLogger(Main.class.getName());
    private static final String HELP_COLUMN_FORMAT = "  %-20s %s%n";

	/**
	 * This is the main method of the application.
	 * It provides examples of how to use it to construct and evaluate arithmetic expressions.
	 *
	 * @param args	Command-line parameters are not used in this version
	 */
	public static void main(String[] args) {

		// Configure logging: remove timestamp and class from console output
        System.setProperty("SimpleFormatter.format", "%4$s: %5$s%n");
        Logger rootLogger = Logger.getLogger("");
        for (Handler h : rootLogger.getHandlers()) {
            h.setFormatter(new Formatter() {
                @Override
                public String format(LogRecord r) {
                    return formatMessage(r) + System.lineSeparator();
                }
            });
        }

		LOGGER.info("Calculator CLI");
		LOGGER.info("Type 'help' for instructions, or 'quit'/'exit' to exit.");

		Calculator c = new Calculator();
		try (java.util.Scanner scanner = new java.util.Scanner(System.in)) {
			boolean exit = false;
			while (!exit && scanner.hasNextLine()) {
				LOGGER.info("> ");

				String input = scanner.nextLine().trim();

				exit = handleInput(input, c);
			}
		}
		LOGGER.info("Goodbye!");
 	}

	private static NumberDomain currentDomain = NumberDomain.INTEGER;
	private static AngleMode currentAngleMode = AngleMode.RAD;
	private static int currentPrecision = 10;

	private static final int MIN_ARG_LENGTH = 2;

	public static NumberDomain getCurrentDomain() { return currentDomain; }
	public static void setCurrentDomain(NumberDomain domain) { currentDomain = domain; }
	
	public static AngleMode getCurrentAngleMode() { return currentAngleMode; }
	public static void setCurrentAngleMode(AngleMode mode) { currentAngleMode = mode; }

	public static int getCurrentPrecision() { return currentPrecision; }
	public static void setCurrentPrecision(int precision) { currentPrecision = precision; }


	static boolean handleInput(String input, Calculator c) {
		if (input == null || input.isEmpty()) return false;

		if (isExitCommand(input)) return true;
		if (handleMode(input)) return false;
		if (handlePrecision(input)) return false;
		if (handleDomain(input)) return false;

		if (input.equalsIgnoreCase("help")) {
			printHelp();
			return false;
		}

		try {
			Expression e = ExpressionParser.parse(input);
			if (e != null) c.print(e);
		} catch (IllegalArgumentException ex) {
			LOGGER.severe("Error: " + ex.getMessage());
		}

		return false;
	}

	private static boolean isExitCommand(String input) {
		return input.equalsIgnoreCase("quit") || input.equalsIgnoreCase("exit");
	}

	private static boolean handleMode(String input) {
		if (!input.toLowerCase(Locale.ROOT).startsWith("mode ")) return false;
		String[] parts = input.split(" ");
		if (parts.length < MIN_ARG_LENGTH) return true;
		if (parts[1].equalsIgnoreCase("deg")) {
			setCurrentAngleMode(AngleMode.DEG);
			LOGGER.info("Angle mode switched to DEG.");
		} else {
			setCurrentAngleMode(AngleMode.RAD);
			LOGGER.info("Angle mode switched to RAD.");
		}
		return true;
	}

	private static boolean handlePrecision(String input) {
		if (!input.toLowerCase(Locale.ROOT).startsWith("precision ")) return false;
		String[] parts = input.split(" ");
		if (parts.length < MIN_ARG_LENGTH) return true;
		try {
			setCurrentPrecision(Math.max(1, Integer.parseInt(parts[1])));
			LOGGER.info("Precision set to " + getCurrentPrecision() + ".");
		} catch (NumberFormatException ignored) {
			LOGGER.warning("Invalid precision value: " + parts[1]);
		}				
		return true;
	}

	private static boolean handleDomain(String input) {
		if (!input.toLowerCase(Locale.ROOT).startsWith("domain ")) return false;
		String[] parts = input.split(" ");
		if (parts.length < MIN_ARG_LENGTH) return true;
		String dom = parts[1].toUpperCase(Locale.ROOT);
		switch (dom) {
			case "R", "RATIONAL":
				setCurrentDomain(NumberDomain.RATIONAL);
				LOGGER.info("Number domain switched to RATIONAL.");
				break;
			case "RE", "REAL":
				setCurrentDomain(NumberDomain.REAL);
				LOGGER.info("Number domain switched to REAL.");
				break;
			case "I", "C", "COMPLEX":
				setCurrentDomain(NumberDomain.COMPLEX);
				LOGGER.info("Number domain switched to COMPLEX.");
				break;
			case "Z", "INTEGER":
			default:
				setCurrentDomain(NumberDomain.INTEGER);
				LOGGER.info("Number domain switched to INTEGER.");
				break;
		}
		return true;
	}

	private static void printHelp() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("--- Calculator Help ---%n%n"));
        sb.append(String.format("Commands:%n"));
        sb.append(String.format(HELP_COLUMN_FORMAT, "help", "Show this help message"));
        sb.append(String.format(HELP_COLUMN_FORMAT, "domain <type>", "Switch domain (Z/INTEGER, R/RATIONAL, RE/REAL, I/C/COMPLEX)"));
        sb.append(String.format(HELP_COLUMN_FORMAT, "mode <rad|deg>", "Switch trigonometric mode to Radians or Degrees"));
        sb.append(String.format(HELP_COLUMN_FORMAT, "precision <n>", "Set precision to n decimal digits (for REAL)"));
        sb.append(String.format(HELP_COLUMN_FORMAT, "quit/exit", "Exit the program"));
        sb.append(String.format("%nExpressions:%n"));
        sb.append(String.format("  Supported ops: +, -, *, /, **, mod, //, !, |x|%n"));
        sb.append(String.format("  Supported funcs: sin, cos, tan, arcsin, arccos, arctan, ln, log%n"));
        sb.append(String.format("  Supported consts: pi, e, phi%n"));
        sb.append(String.format("%nCurrent settings:%n"));
        sb.append(String.format("  Domain: %s%n", getCurrentDomain()));
        sb.append(String.format("  Angle mode: %s%n", getCurrentAngleMode()));
        sb.append(String.format("  Precision: %d%n", getCurrentPrecision()));

        LOGGER.log(Level.INFO, sb::toString);
    }
}