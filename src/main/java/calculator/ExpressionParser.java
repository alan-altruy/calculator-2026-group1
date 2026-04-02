package calculator;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.RecognitionException;

public class ExpressionParser {

    /**
     * Constructor for the ExpressionParser class.
     * This class is responsible for parsing string representations of expressions into Expression objects that can be evaluated by the Calculator.
     * The constructor does not require any parameters and does not perform any initialization, as the parsing is done in the static parse method.
     */
    private ExpressionParser() {
        // No initialization needed for this parser
    }

    /**
     * Parses a string representation of an expression and returns an Expression object that can be evaluated by the Calculator.
     * @param expressionString the string representation of the expression to be parsed
     * @return an Expression object representing the parsed expression
     */
    public static Expression parse(String expressionString) {
        String input = expressionString.trim();

        Expression result = tryParse(input);

        if (result == null && input.startsWith("(")) {
            result = tryParse("*" + input);
        }

        if (result == null) {
            CalculatorLexer lexer = new CalculatorLexer(CharStreams.fromString(input));
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            CalculatorParser parser = new CalculatorParser(tokens);
            ParseTree tree = parser.prog();
            CalculatorVisitorImpl visitor = new CalculatorVisitorImpl();
            return visitor.visit(tree);
        }

        return result;
    }

    private static Expression tryParse(String input) {
        try {
            CalculatorLexer lexer = new CalculatorLexer(CharStreams.fromString(input));
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            CalculatorParser parser = new CalculatorParser(tokens);
            parser.removeErrorListeners();
            ParseTree tree = parser.prog();

            if (parser.getNumberOfSyntaxErrors() > 0) {
                return null;
            }

            CalculatorVisitorImpl visitor = new CalculatorVisitorImpl();
            return visitor.visit(tree);
        } catch (RecognitionException | IllegalArgumentException e) {
            return null;
        }
    }
}
