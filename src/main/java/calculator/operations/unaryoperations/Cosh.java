package calculator.operations.unaryoperations;

import calculator.Expression;
import calculator.enums.Notation;
import calculator.exceptions.IllegalConstruction;
import calculator.value.Value;
import java.util.List;

/**
 * Class representing the hyperbolic cosine operation.
 */
public class Cosh extends UnaryOperation {

    /**
     * Constructor for Cosh operation.
     * @param elist List of expressions (should contain exactly one expression for unary operation)
     * @throws IllegalConstruction if the number of expressions is not exactly one
     */
    public Cosh(List<Expression> elist) throws IllegalConstruction { super(elist); symbol = "cosh"; }

    /**
     * Constructor for Cosh operation with specified notation.
     * @param elist List of expressions (should contain exactly one expression for unary operation)
     * @param n Notation to be used for this operation
     * @throws IllegalConstruction if the number of expressions is not exactly one
     */
    public Cosh(List<Expression> elist, Notation n) throws IllegalConstruction { super(elist, n); symbol = "cosh"; }

    /**
     * Applies the hyperbolic cosine operation to the given value.
     * @param v The value to which the hyperbolic cosine operation will be applied
     * @return The result of applying the hyperbolic cosine operation to the given value
     */
    @Override
    public Value unOp(Value v) { return v.cosh(); }

    /**
     * Returns the precedence of the hyperbolic cosine operation.
     * @return The precedence level of the hyperbolic cosine operation
     */
    @Override
    public int getPrecedence() { return 4; }
}
