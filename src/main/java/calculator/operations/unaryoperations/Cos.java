package calculator.operations.unaryoperations;

import calculator.Expression;
import calculator.enums.Notation;
import calculator.exceptions.IllegalConstruction;
import calculator.value.Value;
import java.util.List;

/**
 * Class representing the cosine operation.
 */
public class Cos extends UnaryOperation {

    /**
     * Constructor for Cos operation.
     * @param elist List of expressions (should contain exactly one expression for unary operation)
     * @throws IllegalConstruction if the number of expressions is not exactly one
     */
    public Cos(List<Expression> elist) throws IllegalConstruction { super(elist); symbol = "cos"; }

    /**
     * Constructor for Cos operation with specified notation.
     * @param elist List of expressions (should contain exactly one expression for unary operation)
     * @param n Notation to be used for this operation
     * @throws IllegalConstruction if the number of expressions is not exactly one
     */
    public Cos(List<Expression> elist, Notation n) throws IllegalConstruction { super(elist, n); symbol = "cos"; }

    /**
     * Applies the cosine operation to the given value.
     * @param v The value to which the cosine operation will be applied
     * @return The result of applying the cosine operation to the given value
     */
    @Override
    public Value unOp(Value v) { return v.cos(); }

    /**
     * Returns the precedence of the cosine operation.
     * @return The precedence level of the cosine operation
     */
    @Override
    public int getPrecedence() { return 4; }
}
