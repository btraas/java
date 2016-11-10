package ca.bcit.comp2526.a2b;


import java.awt.Color;
import java.util.Random;

/**
 * A Herbivore. Requirement for A2a.
 * Subclass of Animal, implements a number of
 *  required methods.
 * 
 * @author Brayden Traas
 * @version 2016-11-09
 */

public class Carnivore extends Animal {

    private static final Color COLOR = Color.RED;
    private static final int INITIAL_FOOD = 4;
    private static final int EAT_AMOUNT = 3;

    private static final Class<?>[] FOOD_TYPES = { Herbivore.class };
    private static final Class<?>[] MOVE_TO_LIFE_TYPES = { Herbivore.class };
    private static final Class<?>[] PREDATOR_TYPES = { };
    
    private static final int MIN_MOVE = 1;
    private static final int MAX_MOVE = 2;
    
    private static final int MIN_REPRODUCE = 1;
    private static final int MAX_REPRODUCE = 2;
    
    private static final int MIN_REPRODUCE_NEIGHBORS = 1;
    private static final int MIN_REPRODUCE_EMPTY_SPACES = 2;
    private static final int MIN_REPRODUCE_FOOD = 2;
    
    
    
    public Carnivore(final Cell location) {
        super(location, COLOR, INITIAL_FOOD);
    
        this.minReproduce = MIN_REPRODUCE;
        this.maxReproduce = MAX_REPRODUCE;
        this.reproduceNeighbors = MIN_REPRODUCE_NEIGHBORS;
        this.reproduceEmptySpaces = MIN_REPRODUCE_EMPTY_SPACES;
        this.reproduceFood = MIN_REPRODUCE_FOOD;
    
        this.minMove = MIN_MOVE;
        this.maxMove = MAX_MOVE;
    }
  
    /**
     * Defines what happens when a Herbivore eats. Simply sets the food amount.
     */
    public void eat() {
        this.foodSupply = EAT_AMOUNT;
    }
    
    /**
     *  Gets a Herbivore's valid food types.
     *  @return an array of valid types to eat.
     */
    public Class<?>[] getFoodTypes() {
        return FOOD_TYPES;
    }
    

    /**
     * Gets the Life Types a Herbivore can move into.
     * @return an array of valid types to move into.
     */
    public Class<?>[] getMoveToLifeTypes() {
        return MOVE_TO_LIFE_TYPES;
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

        int supply = foodSupply + 1;
        return (new DistanceDecision(seed, options, 
                FOOD_TYPES, PREDATOR_TYPES, getCell(), supply)).decide();
        // return (new NearbyDecision(seed, options, FOOD_TYPES, PREDATOR_TYPES)).decide();  
        // return (new NearbyFoodDecision(seed, options, FOOD_TYPES)).decide();
        // return (new MoveDecision(seed, options)).decide();
    }
    
    @Override 
    protected Life create(final Cell location) {
        return new Carnivore(location);
    }

}
