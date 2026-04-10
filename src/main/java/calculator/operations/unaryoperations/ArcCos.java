package calculator.operations.unaryoperations;

import calculator.Expression;
import calculator.enums.Notation;
import calculator.exceptions.IllegalConstruction;
import calculator.value.Value;
import java.util.List;

/**
 * Class representing the arc cosine operation.
 */
public class ArcCos extends UnaryOperation {

    /**
     * Constructor for ArcCos operation.
     * @param elist List of expressions (should contain exactly one expression for unary operation)
     * @throws IllegalConstruction if the number of expressions is not exactly one
     */
    public ArcCos(List<Expression> elist) throws IllegalConstruction { super(elist); symbol = "arccos"; }

    /**
     * Constructor for ArcCos operation with specified notation.
     * @param elist List of expressions (should contain exactly one expression for unary operation)
     * @param n Notation to be used for this operation
     * @throws IllegalConstruction if the number of expressions is not exactly one
     */
    public ArcCos(List<Expression> elist, Notation n) throws IllegalConstruction { super(elist, n); symbol = "arccos"; }

    /**
     * Applies the arc cosine operation to the given value.
     * @param v The value to which the arc cosine operation will be applied
     * @return The result of applying the arc cosine operation to the given value
     */
    @Override
    public Value unOp(Value v) { return v.arccos(); }

    /**
     * Returns the precedence of the arc cosine operation.
     * @return The precedence level of the arc cosine operation
     */
    @Override
    public int getPrecedence() { return 4; }
}
