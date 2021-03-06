package ca.bcit.comp2526.a2a;

import java.util.Random;

/**
 * Creates a Life or no life for a Cell.
 * 
 * @author Brayden Traas
 * @version 2016-11-01
 *
 */
public class Creator {

    private static final int minChance = 0;
    private static final int maxChance = 100;

    // Get chances from the Settings class (Settings file)
    private static final int plantChance = Settings.getInt("PlantChance");
    private static final int herbivoreChance = Settings.getInt("HerbivoreChance");

    private Creator() {} // Unable to instantiate

    /**
     * Creates a new Life, with the random static variables & seed.
     * 
     * @param loc - the Cell this Life (or null) will be placed in
     * @param seed - the seed from this World (for all Random events)
     * @return a new Life object or null based on chances & seed
     */
    public static Life createLife(final Cell loc, final Random seed) {
      
        int percent = getNextPercent(seed);
        if (percent <= herbivoreChance) {
            return new Herbivore(loc);
        }
      
        // Get a new percent, because there's overlap with the previous check.
        percent = getNextPercent(seed);
        if (percent <= plantChance) {
            return new Plant(loc);
        }
        
        
        // Return null if we've made it this far.
        return null;
    }
  
    /*
     * Gets a new percent when given a seed.
     * 
     * @param seed to feed from
     * @return a percentage (integer)
     */
    private static int getNextPercent(final Random seed) {
        return seed.nextInt((maxChance - minChance) + 1) + 1;
    }
}
