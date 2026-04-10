package calculator.operations.unaryoperations;

import calculator.Expression;
import calculator.enums.Notation;
import calculator.exceptions.IllegalConstruction;
import calculator.value.Value;
import java.util.List;

/**
 * Class representing the tangent operation.
 */
public class Tan extends UnaryOperation {

    /**
     * Constructor for Tan operation.
     * @param elist List of expressions (should contain exactly one expression for unary operation)
     * @throws IllegalConstruction if the number of expressions is not exactly one
     */
    public Tan(List<Expression> elist) throws IllegalConstruction { super(elist); symbol = "tan"; }

    /**
     * Constructor for Tan operation with specified notation.
     * @param elist List of expressions (should contain exactly one expression for unary operation)
     * @param n Notation to be used for this operation
     * @throws IllegalConstruction if the number of expressions is not exactly one
     */
    public Tan(List<Expression> elist, Notation n) throws IllegalConstruction { super(elist, n); symbol = "tan"; }

    /**
     * Applies the tangent operation to the given value.
     * @param v The value to which the tangent operation will be applied
     * @return The result of applying the tangent operation to the given value
     */
    @Override
    public Value unOp(Value v) { return v.tan(); }

    /**
     * Returns the precedence of the tangent operation.
     * @return The precedence level of the tangent operation
     */
    @Override
    public int getPrecedence() { return 4; }
}
