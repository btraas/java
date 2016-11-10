package ca.bcit.comp2526.a2a;

import java.util.Random;

/**
 * <p>A NearbyFoodDecision. Subclass of NearbyDecision. Is-a NearbyDecision.</p>
 * 
 * <p>When given a seed, array of options,
 *  and positive types, this Decision 
 *  type decides the best option by prioritizing
 *  Positive options over all possible options.</p>
 * 
 *  <p>Positive: Any move that has a positive benefit
 *   to the user (animal). I.E. provides the 
 *   user with food.</p>
 * 
 * @author Brayden Traas
 * @version 2016-10-22
 */
public class NearbyFoodDecision extends NearbyDecision {

    public NearbyFoodDecision(final Random seed, final Cell[] options, 
          final Class<?>[] positiveLifeTypes) {
        super(seed, options, positiveLifeTypes, null);
    }
  
  
    @Override
    public Cell decide() {
    
        // Only positive matters
        
        // If no options, return null.
        if ( options.length == 0 ) {
            return null;
        }
      
        
        // Get positive options. Defined in NearbyDecision.java
        Cell[] positive = getPositiveOptions(options, positiveTypes);
        
        // If no positive was found, let parent decide from previously reduced subset.
        if (positive.length == 0) {
            // System.out.println("No positive decision found!");
            return (Cell)decide(true);
        }
        
        // Reduce options to the positive options (within range of food)
        this.options = positive;
        
        // Let parent choose (random) from remaining subset.
        // System.out.println("1+ positive decisions found!");
        return (Cell)decide(true);
    }
}
