package calculator;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

public class ExpressionParser {

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
        } catch (Exception e) {
            return null;
        }
    }
}
