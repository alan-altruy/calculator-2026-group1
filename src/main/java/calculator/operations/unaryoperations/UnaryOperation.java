package calculator.operations.unaryoperations;

import calculator.Expression;
import calculator.enums.Notation;
import calculator.operations.Operation;
import calculator.exceptions.IllegalConstruction;
import calculator.value.Value;
import java.util.List;

/**
 * Abstract class representing a unary arithmetic operation (e.g. sin, cos, ln).
 */
public abstract class UnaryOperation extends Operation {

    // Constant for the number of arguments required for a unary operation
    private static final int UNARY_ARG_COUNT = 1;

    /**
     * Constructor for UnaryOperation.
     * @param elist List of expressions (should contain exactly one expression for unary operation)
     * @throws IllegalConstruction if the number of expressions is not exactly one
     */
    protected UnaryOperation(List<Expression> elist) throws IllegalConstruction {
        super(elist);
        if (elist.size() != UNARY_ARG_COUNT) {
            throw new IllegalConstruction("Unary operation requires exactly one argument");
        }
    }

    /**
     * Constructor for UnaryOperation with specified notation.
     * @param elist List of expressions (should contain exactly one expression for unary operation)
     * @param n Notation to be used for this operation
     * @throws IllegalConstruction if the number of expressions is not exactly one
     */
    protected UnaryOperation(List<Expression> elist, Notation n) throws IllegalConstruction {
        super(elist, n);
        if (elist.size() != UNARY_ARG_COUNT) {
            throw new IllegalConstruction("Unary operation requires exactly one argument");
        }
    }

    /**
     * Applies the unary operation to the given value.
     * @param l The value to which the unary operation will be applied (the left operand)
     * @param r The right operand (not used in unary operations)
     * @return The result of applying the unary operation to the given value
     */
    @Override
    public Value op(Value l, Value r) {
        return unOp(l);
    }

    /**
     * Abstract method to be implemented by subclasses to define the specific unary operation.
     * @param v The value to which the unary operation will be applied
     * @return The result of applying the unary operation to the given value
     */
    public abstract Value unOp(Value v);
}
