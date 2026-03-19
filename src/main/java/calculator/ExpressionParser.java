package calculator;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

public class ExpressionParser {

    public static Expression parse(String expressionString) {
        CalculatorLexer lexer = new CalculatorLexer(CharStreams.fromString(expressionString));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        CalculatorParser parser = new CalculatorParser(tokens);
        ParseTree tree = parser.prog();

        CalculatorVisitorImpl visitor = new CalculatorVisitorImpl();
        return visitor.visit(tree);
    }
}
