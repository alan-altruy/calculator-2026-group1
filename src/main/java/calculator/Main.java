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

	/**
	 * This is the main method of the application.
	 * It provides examples of how to use it to construct and evaluate arithmetic expressions.
	 *
	 * @param args	Command-line parameters are not used in this version
	 */
	public static void main(String[] args) {
		System.out.println("Calculator CLI");
		System.out.println("Type 'help' for instructions, or 'quit'/'exit' to exit.");
		
		Calculator c = new Calculator();
		java.util.Scanner scanner = new java.util.Scanner(System.in);
		
		while (true) {
			System.out.print("> ");
			if (!scanner.hasNextLine()) {
				break;
			}
			
			String input = scanner.nextLine().trim();
			if (input.isEmpty()) continue;
			if (input.equalsIgnoreCase("quit") || input.equalsIgnoreCase("exit")) break;
			
			if (input.equalsIgnoreCase("help")) {
				System.out.println("--- Calculator Help ---");
				System.out.println("  Commands:");
				System.out.println("    help  - Show this help message");
				System.out.println("    quit  - Exit the program");
				System.out.println("    exit  - Exit the program");
				System.out.println("  Expressions:");
				System.out.println("    Type any arithmetic expression.");
				System.out.println("    Supported ops: +, -, *, /, ** (Power).");
				System.out.println("    Implicit multiplication (e.g. '(4+5)(6)') is supported.");
				continue;
			}
			
			try {
				Expression e = ExpressionParser.parse(input);
				if (e != null) {
					c.print(e);
				}
			} catch (Exception ex) {
				System.out.println("Error: " + ex.getMessage());
			}
		}
		
		System.out.println("Goodbye!");
		if (scanner != null) {
			scanner.close();
		}
 	}

}
