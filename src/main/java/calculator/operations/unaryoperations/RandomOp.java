package calculator.operations.unaryoperations;

import calculator.Expression;
import calculator.Main;
import calculator.RandomGenerator;
import calculator.enums.Notation;
import calculator.enums.NumberDomain;
import calculator.exceptions.IllegalConstruction;
import calculator.value.*;

import java.util.List;
import java.util.Random;

/**
 * Pseudorandom number generator operation.
 * <p>
 * Behaviour depends on the current number domain:
 * <ul>
 *   <li><b>INTEGER</b>  – random integer in [0, n] where n is the argument</li>
 *   <li><b>RATIONAL</b> – random rational a/b where a ∈ [0, n] and b ∈ [1, n]</li>
 *   <li><b>REAL</b>     – random real in [0, 1) (argument is ignored)</li>
 *   <li><b>COMPLEX</b>  – random complex a + bi where a, b ∈ [0, 1) (argument is ignored)</li>
 * </ul>
 */
public class RandomOp extends UnaryOperation {

    public RandomOp(List<Expression> elist) throws IllegalConstruction {
        super(elist);
        symbol = "random";
    }

    public RandomOp(List<Expression> elist, Notation n) throws IllegalConstruction {
        super(elist, n);
        symbol = "random";
    }

    @Override
    public Value unOp(Value v) {
        Random rng = RandomGenerator.getRandom();
        NumberDomain domain = Main.getCurrentDomain();

        switch (domain) {
            case RATIONAL: {
                int bound = v.intValue();
                if (bound <= 0) throw new ArithmeticException("random bound must be positive for rational");
                int a = rng.nextInt(bound + 1);    // numerator in [0, bound]
                int b = rng.nextInt(bound) + 1;    // denominator in [1, bound]
                return new RationalValue(a, b);
            }
            case REAL: {
                return new RealValue(rng.nextDouble());
            }
            case COMPLEX: {
                double re = rng.nextDouble();
                double im = rng.nextDouble();
                return new ComplexValue(re, im);
            }
            default: { // INTEGER and fallback
                int bound = v.intValue();
                if (bound < 0) throw new ArithmeticException("random bound must be non-negative");
                return new IntegerValue(rng.nextInt(bound + 1));
            }
        }
    }

    @Override
    public int getPrecedence() {
        return 4;
    }
}
