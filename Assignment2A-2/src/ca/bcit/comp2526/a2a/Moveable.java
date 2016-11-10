package ca.bcit.comp2526.a2a;

import java.util.Random;

/**
 * <p>A Moveable Interface.</p>
 * 
 * <p>Defines a few methods that Moveable
 *  objects (Animals) must implement.</p>
 *  
 * <p>This class uses parameterized types for the type-of the destination.</p>
 * 
 * @author Brayden Traas
 * @version 2016-10-22
 */
public interface Moveable<DestinationT> {
  
    /**
     * Handles what happens when this object "moves".
     */
    public void move();
    
    /**
     *  Get Life types we can move into.
     * @return an array of Class objects that this type can move "into".
     */
    public Class<?>[] getMoveToLifeTypes();
    
    /**
     *  Get the distance this object can move.
     * @return the distance this object can move.
     */
    public int getMoveDistance();
    

    /**
     * Decide an Object to move to.
     * 
     * @param seed - Random object to make random decisions from.
     * @param options - Array of Objects to choose from.
     * @return a destination Object.
     */
    public DestinationT decideMove(final Random seed, DestinationT[] options);
    
}
