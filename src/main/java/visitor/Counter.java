package visitor;

import calculator.Expression;
import calculator.MyNumber;
import calculator.Operation;

/**
 * Counter is a concrete visitor that serves to
 * count the depth, number of operations, and number of numbers
 * contained in an arithmetic expression.
 */
public class Counter extends Visitor {

    /**
     * The depth of the last visited expression.
     */
    private int countDepth;

    /**
     * The number of operations in the last visited expression.
     */
    private int countOps;

    /**
     * The number of numbers in the last visited expression.
     */
    private int countNbs;

    /**
     * Default constructor of the class. Does not initialise anything.
     */
    public Counter() {
        /*
         * Intentionally left blank: all counter fields are primitives
         * and default to 0. Initialization is performed when visiting
         * expressions (`visit(MyNumber)` and `visit(Operation)`), so
         * no setup is required here. Keeping an explicit no-op
         * constructor documents the intended behavior.
         */
    }

    /**
     * Getter method to obtain the depth of the last visited expression.
     *
     * @return The depth of the expression
     */
    public int getCountDepth() { return countDepth; }

    /**
     * Getter method to obtain the number of operations in the last visited expression.
     *
     * @return The number of operations
     */
    public int getCountOps() { return countOps; }

    /**
     * Getter method to obtain the number of numbers in the last visited expression.
     *
     * @return The number of numbers
     */
    public int getCountNbs() { return countNbs; }

    /**
     * Visit a number. A number has depth 0, 0 operations, and 1 number.
     *
     * @param n The number being visited
     */
    @Override
    public void visit(MyNumber n) {
        countDepth = 0;
        countOps = 0;
        countNbs = 1;
    }

    /**
     * Visit an operation. Recursively counts the depth, operations, and numbers
     * of all sub-expressions.
     *
     * @param o The operation being visited
     */
    @Override
    public void visit(Operation o) {
        int maxDepth = 0;
        int totalOps = 0;
        int totalNbs = 0;
        for (Expression a : o.getArgs()) {
            a.accept(this);
            maxDepth = Math.max(maxDepth, countDepth);
            totalOps += countOps;
            totalNbs += countNbs;
        }
        countDepth = 1 + maxDepth;
        countOps = 1 + totalOps;
        countNbs = totalNbs;
    }
}
