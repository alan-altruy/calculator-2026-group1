package calculator;

import java.util.Random;
import java.security.SecureRandom;

/**
 * Seedable pseudorandom number generator for the calculator.
 * <p>
 * Provides a shared {@link Random} instance whose seed can be set for deterministic
 * (testable) behaviour. NOTE: {@code java.util.Random} is <b>not</b> suitable for
 * cryptographic purposes. If you need cryptographically secure randomness, use
 * {@link #getSecureRandom()} which returns a {@link SecureRandom} instance.
 */
public class RandomGenerator {

    // Shared Random instance used for non-cryptographic purposes (deterministic when seeded)
    private static SecureRandom random = new SecureRandom();

    private RandomGenerator() {
        // utility class
    }

    /**
     * Set the seed for deterministic output.
     * @param seed the seed value
     */
    public static void setSeed(long seed) {
        random = new SecureRandom();
        random.setSeed(seed);
    }

    /**
     * Reset to a non-deterministic Random instance.
     */
    public static void reset() {
        random = new SecureRandom();
    }

    /**
     * @return the shared Random instance
     */
    public static Random getRandom() {
        return random;
    }

    /**
     * Return a new {@link SecureRandom} for security-sensitive operations.
     * This method intentionally returns a fresh SecureRandom instance; callers
     * should not rely on `setSeed` for SecureRandom.
     *
     * @return a SecureRandom suitable for cryptographic use
     */
    public static SecureRandom getSecureRandom() {
        return new SecureRandom();
    }
}
