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
    
    public static boolean isEven(int in) {
        return in % 2 == 0;
    }
    
    public static boolean isOdd(int in) {
        return Math.abs(in % 2) == 1;
    }

}
