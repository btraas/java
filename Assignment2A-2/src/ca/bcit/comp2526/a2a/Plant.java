package ca.bcit.comp2526.a2a;

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
  
    public Plant(final Cell location) {
        super(location, CELL_COLOR);
    }
    
    /**
     *  Nothing happens to a plant at the end of it's turn.
     *   Explicitly defining that nothing happens.
     */
    public void processTurn() {
        
    }
  
}
