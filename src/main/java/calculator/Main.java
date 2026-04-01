package calculator;


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
			while (true) {
				LOGGER.info("> ");

				if (!scanner.hasNextLine()) {
					break;
				}

				String input = scanner.nextLine().trim();

				if (handleInput(input, c)) {
					break;
				}
			}
		}
		LOGGER.info("Goodbye!");
 	}

	static boolean handleInput(String input, Calculator c) {
		if (input == null || input.isEmpty()) return false;

		if (input.equalsIgnoreCase("quit") || input.equalsIgnoreCase("exit")) {
			return true;
		}

		if (input.equalsIgnoreCase("help")) {
			LOGGER.info("--- Calculator Help ---");
			LOGGER.info("  Commands:");
			LOGGER.info("    help  - Show this help message");
			LOGGER.info("    quit  - Exit the program");
			LOGGER.info("    exit  - Exit the program");
			LOGGER.info("  Expressions:");
			LOGGER.info("    Type any arithmetic expression.");
			LOGGER.info("    Supported ops: +, -, *, /, ** (Power).");
			LOGGER.info("    Implicit multiplication (e.g. '(4+5)(6)') is supported.");
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