package calculator;

import java.util.Random;

/**
 * Seedable pseudorandom number generator for the calculator.
 * Provides a global Random instance whose seed can be set
 * for deterministic (testable) behaviour.
 */
public class RandomGenerator {

    private static Random random = new Random();

    private RandomGenerator() {
        // utility class
    }

    /**
     * Set the seed for deterministic output.
     * @param seed the seed value
     */
    public static void setSeed(long seed) {
        random = new Random(seed);
    }

    /**
     * Reset to a non-deterministic Random instance.
     */
    public static void reset() {
        random = new Random();
    }

    /**
     * @return the shared Random instance
     */
    public static Random getRandom() {
        return random;
    }
}
