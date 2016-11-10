package ca.bcit.comp2526.a2b;


import java.util.ArrayList;
import java.util.Random;

/**
 * <p>A NearbyDecision. Subclass of MoveDecision (is-a MoveDecision.)</p>
 * 
 * <p>When given a seed, array of options,
 *  positive types & negative types,
 *  this Decision type decides the best option
 *  with the priority:</p>
 *  
 *  <p>Not-negative > Evasive > Positive</p>
 *  
 *  <p>Not-negative: Any move that doesn't instantly
 *   kill the user (Animal).</p>
 *   
 *  <p>Evasive: Any move that doesn't put the user
 *   (animal) in danger for the next round.</p>
 * 
 *  <p>Positive: Any move that has a positive benefit
 *   to the user (animal). I.E. provides the 
 *   user with food.</p>
 *   
 *   
 *   <p>TODO: Favor positive > evasive IF no food 
 *    this round = death. And possibly 2+ turns
 *    to death as well?</p>
 *    
 *  
 * @author Brayden Traas
 * @version 2016-10-22
 */
public class NearbyDecision extends MoveDecision<Cell> {
  
    private static final int EVASIVE_BUFFER = Settings.getInt("evasivebufferDistance");
  
    // Classes we want to get to
    protected Class<?>[] positiveTypes;
    
    // Classes we want to avoid
    protected Class<?>[] negativeTypes;
    
    /**
     * Creates a NearbyDecision when given a seed, options, and new constraints.
     * 
     * @param seed to use for Random decisions
     * @param options to choose from
     * @param positiveTypes types to target
     * @param negativeTypes types to avoid
     */
    public NearbyDecision(final Random seed, final Cell[] options, 
                          final Class<?>[] positiveTypes, 
                          final Class<?>[] negativeTypes) {
        super(seed, options);
        this.positiveTypes = positiveTypes;
        this.negativeTypes = negativeTypes;
    }
    
    /**
     * Creates a subset of options (positive only).
     * 
     * @param options - all possible options given
     * @param positiveTypes to filter by
     * @return a subset of only positive options
     */
    public static Cell[] getPositiveOptions(final Cell[] options, 
          final Class<?>[] positiveTypes) {
        ArrayList<Cell> newOptions = new ArrayList<Cell>();
        
        for (int i = 0; i < options.length; i++) {
          
            // If this Cell is empty, continue to next Cell
            if (options[i].getLife() == null) {
                continue;
            }
            for (int j = 0; j < positiveTypes.length; j++) {
                
                // If the Life in this Cell is a positive option
                if (positiveTypes[j].isInstance(options[i].getLife())) {
                    newOptions.add(options[i]);
                }
            }
        }
        
        return newOptions.toArray(new Cell[newOptions.size()]);
        
    }
    
    
    
    
    /**
     *  Instance method, alternative to static getPositiveOptions() above.
     * @return a subset of only positive options for this Decision object
     */
    public Cell[] getPositiveOptions() {
        return getPositiveOptions(options, positiveTypes);
    }
    
    
    /**
     * Gets the non-negative options for this turn.
     * 
     * @param options - all options to choose from.
     * @param negativeLifeTypes - types to avoid.
     * @return a subset of options - only the non-negative ones.
     */
    public static Cell[] getNotNegativeOptions(final Cell[] options, 
        final Class<?>[] negativeLifeTypes) {
      
        ArrayList<Cell> newOptions = new ArrayList<Cell>();
      
        for (int i = 0; i < options.length; i++) {
        
            // If this Cell is empty, add this and continue to next Cell
            if (options[i].getLife() == null) {
                newOptions.add(options[i]);
                continue;
            }
            for (int j = 0; j < negativeLifeTypes.length; i++) {
                
                // If the Life in this Cell is a negative option, don't add to non-negative options
                if (negativeLifeTypes[j].isInstance(options[i].getLife())) {
                    continue;
                }
            }
            newOptions.add(options[i]);
        }
  
      
        return newOptions.toArray(new Cell[newOptions.size()]);
      
    }
    
