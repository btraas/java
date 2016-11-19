package ca.bcit.comp2526.a2b;


import java.awt.Color;
import java.util.Random;

/**
 * An Omnivore. Requirement for A2b.
 * Subclass of Animal, implements a number of
 *  required methods.
 * 
 * @author Brayden Traas
 * @version 2016-11-11
 */
public class Omnivore extends Animal {

    private static final Color COLOR = Color.MAGENTA;
    private static final int INITIAL_FOOD = 4;
    private static final int EAT_AMOUNT = 4;

    private static final Class<?>[] FOOD_TYPES = { Life.class, WaterCell.class };
    private static final Class<?>[] INVALID_MOVE_TO_TYPES = { Omnivore.class };
    private static final Class<?>[] PREDATOR_TYPES = { Carnivore.class };
    
    private static final int MIN_MOVE = 1;
    private static final int MAX_MOVE = 1;
    
    private static final int MIN_REPRODUCE = 1;
    private static final int MAX_REPRODUCE = 2;
    
    private static final int MIN_REPRODUCE_NEIGHBORS = 1;
    private static final int MIN_REPRODUCE_EMPTY_SPACES = 3;
    private static final int MIN_REPRODUCE_FOOD = 3;
    
    
    
    public Omnivore(final Cell location) {
        super(location, COLOR, 
        		INITIAL_FOOD,
        		EAT_AMOUNT,
        		MIN_MOVE,
        		MAX_MOVE,
        		INVALID_MOVE_TO_TYPES,
        		FOOD_TYPES);
    
        this.minReproduce = MIN_REPRODUCE;
        this.maxReproduce = MAX_REPRODUCE;
        this.reproduceNeighbors = MIN_REPRODUCE_NEIGHBORS;
        this.reproduceEmptySpaces = MIN_REPRODUCE_EMPTY_SPACES;
        this.reproduceFood = MIN_REPRODUCE_FOOD;
    
    }
   
    
    /**
     * Creates a new Decision, given a Herbivore's level of intelligence,
     *  senses, predators, etc.
     * @param seed - Random object to use to make any random decisions with.
     * @param options - Array of Cells to choose from.
     * @return a Decision object.
     */
    @Override
    public Cell decideMove(final Random seed, final Cell[] options) {
        
    	
        // Uncomment a MoveDecision type below to use it instead.
        // Generally, the more parameters, the smarter the AI.
      
        // Goal = foodSupply+1; Animal will follow food even if they can't quite make it.

       // int supply = (life*MAX_MOVE) + 1;
       // return (new DistanceDecision(seed, options, 
       //         FOOD_TYPES, PREDATOR_TYPES, getCell(), supply)).decide();
         return (new NearbyDecision(seed, options, FOOD_TYPES, PREDATOR_TYPES)).decide();  
        // return (new NearbyFoodDecision(seed, options, FOOD_TYPES)).decide();
        // return (new MoveDecision(seed, options)).decide();
    }
    
    @Override 
    protected Life create(final Cell location) {
        return new Omnivore(location);
    }

}
