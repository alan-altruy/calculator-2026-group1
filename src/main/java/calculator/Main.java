package calculator;

import java.util.Locale;

/**
 * A very simple calculator in Java
 * University of Mons - UMONS
 * Software Engineering Lab
 * Faculty of Sciences
 *
 * @author tommens
 */
public class Main {

	private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(Main.class.getName());

	/**
	 * This is the main method of the application.
	 * It provides examples of how to use it to construct and evaluate arithmetic expressions.
	 *
	 * @param args	Command-line parameters are not used in this version
	 */
	public static void main(String[] args) {
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

	public static NumberDomain currentDomain = NumberDomain.INTEGER;
	public static AngleMode currentAngleMode = AngleMode.RAD;
	public static int currentPrecision = 10;

	static boolean handleInput(String input, Calculator c) {
		if (input == null || input.isEmpty()) return false;

		if (input.equalsIgnoreCase("quit") || input.equalsIgnoreCase("exit")) {
			return true;
		}

		if (input.toLowerCase(Locale.ROOT).startsWith("mode ")) {
			String[] parts = input.split(" ");
			if (parts.length > 1) {
				if (parts[1].equalsIgnoreCase("deg")) {
					currentAngleMode = AngleMode.DEG;
					LOGGER.info("Angle mode switched to DEG.");
				} else {
					currentAngleMode = AngleMode.RAD;
					LOGGER.info("Angle mode switched to RAD.");
				}
			}
			return false;
		}

		if (input.toLowerCase(Locale.ROOT).startsWith("precision ")) {
			String[] parts = input.split(" ");
			if (parts.length > 1) {
				try {
					currentPrecision = Math.max(1, Integer.parseInt(parts[1]));
					LOGGER.info("Precision set to " + currentPrecision + ".");
				} catch (NumberFormatException ignored) { }
			}
			return false;
		}

		if (input.toLowerCase(Locale.ROOT).startsWith("domain ")) {
			String[] parts = input.split(" ");
				if (parts.length > 1) {
				String dom = parts[1].toUpperCase(Locale.ROOT);
				switch (dom) {
					case "R":
					case "RATIONAL":
						currentDomain = NumberDomain.RATIONAL;
						LOGGER.info("Number domain switched to RATIONAL.");
						break;
					case "RE":
					case "REAL":
						currentDomain = NumberDomain.REAL;
						LOGGER.info("Number domain switched to REAL.");
						break;
					case "I":
					case "C":
					case "COMPLEX":
						currentDomain = NumberDomain.COMPLEX;
						LOGGER.info("Number domain switched to COMPLEX.");
						break;
					case "Z":
					case "INTEGER":
					default:
						currentDomain = NumberDomain.INTEGER;
						LOGGER.info("Number domain switched to INTEGER.");
						break;
				}
			}
			return false;
		}

		if (input.equalsIgnoreCase("help")) {
			LOGGER.info("--- Calculator Help ---");
			LOGGER.info("  Commands:");
			LOGGER.info("    help              - Show this help message");
			LOGGER.info("    domain <type>     - Switch domain (Z/INTEGER, R/RATIONAL, RE/REAL, I/C/COMPLEX)");
			LOGGER.info("    mode <rad|deg>    - Switch trigonometric mode to Radians or Degrees");
			LOGGER.info("    precision <n>     - Set precision to n decimal digits (for REAL)");
			LOGGER.info("    quit/exit         - Exit the program");
			LOGGER.info("  Expressions:");
			LOGGER.info("    Supported ops: +, -, *, /, **, mod, //, !, |x|");
			LOGGER.info("    Supported funcs: sin, cos, tan, arcsin, arccos, arctan, ln, log");
			LOGGER.info("    Supported consts: pi, e, phi");
			return false;
		}

		try {
			Expression e = ExpressionParser.parse(input);
			if (e != null) {
				c.print(e);
			}
		} catch (IllegalArgumentException ex) {
			LOGGER.severe("Error: " + ex.getMessage());
		}

		return false;
	}
}