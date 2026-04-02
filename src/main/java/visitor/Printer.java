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
    private Notation notation;

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
        if (notation != Notation.PREFIX && notation != Notation.INFIX && notation != Notation.POSTFIX) {
            this.notation = Notation.DEFAULT; // Default to DEFAULT if an invalid notation is provided
        }
        else {
            this.notation = notation;
        }
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

        switch (notation) {
            case INFIX -> result = formatInfix(o, childStrings);
            case PREFIX -> result = formatPrefix(o, childStrings);
            case POSTFIX -> result = formatPostfix(o, childStrings);
            default -> throw new IllegalStateException("Unexpected notation");
        }
    }

    private String formatInfix(Operation o, List<String> childStrings) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < o.getArgs().size(); i++) {
            Expression child = o.getArgs().get(i);
            boolean needsParens = childNeedsParens(o, child, i);

            if (i > 0) sb.append(" ").append(o.getSymbol()).append(" ");
            if (needsParens) sb.append("( ").append(childStrings.get(i)).append(" )");
            else sb.append(childStrings.get(i));
        }
        return sb.toString();
    }

    private String formatPrefix(Operation o, List<String> childStrings) {
        String joined = String.join(", ", childStrings);
        return o.getSymbol() + " (" + joined + ")";
    }

    private String formatPostfix(Operation o, List<String> childStrings) {
        String joined = String.join(", ", childStrings);
        return "(" + joined + ") " + o.getSymbol();
    }

    private boolean childNeedsParens(Operation parent, Expression child, int index) {
        if (!(child instanceof Operation opChild)) return false;
        if (opChild.getNotation() != Notation.INFIX) return false;

        int childPrec = opChild.getPrecedence();
        int parentPrec = parent.getPrecedence();

        if (childPrec < parentPrec) return true;
        if (childPrec == parentPrec) {
            // For left-associative operations (Minus, Divides), the right-hand child needs parens
            if (index > 0 && (parent.getSymbol().equals("-") || parent.getSymbol().equals("/"))) {
                return true;
            }
            // For right-associative operations (Power), the left-hand child needs parens
            if (index == 0 && parent.getSymbol().equals("**")) {
                return true;
            }
        }
        return false;
    }
}
