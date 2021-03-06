package ca.bcit.comp2526.a2b;


import java.util.ArrayList;
import java.util.Arrays;
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
 * @version 2
 */
public class NearbyDecision extends MoveDecision<Cell> {
  
    
    private static final String NO_PREDATORS_UNKNOWN_ERROR = 
            "ERROR: unsafe move but no predators... this shouldn't happen!";
    private static final int EVASIVE_BUFFER = Settings.getInt("evasivebufferDistance");
    private static final Terrain SAFE_TERRAIN = Terrain.DEFAULT;
  
    // Classes we want to get to
    protected Matter[] positiveTypes;
    
    // Classes we want to avoid
    protected Matter[] negativeTypes;
    
    /**
     * Creates a NearbyDecision when given a seed, options, and new constraints.
     * 
     * @param seed to use for Random decisions
     * @param options to choose from
     * @param positiveTypes types to target
     * @param negativeTypes types to avoid
     */
    public NearbyDecision(final Random seed, final Cell[] options, 
                          final Matter[] positiveTypes, 
                          final Matter[] negativeTypes) {
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
    public static Cell[] optionsWith(final Cell[] options, 
          final Matter[] positiveTypes) {
        ArrayList<Cell> newOptions = new ArrayList<Cell>();
        
        for (int i = 0; i < options.length; i++) {
            
            /* 
             * has(this, that) -> aka has a life that is-a
             * positiveType, and not-a null object.
             *
             */
            if (options[i].has(positiveTypes) 
                   || options[i].is(positiveTypes)) {
                newOptions.add(options[i]);
            }
            
        }
        
        return newOptions.toArray(new Cell[newOptions.size()]);
        
    }
    
    
    
    
    /**
     *  Instance method, alternative to static getPositiveOptions() above.
     * @return a subset of only positive options for this Decision object
     */
    public Cell[] getPositiveOptions() {
        return optionsWith(options, positiveTypes);
    }
    
    
    /**
     * Gets the non-negative options for this turn.
     * 
     * @param options - all options to choose from.
     * @param negativeTypes - types to avoid.
     * @return a subset of options - only the non-negative ones.
     */
    public static Cell[] optionsWithout(final Cell[] options, 
        final Matter[] negativeTypes) {
      
        ArrayList<Cell> newOptions = new ArrayList<Cell>();
      
        for (int i = 0; i < options.length; i++) {
            
            if (options[i].hasOrIs(negativeTypes)) {
                continue;
            }
            // Else add this option.
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
        return optionsWithout(options, negativeTypes);
    }
    
    /**
     * Gets the non-negative options for this turn.
     * 
     * @param options - all options to choose from.
     * @param negativeLifeTypes - types to avoid.
     * @return a subset of options - only the non-negative ones.
     */
    public static Cell[] getNonNegativeOptions(final Cell[] options, 
        final Matter[] negativeLifeTypes) {
      
        return optionsWithout(options, negativeLifeTypes);
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
     *  Uses incompatible Life & Cell types to perform
     *  evasive maneuvers as well. i.e. a Carnivore 
     *  cannot move into a WaterCell, so a WaterCell
     *  is determined to be evasive for a Herbivore
     *  even if a Carnivore is within range, because
     *  the carnivore cannot move here. However, 
     *  since an Omnivore can move into a WaterCell,
     *  a WaterCell isn't considered evasive if an
     *  omnivore is in range.
     * 
     * @param options to choose from
     * @param negativeTypes options to avoid
     * @param dist to keep from negative options
     * @return all options that are far enough away from enemies.
     */
    public static Cell[] getEvasiveOptions(final Cell[] options, 
        final Matter[] negativeTypes, int dist) {
        
        ArrayList<Cell> newOptions = new ArrayList<Cell>();
        
        // For each move option, see if there's a negative Life type in the specified range.
        
        // For each given move option
        for (int i = 0; i < options.length; i++) {
        
            // Find ALL possible moves from this option's location
            Cell[] nearby = options[i].getNearbyCells(1, dist);
          
            //System.out.println("Cells nearby: "+nearby.length);
            
            
            Cell[] safeMoves = NearbyDecision.optionsWithout(nearby, negativeTypes);
            
            //System.out.println("Safe Cells nearby: "+safeMovesInRange.length+"\n");
            
            
            boolean safe = false;
            
            // In other words: No negative options from here
            if (safeMoves.length == nearby.length) {
                safe = true;
            } else if (options[i].getTerrain() != SAFE_TERRAIN) {
                // Perform a check to see if the predators can move here.
                
                // Safe until proven otherwise.
                safe = true;
                
                ArrayList<Cell> safeList = new ArrayList<Cell>(Arrays.asList(safeMoves));
                
                // For each cell nearby this "potentially unsafe" cell
                for (int j = 0; j < nearby.length; j++) {
                    
                    // If this nearby Cell not in the safe list
                    if (!safeList.contains(nearby[j])) {
                    
                        // If this Cell itself is unsafe...
                        if (nearby[j].is(negativeTypes) && !nearby[j].has(negativeTypes)) {
                            continue;
                        }
                    
                        ArrayList<Life> predatorsHere = nearby[j].getLives(negativeTypes);
                    
                        if (predatorsHere.size() == 0) {
                            throw new RuntimeException(
                             NO_PREDATORS_UNKNOWN_ERROR);
                        }
                    
                        for (Life occupier : predatorsHere) {
                            
                            // If the original move-to Cell is not incompatible with this predator.
                            //  i.e. If the predator in the nearby cell
                            //  can move to this type of Cell.
                            if (!options[i].is(occupier.getIncompatibleTypes())) {
                                safe = false;
                            }
                        }
                    }
                }
            }
            
            if (safe) {
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
        Cell[] nonNegative = optionsWithout(options, negativeTypes);
        
        //System.out.println(" nonNegative: "+Arrays.asList(nonNegative));
        
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
        
        // System.out.println(" evasive: "+Arrays.asList(evasive));
        
        // If no evasive was found, let parent decide from previously reduced subset.
        if ( evasive.length == 0 ) {
            // System.out.println("No evasive decision found!");
            return options;
        }
        
        // Reduce options to the evasive options (not within range of an enemy)
        this.options = evasive;
        
        // Get positive options
        Cell[] positive = optionsWith(options, positiveTypes);
        
        //System.out.println(" positive: "+Arrays.asList(positive));
        
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
