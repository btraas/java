package ca.bcit.comp2526.a2a;

import java.awt.Color;
import java.util.Random;

/**
 * <p>An Animal.
 * Subclass of Life, but also abstract.</p>
 * 
 * <p>Defines some further abstract methods
 *  that must be implemented by child classes,
 *  must have food, and is Moveable.</p>
 * 
 * @author Brayden Traas
 * @version 2016-10-22
 */
public abstract class Animal extends Life implements Moveable<Cell> {

    private static final int MOVE_DISTANCE = 1;
    private static final String NO_MOVES =              "No moves found...";
    private static final String UNHANDLED_SCENARIO =    "Unable to handle scenario!";
    private static final String ATTEMPTED_MOVE =        "attempted to move a";
    private static final String FROM =                  "from Cell";
    private static final String TO =                    "to Cell";
    private static final String CONTAINING =            "containing a";
  
    protected int foodSupply;
  
    public Animal(Cell location, Color color, int initialFood) {
        super(location, color);
        foodSupply = initialFood;  
    }

    // Child classes must define
    
    /**
     * This Animal performs its eat action.
     *  Can include updating food supply, 
     *  reproduction etc.
     */
    public abstract void eat();
    
    
    /**
     *  Gets the types that the food can be eaten
     *   by this type.
     * @return an array of viable classes.
     */
    public abstract Class<?>[] getFoodTypes();
    
    /**
     *  Gets a default move distance for Animals. 
     *   Can be overridden by child classes.
     *  
     *  @return MOVE_DISTANCE - how many tiles this Life can move.
     *   
     */
    public int getMoveDistance() {
        return MOVE_DISTANCE;
    }
    
    /**
     * Processes this Animal's turn. Must be implemented as per Life.
     */
    public void processTurn() {
        
        // Decrement food and
        //  die if we've run out of food.
        if (--foodSupply < 0) {
            this.getCell().setLife(null);
            this.destroy();
            return;
        }
        
        this.move();
    }
    
    /**
     * Gets all the possible move options for this animal.
     * @return an array of new locations (Cell objects)
     */
    protected final Cell[] getMoveOptions() {
        
        // getMoveToLifeTypes() and getMoveDistance() must be defined in child class, 
        //  as per interface.
        return this.getCell().getMoveToPossibilities(getMoveToLifeTypes(), getMoveDistance());
    }
    
    
    /**
     * Creates a MoveDecision.
     * 
     * <p>Can be overridden by a child class, to specify
     * a certain decision type per Animal type.</p>
     * 
     * <p>This, the default method, chooses randomly
     * from all possible options.</p>
     * 
     * <p>Since we are using the raw MoveDecision Class, we must supply a type to be passed (Cell).
     *  MoveDecision is not statically only work for Cell objects, it works for whatever type is
     *  passed via the parameterized type. 
     *  Other child classes of MoveDecision force the Cell type.</p>
     * 
     * 
     * @param seed - Random object from the World object
     * @param options - All available options for moving
     * @return a MoveDecision object
     */
    @Override
    public Cell decideMove(final Random seed, final Cell[] options) {
        return (new MoveDecision<Cell>(seed, options)).decide();
    }
    
  
    /**
     * Moves an animal to any valid adjacent cell at random.
     * Uses a seed that's dependent on the World object.
     */
    public void move() {
      
        World thisWorld = this.getCell().getWorld();
        Random seed = thisWorld.getSeed();
        
        //MoveDecision decision = this.getMoveDecision(seed, getMoveOptions());
        Cell newCell = this.decideMove(seed, getMoveOptions()); //decision.decide();
        
        //  System.out.println("Old cell: "+this.getCell().toString());
        // Clear current Cell's reference to this Life
        this.getCell().setLife(null);
        
        
        // System.out.println("Moving to: "+newCell.getLocation().toString());
      
        if (newCell == null) {
            System.out.println(NO_MOVES);
            return;
        }
        
        Life occupier = newCell.getLife();
        
        // If there's something in this Cell already
        if (occupier != null) {
            Class<?>[] foodTypes = getFoodTypes();
          
            // Run through foods we can eat.
            //  if the occupier of this cell is a type 
            //  we can eat, then eat it.
            boolean actionHappened = false;
            for (int i = 0; i < foodTypes.length; i++) {
                if (foodTypes[i].isInstance(occupier)) {
                    this.eat();
                    actionHappened = true;
                    continue;
                }
            }
            
            // If there's an occupier but we didn't handle it...
            if (!actionHappened) {
                String msg = UNHANDLED_SCENARIO + "\n";
                msg += ATTEMPTED_MOVE + " " + this.getClass() + " " + FROM + " "; 
                msg += this.getCell().getLocation().toString();
                
                msg += " " + TO + " " + newCell.getLocation().toString()
                    + " " + CONTAINING + " "; 
                msg += occupier.getClass() + "\n";
                
                throw new RuntimeException(msg);
            }
          
        }
        
        // Set two-way reference (to Cell & Life)
        this.setCell(newCell);
        if (this.getCell().getLife() != null) {
            this.getCell().getLife().destroy();
        }
        this.getCell().setLife(this);
    }
 
    /**
     * Gets the amount of food supply left.
     * @return foodSupply (int)
     */
    public int getFoodSupply() {
        return foodSupply;
    }
    
}
