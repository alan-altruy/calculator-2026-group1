package visitor;

import calculator.Expression;
import calculator.MyNumber;
import calculator.Notation;
import calculator.Operation;

import java.util.ArrayList;
import java.util.List;

/**
 * Printer is a concrete visitor that serves to
 * convert arithmetic expressions into their String representation
 * using a specified notation (prefix, infix, or postfix).
 */
public class Printer extends Visitor {

    /**
     * The notation to use for rendering operations.
     */
    private final Notation notation;

    /**
     * The result of the last visit, i.e. the String representation of the last visited expression.
     */
    private String result;

    /**
     * Constructor specifying the notation to use.
     *
     * @param notation The notation (PREFIX, INFIX, or POSTFIX)
     */
    public Printer(Notation notation) {
        this.notation = notation;
    }

    /**
     * Default constructor using INFIX notation.
     */
    public Printer() {
        this(Notation.INFIX);
    }

    /**
     * Getter method to obtain the result of the string conversion.
     *
     * @return The String representation of the last visited expression
     */
    public String getResult() {
        return result;
    }

    /**
     * Visit a number and convert it to its String representation.
     *
     * @param n The number being visited
     */
    @Override
    public void visit(MyNumber n) {
        result = Integer.toString(n.getValue());
    }

    /**
     * Visit an operation and convert it to its String representation
     * using the specified notation.
     *
     * @param o The operation being visited
     */
    @Override
    public void visit(Operation o) {
        // First, get the String representation of each child expression
        List<String> childStrings = new ArrayList<>();
        for (Expression a : o.getArgs()) {
            a.accept(this);
            childStrings.add(result);
        }

        result = switch (notation) {
            case INFIX -> {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < o.getArgs().size(); i++) {
                    Expression child = o.getArgs().get(i);
                    boolean needsParens = false;
                    if (child instanceof Operation opChild) {
                        if (opChild.notation == Notation.INFIX) {
                            if (opChild.getPrecedence() < o.getPrecedence()) {
                                needsParens = true;
                            } else if (opChild.getPrecedence() == o.getPrecedence()) {
                                // For left-associative operations (Minus, Divides), the right-hand child needs parens
                                if (i > 0 && (o.getSymbol().equals("-") || o.getSymbol().equals("/"))) {
                                    needsParens = true;
                                }
                                // For right-associative operations (Power), the left-hand child needs parens
                                else if (i == 0 && o.getSymbol().equals("**")) {
                                    needsParens = true;
                                }
                            }
                        }
                    }
                    if (i > 0) sb.append(" ").append(o.getSymbol()).append(" ");
                    if (needsParens) sb.append("( ").append(childStrings.get(i)).append(" )");
                    else sb.append(childStrings.get(i));
                }
                yield sb.toString();
            }
            case PREFIX -> o.getSymbol() + " (" +
                    childStrings.stream().reduce((s1, s2) -> s1 + ", " + s2).get() +
                    ")";
            case POSTFIX -> "(" +
                    childStrings.stream().reduce((s1, s2) -> s1 + ", " + s2).get() +
                    ") " + o.getSymbol();
        };
    }
}