    /**
     * Gets the non-negative options for this Decision.
     * 
     * @return a subset of options - only the non-negative ones.
     */
    public Cell[] getNotNegativeOptions() {
        return getNotNegativeOptions(options, negativeTypes);
    }
    
    /**
     * Gets the non-negative options for this turn.
     * 
     * @param options - all options to choose from.
     * @param negativeLifeTypes - types to avoid.
     * @return a subset of options - only the non-negative ones.
     */
    public static Cell[] getNonNegativeOptions(final Cell[] options, 
        final Class<?>[] negativeLifeTypes) {
      
        return getNotNegativeOptions(options, negativeLifeTypes);
    }
    
    /**
     * Gets the non-negative options for this Decision.
     * 
     * @return a subset of options - only the non-negative ones.
     */
    public Cell[] getNonNegativeOptions() {
        return getNonNegativeOptions(options, negativeTypes);
    }
    
    
    
    /**
     * Gets the evasive options from the given options.
     * 
     * @param options to choose from
     * @param negativeLifeTypes options to avoid
     * @param dist to keep from negative options
     * @return all options that are far enough away from enemies.
     */
    public static Cell[] getEvasiveOptions(final Cell[] options, 
        final Class<?>[] negativeLifeTypes, int dist) {
        
        ArrayList<Cell> newOptions = new ArrayList<Cell>();
        
        // For each move option, see if there's a negative Life type in the specified range.
        
        // For each given move option
        for (int i = 0; i < options.length; i++) {
            
            // Find ALL possible moves from this option's location
            Class<?>[] moveToClasses = {Object.class};
            Cell[] nearby = options[i].getMoveToPossibilities(moveToClasses, 1, dist);
          
            //System.out.println("Cells nearby: "+nearby.length);
            
            
            Cell[] safeMoves = NearbyDecision.getNotNegativeOptions(nearby, negativeLifeTypes);
            
            //System.out.println("Safe Cells nearby: "+safeMovesInRange.length+"\n");
            
            // In other words: No negative options from here
            if (safeMoves.length == nearby.length) {
                newOptions.add(options[i]);
            }
            
        }
        
        return newOptions.toArray(new Cell[newOptions.size()]);
      
      
    }
    
    /**
     * Reduces decisions to the best subset (of strategically equivalent choices).
     * @return a reduced subset of decisions
     */
    public Cell[] reduceDecisions() {
        
        // Get non-negative options
        Cell[] nonNegative = getNotNegativeOptions(options, negativeTypes);
        
        // If no non-negative was found, let parent decide (random)
        if ( nonNegative.length == 0 ) {
            // System.out.println("No non-negative decision found!");
            return options;
        }
        
        // Reduce options to the non-negative options
        this.options = nonNegative;
        
        // Get evasive options
        //int distance = Settings.getInt("EvasiveBufferDistance");
        Cell[] evasive = getEvasiveOptions(nonNegative, negativeTypes, EVASIVE_BUFFER);
        
        // If no evasive was found, let parent decide from previously reduced subset.
        if ( evasive.length == 0 ) {
            // System.out.println("No evasive decision found!");
            return options;
        }
        
        // Reduce options to the evasive options (not within range of an enemy)
        this.options = evasive;
        
        // Get positive options
        Cell[] positive = getPositiveOptions(options, positiveTypes);
        
        // If no positive was found, let parent decide from previously reduced subset.
        if (positive.length == 0) {
            // System.out.println("No positive decision found!");
            return options;
        }
        
        // Reduce options to the positive options (within range of food)
        this.options = positive;
        
        return options;
    }
    
    /**
     * Decides a Cell.
     * @return the Cell that was decided.
     */
    public Cell decide() {
        
        // Pritory = Not-negative > Evasive > Positive
      
        // If no options, return null.
        if ( options.length == 0 ) {
            return null;
        }
      
        
        this.reduceDecisions();
        
        // Let parent choose (random) from remaining subset.
        // System.out.println("1+ positive decisions found!");
        return (Cell)super.decide();
        
    }
    
  
}
