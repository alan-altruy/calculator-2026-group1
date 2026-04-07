package calculator;

import calculator.value.Value;
import java.util.List;

/**
 * Abstract class representing a unary arithmetic operation (e.g. sin, cos, ln).
 */
public abstract class UnaryOperation extends Operation {

    public UnaryOperation(List<Expression> elist) throws IllegalConstruction {
        super(elist);
        if (elist.size() != 1) {
            throw new IllegalConstruction("Unary operation requires exactly one argument");
        }
    }

    public UnaryOperation(List<Expression> elist, Notation n) throws IllegalConstruction {
        super(elist, n);
        if (elist.size() != 1) {
            throw new IllegalConstruction("Unary operation requires exactly one argument");
        }
    }

    @Override
    public Value op(Value l, Value r) {
        return unOp(l);
    }

    public abstract Value unOp(Value v);

    @Override
    public void accept(visitor.Visitor v) {
        // We override this in subclasses or Evaluator needs to know how to visit UnaryOperation
        // For simplicity, Evaluator will visit it as a normal operation, but we must override
        // Evaluator.visit(Operation) to handle unary ops, OR just return unOp in op(l,r) ignoring r?
        // Let's implement Op by ignoring 'r' to reduce the need to change Evaluator heavily!
        super.accept(v);
    }
}
