package calculator.operations.unaryoperations;

import calculator.Expression;
import calculator.enums.Notation;
import calculator.exceptions.IllegalConstruction;
import calculator.value.Value;
import java.util.List;


/**
 * Class representing the sine operation.
 */
public class Sin extends UnaryOperation {

    /**
     * Constructor for Sin operation.
     * @param elist List of expressions (should contain exactly one expression for unary operation)
     * @throws IllegalConstruction if the number of expressions is not exactly one
     */
    public Sin(List<Expression> elist) throws IllegalConstruction { super(elist); symbol = "sin"; }

    /**
     * Constructor for Sin operation with specified notation.
     * @param elist List of expressions (should contain exactly one expression for unary operation)
     * @param n Notation to be used for this operation
     * @throws IllegalConstruction if the number of expressions is not exactly one
     */
    public Sin(List<Expression> elist, Notation n) throws IllegalConstruction { super(elist, n); symbol = "sin"; }

    /**
     * Applies the sine operation to the given value.
     * @param v The value to which the sine operation will be applied
     * @return The result of applying the sine operation to the given value
     */
    @Override
    public Value unOp(Value v) { return v.sin(); }

    /**
     * Returns the precedence of the sine operation.
     * @return The precedence level of the sine operation
     */
    @Override
    public int getPrecedence() { return 4; }
}
