package ca.bcit.comp2526.a2a;

import java.awt.Color;
import java.util.Random;

/**
 * A Herbivore. Requirement for A2a.
 * Subclass of Animal, implements a number of
 *  required methods.
 * 
 * @author Brayden Traas
 * @version 2016-10-22
 */

public class Herbivore extends Animal {

    private static final Color COLOR = Color.YELLOW;
    private static final int INITIAL_FOOD = 4;
    private static final int EAT_AMOUNT = 5;

    private static final Class<?>[] FOOD_TYPES = { Plant.class };
    private static final Class<?>[] MOVE_TO_LIFE_TYPES = { Plant.class };
    private static final Class<?>[] PREDATOR_TYPES = {};
    
    public Herbivore(final Cell location) {
        super(location, COLOR, INITIAL_FOOD);
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

}
