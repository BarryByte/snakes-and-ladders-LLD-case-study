import java.util.Random;

public class Dice {
    private Random random;
    private int sides;
    
    public Dice() {
        this(6); // Default 6-sided dice
    }
    
    public Dice(int sides) {
        this.sides = sides;
        this.random = new Random();
    }
    
    /**
     * Rolls the dice and returns result
     */
    public int roll() {
        return random.nextInt(sides) + 1; // Returns 1 to sides
    }
    
    /**
     * Rolls dice multiple times (for games with multiple dice)
     */
    public int rollMultiple(int numberOfDice) {
        int total = 0;
        for (int i = 0; i < numberOfDice; i++) {
            total += roll();
        }
        return total;
    }
    
    public int getSides() {
        return sides;
    }
    
    // /**
    //  * For testing - allows setting seed for predictable results
    //  */
    public void setSeed(long seed) {
        this.random = new Random(seed);
    }
}