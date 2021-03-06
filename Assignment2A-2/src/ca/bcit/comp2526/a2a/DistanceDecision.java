package ca.bcit.comp2526.a2a;

import java.util.Random;

/**
 * Creates a Distance decision. Prioritizes nearby decisions,
 *  but if all nearby decisions are equivalent, it will find
 *  a goal and make a decision to be closer to the goal.
 *  
 * @author Brayden Traas
 * @version 2016-11-01
 *
 */
public class DistanceDecision extends NearbyDecision {

    private static final int MAX_SENSE_DISTANCE = Settings.getInt("maxSenseDistance");
  
    private Cell current;
    private int maxDistance;
  
    /**
     * Creates a DistanceDecision when given the parameters.
     * 
     * @param seed to make Random decisions from
     * @param options total to choose from
     * @param positiveLifeTypes to target
     * @param negativeLifeTypes to avoid
     * @param current (location)
     * @param maxDistance to sense food / predators
     */
    public DistanceDecision(final Random seed, final Cell[] options, 
        final Class<?>[] positiveLifeTypes,
        final Class<?>[] negativeLifeTypes, final Cell current, int maxDistance) {
        
        super(seed, options, positiveLifeTypes, negativeLifeTypes);
        this.current = current;
        this.maxDistance = maxDistance;
        
        if (maxDistance > MAX_SENSE_DISTANCE) {
            this.maxDistance = MAX_SENSE_DISTANCE;
        }
    }

    
    // Tricky, we decide a goal, and then from there decide next move.
    
    /*
     * Find a goal to move towards.
     * @return a target Cell to move towards, multi-turn.
     */
    private Cell createGoal() {
        NearbyDecision outlook; 
        Cell[] outlookOptions;
        Cell[] positiveOptions;
    
        for (int distance = 2; distance <= maxDistance; distance++) {
            outlookOptions = current.getMoveToPossibilities(null, distance);
            outlook = new NearbyFoodDecision(seed, outlookOptions, positiveTypes);
      
            positiveOptions = outlook.getPositiveOptions();
            if (positiveOptions.length > 0) {
              
                //System.out.println("Positive options:");
                //Cell.print(positiveOptions);
              
                // Since this is a NearbyFoodDecision, decisions are based only on 
                // cells that contain food.
                
                return outlook.decide();
            } 
        }
        
        return null;
        
    }
    
    
    /**
     * Decide a move this turn.
     * @return a Cell to move to.
     */
    public Cell decide() {
      
        if ( options == null || options.length == 0 ) {
            return null;
        }
        
        // Reduce decisions to best decisions for "this turn".
        super.reduceDecisions();
        
        
        // If there's a positive option this turn, choose that.
        if (getPositiveOptions().length > 0 || current == null) {
            
            return super.decide(); 
        }
        
        
        // Else, look for the best decision for 2 turns from now.

        Life currentLife = current.getLife();
        //System.out.println("\nThis is a: "+currentLife.getClass().getSimpleName());
        
        if (maxDistance < 2 || currentLife == null || !(currentLife instanceof Moveable) ) {
            return super.decide();
        }
        
        // Create a future goal, up to the max distance given in constructor.
        // (ideally, this should be the lifetime)
        Cell goal = createGoal();
        
        if (goal == null) {
            return super.decide();
        }
        
        Cell closest =  goal.closest(options);
        
        if (closest == null) {
            return super.decide();
        }
        
        return closest;
      
    }
    
  
  
}
