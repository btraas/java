package ca.bcit.comp2526.a2b;

/**
 * A simple utilities class.
 * 
 * @author Brayden Traas
 * @version 1
 *
 */
public final class Util {
    
    private Util() {}
    
    /**
     * Decides if the integer is even.
     * @param in - int value to test.
     * @return true if the integer is even.
     */
    public static boolean isEven(int in) {
        return in % 2 == 0;
    }
    
    /**
     * Decides if the integer is odd.
     * @param in - int value to test.
     * @return true if the integer is odd.
     */
    public static boolean isOdd(int in) {
        return Math.abs(in % 2) == 1;
    }

}
