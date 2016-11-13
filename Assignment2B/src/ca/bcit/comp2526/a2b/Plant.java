package ca.bcit.comp2526.a2b;


import java.awt.Color;

/**
 * Describes a Plant.
 * Does not move or need food.
 * 
 * @author Brayden Traas
 * @version 2016-10-22
 */
@SuppressWarnings("serial")
public class Plant extends Life {

    private static final Color CELL_COLOR = Color.GREEN;
  
    private static final int INITIAL_LIFE = 10;
    
    private static final int MIN_REPRODUCE = 1;
    private static final int MAX_REPRODUCE = 2;
    
    private static final int MIN_REPRODUCE_NEIGHBORS = 3;
    private static final int MIN_REPRODUCE_EMPTY_SPACES = 2;
    
    private static final Class<?>[] FOOD_TYPES = {};
    
    
    public Plant(final Cell location) {
        super(location, CELL_COLOR, INITIAL_LIFE, FOOD_TYPES);
        this.minReproduce = MIN_REPRODUCE;
        this.maxReproduce = MAX_REPRODUCE;
        this.reproduceNeighbors = MIN_REPRODUCE_NEIGHBORS;
        this.reproduceEmptySpaces = MIN_REPRODUCE_EMPTY_SPACES;
    }
    
    /**
     *  Nothing happens to a plant at the end of it's turn.
     *   Explicitly defining that nothing happens.
     */
    /**
     * Processes this Animal's turn. Must be implemented as per Life.
     */
    public void processTurn() {
        
        super.processTurn();
        
        // Dead.
        if (this.getCell() == null) {
        	return;
        }
        
        this.reproduce();
    }
    
    protected Life create(final Cell location) {
        return new Plant(location);
    }
  
}
