package ca.bcit.comp2526.a2b;


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
    private static final int carnivoreChance = Settings.getInt("CarnivoreChance");
    private static final int omnivoreChance  = Settings.getInt("OmnivoreChance");
    
    private static final int waterChance = Settings.getInt("WaterChance");

    private Creator() {} // Unable to instantiate

    /**
     * Creates a new Life, with the random static variables & seed.
     * 
     * @param loc - the Cell this Life (or null) will be placed in
     * @param seed - the seed from this World (for all Random events)
     * @return a new Life object or null based on chances & seed
     */
    public static Life createLife(final Cell loc, final Random seed) {
      
    	int plantPct = plantChance;
    	int herbivorePct = herbivoreChance + plantPct;
    	int carnivorePct = carnivoreChance + herbivorePct;
    	int omnivorePct  = omnivoreChance  + carnivorePct;
    	
    	int percent = getNextPercent(seed);
    	if (percent <= plantPct) {
            return new Plant(loc);
        } else if (percent <= herbivorePct) {
            return new Herbivore(loc);
        } else if (percent <= carnivorePct) {
            return new Carnivore(loc);
        } else if (percent <= omnivorePct) {
        	return new Omnivore(loc);
        }
      
        
        // Return null if we've made it this far.
        return null;
    }
    
    public static Cell createCell(final Cell origin, final Random seed) {
    	if (origin == null) {
    		return null;
    	}
    	
    	int waterPct = waterChance;
    	
    	int percent = getNextPercent(seed);
    	
    	if (percent <= waterPct) {
    	
    		if (origin instanceof HexCell) {
    			return new WaterHexCell((HexCell)origin);
    		} else if (origin instanceof SquareCell) {
    			return new WaterSquareCell((SquareCell)origin);
    		}
    	}
    	
    	return origin;
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
