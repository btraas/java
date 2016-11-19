package ca.bcit.comp2526.a2b;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
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

    
    
    //private static final String NO_MOVES =              "No moves found...";
  
    //protected int foodSupply;
    private int eatAmount;
    
    private int minMove;
    private int maxMove;
    
    //protected Class<?>[] invalidMoveToTypes;
  
    public Animal(Cell location, Color color, 
    				int initialFood,
    				int eatAmount,
    				int minMove,
    				int maxMove,
    				Class<?>[] invalidMoveToTypes,
    				Class<?>[] foodTypes) {
        super(location, color, initialFood, foodTypes);
        this.eatAmount = eatAmount;
        this.minMove = minMove;
        this.maxMove = maxMove;
        this.incompatibleTypes = invalidMoveToTypes;
    }

    
    /**
     * Processes this Animal's turn. Must be implemented as per Life.
     */
    public void processTurn() {
        
        super.processTurn();
        
        // Dead.
        if (this.getCell() == null) {
        	if (World.DEBUG) {
        		System.err.println(" " + this + " IS DEAD");
        	}
        	return;
        }
        
        
        if (World.DEBUG) {
        	System.out.println(" " + getCell()+" NOW CONTAINS: " + getCell().occupiers);
        	System.out.println(" Moving...");
        }
        this.move(); 
        if (World.DEBUG) {
        	System.out.println(" " + getCell()+" NOW CONTAINS: " + getCell().occupiers);
        	System.out.println(" Eating...");
        }
        this.eat();
        if (World.DEBUG) {
        	System.out.println(" " + getCell()+" NOW CONTAINS: " + getCell().occupiers);
        	System.out.println(" Reproducing...");
        }
        this.reproduce();
        
        System.out.println(" " + getCell()+" NOW CONTAINS: " + getCell().occupiers);
    }
    
    public int getMoveMin() {
        return minMove;
    }
    
    public int getMoveMax() {
        return maxMove;
    }
    
    /**
     *  Gets a Herbivore's valid food types.
     *  @return an array of valid types to eat.
     */
    public Class<?>[] getFoodTypes() {
        return foodTypes;
    }
    

    /*

    public Class<?>[] getInvalidMoveToTypes() {
        return invalidMoveToTypes;
    }
    */
    
    /**
     * Gets the possibilities to move to from here.
     * @param types - allowable types we can move into.
     * @param distanceMin - min distance possible to move.
     * @param distanceMax - max distance possible to move.
     * @return an array of possible destination Cells.
     */
    @Override
    public Cell[] getMoveToPossibilities(final Class<?>[] invalidTypes, int min, int max) {
        List<Cell> cells = new ArrayList<Cell>();
        
        /*
        if (this.getClass().getSimpleName().equals("Herbivore")) {
        	for (int i = 0; i < invalidTypes.length; i++) {
        		System.out.println(" Herb invalid types: "+invalidTypes[i].getSimpleName());
        	}
        }
        */
        
        Cell[] possibilities = getCell().getNearbyCells(min, max);
                
        for (int i=0; i<possibilities.length; i++ ) {
          
            Cell newCell = possibilities[i];
          
            // If the found cell != this cell
            if (newCell != null && newCell != getCell()) {
                
            	// If defined types is null, we're allowing all types.
                if ( invalidTypes == null || invalidTypes.length == 0) {
                    cells.add(newCell);
                    continue;
                }
            	
                boolean occupiersValid = true;
                boolean terrainValid = true;
                
                for (int k = 0; k < invalidTypes.length; k++) {
                	// if one of the lives is this type, or this cell is this type.
                    if (newCell.has(invalidTypes[k])) {
                    	occupiersValid = false;
                    } 
                    if (invalidTypes[k].isInstance(newCell)) {
                        terrainValid = false;
                    }
                }
                
                if (occupiersValid && terrainValid) {
                	cells.add(newCell);
                }
            }
        }
        if (this.getClass().getSimpleName().equals("Herbivore")) {
        //	System.out.print(getCell()+" herb possibilities: ");
        	for (Cell cell : cells) {
        //		System.out.print(cell + " containing " + cell.occupiers +", " );
        	}
        //	System.out.println();
        }
        
        return cells.toArray(new Cell[cells.size()]);   
    }
    
    
    /**
     * Gets all the possible move options for this animal.
     * @return an array of new locations (Cell objects)
     */
    protected final Cell[] getMoveOptions() {
        
        // getMoveToLifeTypes() and getMoveDistance() must be defined in child class, 
        //  as per interface.
        return this.getMoveToPossibilities(getInvalidMoveToTypes(), minMove, maxMove);
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
      
        if (this.getCell() == null) {
            return;
        }
      
        World thisWorld = this.getCell().getWorld();
        Random seed = thisWorld.getSeed();
        
        //MoveDecision decision = this.getMoveDecision(seed, getMoveOptions());
        Cell newCell = this.decideMove(seed, getMoveOptions()); //decision.decide();
        
        if (getClass().getSimpleName().equals("Herbivore")) System.out.println("  chosen: "+newCell+" containing: "+(newCell==null?"":newCell.getLives()));
        
        //System.out.println();
        
        
        //  System.out.println("Old cell: "+this.getCell().toString());
        
        
        
        // System.out.println("Moving to: "+newCell.getLocation().toString());
      
        if (newCell == null) {
            System.err.println("NO MOVES");
            return;
        }
        
        // Clear current Cell's reference to this Life
        this.getCell().removeLife(this);
        
        // Save previous location
        this.previousLocation = this.getCell();
        // Set two-way reference (to Cell & Life)
        this.setCell(newCell);
        /*
        if (this.getCell().getLife() != null) {
            this.getCell().getLife().destroy();
        }
        */
        this.getCell().addLife(this);
        
        if (this.getCell() instanceof WaterCell) {
        	System.out.println(getCell() + " NOW CONTAINS A " + getClass().getSimpleName());
        }
    }
    
    /**
     * Defines what happens when an animal. Simply sets the food amount.
     */
    public final void eat() {
    	
    	boolean eaten = false;
    	
    	// See if there's food in this Cell. Eat it if so.
    	// But not this one!
    	// has() -> one of food types, not null, not this.
    	if (this.getCell().has(this.foodTypes, null, this)) {
    		Life food = this.getCell().getLife(this.foodTypes);
    		if (World.DEBUG) {
    			System.out.println("  Eating "+food.getClass().getSimpleName());
    		}
    		this.getCell().removeLife(food);
    		
    		eaten = true;
    		
    	// See if this Cell is edible. Eat it if so.
    	//  currently, a Cell doesn't change when eaten.
    	} else if (this.getCell().is(this.foodTypes)) {
    		eaten = true;
    	}
    	
    	if (eaten) {
    		this.life = eatAmount;
    		this.color = originalColor;
    	}
    	
    } 
}
