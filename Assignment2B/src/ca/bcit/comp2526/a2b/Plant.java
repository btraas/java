package ca.bcit.comp2526.a2b;


import java.awt.Color;

/**
 * Describes a Plant.
 * Does not move or need food.
 * 
 * @author Brayden Traas
 * @version 2016-10-22
 */
public class Plant extends Life {

    private static final Color CELL_COLOR = Color.GREEN;
  
    private static final int MIN_REPRODUCE = 1;
    private static final int MAX_REPRODUCE = 2;
    
    private static final int MIN_REPRODUCE_NEIGHBORS = 3;
    private static final int MIN_REPRODUCE_EMPTY_SPACES = 2;
    
    
    
    public Plant(final Cell location) {
        super(location, CELL_COLOR);
        this.minReproduce = MIN_REPRODUCE;
        this.maxReproduce = MAX_REPRODUCE;
        this.reproduceNeighbors = MIN_REPRODUCE_NEIGHBORS;
        this.reproduceEmptySpaces = MIN_REPRODUCE_EMPTY_SPACES;
    }
    
    /**
     *  Nothing happens to a plant at the end of it's turn.
     *   Explicitly defining that nothing happens.
     */
    public void processTurn() {
        reproduce();
    }
    
    public Class<?>[] getFoodTypes() {
        return new Class[]{};
    }
    
    protected Life create(final Cell location) {
        return new Plant(location);
    }
  
}
