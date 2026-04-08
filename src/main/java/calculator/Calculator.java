package calculator;

import visitor.Counter;
import visitor.Evaluator;
import calculator.value.Value;
import java.util.logging.Logger;

/**
 * This class represents the core logic of a Calculator.
 * It can be used to print and evaluate artihmetic expressions.
 *
 * @author tommens
 */
public class Calculator {

    private static final Logger LOGGER = Logger.getLogger(Calculator.class.getName());

    /**
     * Default constructor of the class.
     * Does nothing since the class does not have any variables that need to be initialised.
     */
    public Calculator() {
        /* Intentionally left empty: Calculator currently holds no instance state
           that requires initialization. The explicit no-arg constructor documents
           this intent and allows adding initialization later without changing
           callers. */
    }

    /*
     For the moment the calculator only contains a print method and an eval method
     It would be useful to complete this with a read method, so that we would be able
     to implement a full REPL cycle (Read-Eval-Print loop) such as in Scheme, Python, R and other languages.
     To do so would require to implement a method with the following signature, converting an input string
     into an arithmetic expression:
     public Expression read(String s)
    */

    /**
     * Prints an arithmetic expression provided as input parameter.
     * @param e the arithmetic Expression to be printed
     * @see #printExpressionDetails(Expression) 
     */
    public void print(Expression e) {
        java.util.Objects.requireNonNull(e, "expression must not be null");
        LOGGER.log(java.util.logging.Level.INFO, () -> String.format("The result of evaluating expression %s", e));
        LOGGER.log(java.util.logging.Level.INFO, () -> String.format("is: %s.", evalValue(e)));
        LOGGER.log(java.util.logging.Level.INFO, () -> "");
    }

    /**
     * Prints verbose details of an arithmetic expression provided as input parameter.
     * Uses the Counter visitor to obtain counting information.
     * @param e the arithmetic Expression to be printed
     * @see #print(Expression)
     */
    public void printExpressionDetails(Expression e) {
        print(e);
        Counter counter = new Counter();
        e.accept(counter);
        LOGGER.log(java.util.logging.Level.INFO, () -> String.format(
            "It contains %d levels of nested expressions, %d operations and %d numbers.",
            counter.getCountDepth(), counter.getCountOps(), counter.getCountNbs()));
        LOGGER.log(java.util.logging.Level.INFO, () -> "");
    }

    /**
     * Evaluates an arithmetic expression and returns its result
     * @param e the arithmetic Expression to be evaluated
     * @return The result of the evaluation
     */
    public int eval(Expression e) {
        return evalValue(e).intValue();
    }

    /**
     * Evaluates an arithmetic expression and returns its result as a Value
     * @param e the arithmetic Expression to be evaluated
     * @return The Value result of the evaluation
     */
    public Value evalValue(Expression e) {
        Evaluator v = new Evaluator();
        e.accept(v);
        return v.getResult();
    }

    /*
     We could also have other methods, e.g. to verify whether an expression is syntactically correct
     public Boolean validate(Expression e)
     or to simplify some expression
     public Expression simplify(Expression e)
    */
}
