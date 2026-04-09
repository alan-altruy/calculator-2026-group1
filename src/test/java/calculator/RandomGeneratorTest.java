package calculator;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.Random;

import org.junit.jupiter.api.Test;

class RandomGeneratorTest {
    
    @Test
    void testSetSeed() {
        long seed = 12345L;
        RandomGenerator.setSeed(seed);
        Random rand1 = RandomGenerator.getRandom();
        Random rand2 = RandomGenerator.getRandom();
        assertNotEquals(rand1.nextInt(), rand2.nextInt());
    }

    @Test
    void testReset() {
        long seed = 54321L;
        RandomGenerator.setSeed(seed);
        Random rand1 = RandomGenerator.getRandom();
        RandomGenerator.reset();
        Random rand2 = RandomGenerator.getRandom();
        assertNotEquals(rand1.nextInt(), rand2.nextInt());
    }
}
