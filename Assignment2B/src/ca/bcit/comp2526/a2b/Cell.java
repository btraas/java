package ca.bcit.comp2526.a2b;


import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Cells within a world.</p>
 * 
 * <p>Each Cell has 0 or 1 Life,
 *  each Life must have a Cell
 *  (or it's dead with no reference).</p>
 * 
 * @author Brayden Traas
 * @version 2016-10-22
 */
public abstract class Cell {



    /**
     * The Cell's empty color.
     */
    public static final Color EMPTY_COLOR = new Color(163, 117, 73); // brown

    /**
     * The Cell's border color.
     */
    public static final Color BORDER_COLOR = Color.WHITE;
   
    /**
     * The Cell's text color.
     */
    public static final Color TEXT_COLOR = Color.WHITE;
    
    
    
    //protected int row;
    //protected int column;
    protected World<? extends Cell> world;
    protected final Point location;
    
    protected ArrayList<Life> occupiers = new ArrayList<Life>();
 
    protected final Terrain terrain;
    //protected Color lifeColor = getEmptyColor();
    
    /**
     * Creates a Cell linking back to its world and position.
     * 
     * @param world - World object this Cell belongs to
     * @param column - the column this Cell resides in
     * @param row - the row this Cell resides in
     */
    public Cell(final World<? extends Cell> world, int column, int row) {
        this.world = world;
        location = new Point(column, row);
        this.terrain = Creator.newTerrain(world.getSeed());
    }
    
    /**
     * Gets the shape type of this Cell, regardless of its true type.
     * Eg: WaterHexCell and HexCell both return HexCell.class
     * @return the Shape class of this Cell.
     */
    //private abstract Class<? extends Cell> getShape();
    
    
    /**
     * Calculates the distance between this Cell and a given Cell.
     * @param other Cell to compare with.
     * @return the distance.
     */
    public abstract double distance(final Cell other);
    
   
    /**
     * Gets the terrain of this Cell. 
     * @return the Terrain 
     */
    public Terrain getTerrain() {
        return terrain;
    }
    
    /**
     * Returns the EMPTY_COLOR variable.
     * @return Color for backgrounds.
     */
    public Color getEmptyColor() {
        return terrain.getColor();
    }
    
    
    /**
     * Gets the location (Point) of this Cell.
     * @return location (Point)
     */
    public Point getLocation() {
        return location;
    }
    
    /**
     * Gets the Cells within the given distance.
     * 
     * @param min distance to travel
     * @param max distance to travel
     * @return an array of valid Cells.
     */
    public Cell[] getNearbyCells(int min, int max) {
        List<Cell> cells = new ArrayList<Cell>();
  
        // For each from -1 to 1 in distance Y from this Cell
        for (int i = 0 - max; i <= max; i++) {
          
            // For each from -1 to 1 in distance X from this Cell
            for (int j = 0 - max; j <= max; j++) {
                // System.out.println("  Checking "+(row+i)+"x"+(column+j)+" for possible move");
                // Get the Cell at this offset
                Cell newCell = world.getCellAt(location.x + i, location.y + j);
                
                // If the found cell != this cell
                if (newCell != null && newCell != this) {
                  
                    // special case for hex, not all within x / y distance
                    //  are within the true distance...
                    if (this.distance(newCell) > max || this.distance(newCell) < min) {
                        continue;
                    }
                    
                    cells.add(newCell);
                }
            }
        }

        return cells.toArray(new Cell[cells.size()]);
    }
    
    private Cell[] getAdjacentCellsWithAndWithout(
        final Matter[] mustHaveOrBe, final Matter[] mustNotHaveOrBe) {

        Cell[] all = getAdjacentCells();
        ArrayList<Cell> valid = new ArrayList<Cell>();
        
        //System.out.println("Cell adj: "+all.length);
        
        
        for (int i = 0; i < all.length; i++) {
            if ((mustHaveOrBe == null || all[i].hasOrIs(mustHaveOrBe)) 
                && !all[i].hasOrIs(mustNotHaveOrBe)) {
                valid.add(all[i]);
            }
            
        }
         
        return valid.toArray(new Cell[valid.size()]);
        
    }
    
    
    /**
     * Gets the adjacent cells to this Cell
     * @return an array of Cells that are adjacent and of the given types.
     */
    public Cell[] getAdjacentCellsWithout(final Matter[] invalidTypes) {

        return getAdjacentCellsWithAndWithout(null, invalidTypes);
        
    }
    
    /**
     * Gets the adjacent cells to this Cell
     * @return an array of Cells that are adjacent and of the given types.
     */
    public Cell[] getAdjacentCellsWith(final Matter[] validTypes) {
        
        return getAdjacentCellsWithAndWithout(validTypes, null);
        
    }
    
    /**
     * Gets the adjacent Cells.
     * @return an array of adjacent Cells.
     */
    public final Cell[] getAdjacentCells() {
        return getNearbyCells(1, 1);
    }
  
    /**
     * Gets all the lives in this Cell.
     * @return the occupier (Life)
     */
    public final ArrayList<Life> getLives() {
        return occupiers;
    }
    
    /**
     * Gets the matching lives in this Cell.
     * Allow all Matter so we can pass in a larger set to check.
     * @return the occupiers that match.
     */
    public final ArrayList<Life> getLives(final Matter[] validTypes) {
        ArrayList<Life> newLives = new ArrayList<Life>();
        if (occupiers == null || occupiers.size() == 0) {
            return newLives;
        }
        for (Life life : occupiers) {
            if (life == null) {
                continue;
            }
            for (int i = 0; i < validTypes.length; i++) {
                /* Check if this Life is this valid & not yet added, and add */
                if (validTypes[i] == life.type && !newLives.contains(life)) {
                    newLives.add(life);
                }
            }
        }
        return newLives;
    }
    
    /**
     * Updates the Life object in this Cell.
     * @param occupier to set
     */
    public final void addLife(final Life occupier) {
        //if(this.occupier != null) this.occupier.destroy(); // already done.
          
        this.occupiers.add(occupier);

    }
    
    /**
     * Removes a Life object this Cell contains.
     * @param occupier to remove for this Cell.
     */
    public final void removeLife(final Life occupier) {
        if (occupier == null) {
            occupiers.clear();
            occupiers.trimToSize();
            return;
        }
        occupiers.remove(occupier);
        occupiers.trimToSize();
    }
    

    
    /**
     * Get a Life object this Cell contains of this type.
     * @param type to select.
     * @param classException - array of Matter types to not select.
     * @param objectException - an Object to not select.
     * @return Life of this type found.
     */
    public final Life getLife(final Matter type, 
            final Matter[] classException, 
            final Object objectException) {
        
        for (Life occupier : occupiers) {
            
            // If we have one of the desired types.
            if (occupier != null && type == occupier.type 
                    &&  occupier != objectException) {
                if (classException == null) {
                    return occupier;
                }
                boolean valid = true;
                for (int i = 0; i < classException.length; i++) {
                    if (classException[i] == occupier.type) {
                        valid = false;
                    }
                }
                if (valid) {
                    return occupier;
                }
            }
        }
        return null;

    }
    
    /**
     * Get a Life object this Cell contains of these types.
     * @param types of Matter Enums to select.
     * @param classException - Matter Enum types to not select.
     * @param objectException - Object to not select.
     * @return Life of this type found.
     */
    public final Life getLife(final Matter[] types, 
            final Matter[] classException, final Object objectException) {
        for (int i = 0; i < types.length; i++) {
            Life thisType = getLife(types[i], classException, objectException);
            if (thisType != null) {
                return thisType;
            }
        }
        return null;
    }
    
    
    /**
     * Get a Life object this Cell contains of this type.
     * @param type to select
     * @return Life of this type found.
     */
    public final Life getLife(final Matter type) {
        // boolean is a dummy class...
        return getLife(type, null, null);
    }
    

    /**
     * Get a Life object this Cell contains of these types.
     * @param types to select
     * @return Life of this type found.
     */
    public final Life getLife(final Matter[] types) {
        return getLife(types, null, null);
    }
    
    /**
     * Returns true if this Cell has a Life of this type
     * @param type of Matter to check for.
     * @param classException of Matter to not check for.
     * @param objectException - object to not choose.
     * @return true if it has this, otherwise false.
     */
    public final boolean has(final Matter type, 
            final Matter[] classException, final Object objectException) {
        return getLife(type, classException, objectException) != null;
    }
    

    
    /**
     * Returns true if this Cell has a Life of one of these types
     * @param types of Matter to check for.
     * @param exceptionClass - Array of Matter Enums to not check for.
     * @param exceptionObject - object to not choose.
     * @return true if it has this, otherwise false.
     */
    public final boolean has(final Matter[] types, 
            final Matter[] exceptionClass, final Object exceptionObject ) {
        if (types == null) {
            return false;
        }
        for (int i = 0; i < types.length; i++) {
            if (this.has(types[i], exceptionClass, exceptionObject)) {
                return true;
            }
        }
        return false;
    }
    


    /**
     * Returns true if this Cell has a Life of this type
     * @param type of Matter to check for.
     * @return true if it has this, otherwise false.
     */
    public final boolean has(final Matter type) {
        return getLife(type, null, null) != null;
    }
    
    
    /**
     * Returns true if this Cell has a Life of one of these types
     * @param types to check
     * @return true if it has this, otherwise false.
     */
    public final boolean has(final Matter[] types) {
        return has(types, null, null);
    }
    
    
    /**
     * Returns true if this Cell is an instance of these types.
     * @param types to check for.
     * @return true if this is one of the given types.
     */
    public final boolean is(final Matter[] types) {
        if (types == null) {
            return false;
        }
        for (int i = 0; i < types.length; i++) {
            if (types[i] == terrain) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Public shortcut to see if this is or has one of these types.
     * @param types to check.
     * @return true if this is or has one of these types.
     */
    public final boolean hasOrIs(final Matter[] types) {
        
        if (types == null) {
            return false;
        }   
        return (this.has(types) || this.is(types));
    }
    
    /**
     * Gets the Row this Cell belongs to.
     * @return the row.
     */
    public int getRow() {
        return location.y; //row;
    }
    
  
    /**
     * Gets the column this Cell exists in.
     * @return the column this Cell exists in.
     */
    public int getColumn() {
        return location.x;
    }
    
    /**
     * Gets the World this Cell resides in.
     * @return the World object reference.
     */
    public final World<? extends Cell> getWorld() {
        return world;
    }
    
    /**
     * Of a given haystack, finds the closest Cell to this one.
     * @param haystack - an array of Cells to choose from.
     * @return the decided cell.
     */
    public final Cell closest(final Cell[] haystack) {
            
        // If no haystack provided.
        if (haystack == null || haystack.length == 0) {
            return null;
        } else if (!(haystack[0] instanceof Cell) ) {
            return null;
        } else if (haystack.length == 1) {
            return haystack[0];
        }
       
        Point goal = this.getLocation();
       
        // Assign closest = first in haystack.
        Cell closest = haystack[0];
        // Assign closest distance = distance to first.
        double closestDistance = goal.distance(closest.getLocation());
       
        // Iterate over each in haystack and update closest variable
        //  if this is closer.
        for (int i = 0; i < haystack.length; i++) {
          
            if (goal.distance(haystack[i].getLocation()) < closestDistance) {
                closest = haystack[i];
                closestDistance = goal.distance(closest.getLocation());
            }
       
        }
       
        return closest;
     
    }
    
    
    /**
     * Gets the text to display on this Cell.
     * @return text to display.
     */
    public String getText() {
    
        Life occupier = occupiers.size() == 0 ? null : 
            occupiers.get(occupiers.size() - 1);

        if (World.SHOW_FOOD
            && occupier != null) {
          
            // Get food supply
            int supply = occupier.getLifeLeft();
            return "" + supply;
          
        } else if (World.SHOW_MOVES 
            && occupier != null && occupier.moveable()) {

            
            // Get the number of possible moves.
            int moves = occupier.getMoveToPossibilities(
                occupier.type.move.min, 
                occupier.type.move.max).length;
            return "" + moves;
        
        } else if (World.SHOW_COORDINATES) {
            
            // Simply ROWxCOL.
            return location.x + "," + location.y;
        }
      
        return " ";
      
    }


    

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((world == null) ? 0 : world.hashCode());
        result = prime * result +  getClass().hashCode();
        result = prime * result + ((location == null) ? 0 : location.hashCode());
        
        return result;
    }

    
    @Override
    public boolean equals(final Object other) {
        if (other == null) {
            return false;
        }
        if (other == this) {
            return true;
        }
        if (!(other instanceof Cell)) {
            return false;
        }
        
        Cell otherCell = (Cell)other;
        
        // Checking if it's the same world, not equivalent.
        // Shape denotes whether this object is a SquareCell, HexCell etc.
        if (  this.getWorld() == otherCell.getWorld()
                && this.getClass().equals(otherCell.getClass())
                && this.getLocation().equals(otherCell.getLocation()) ) {
            return true;
        }
        
        return false;
    }
    

    /**
     * Gets the String of this SquareCell object.
     * @return String representing this object.
     */
    @Override
    public String toString() {
        //String life = getLives().toString();
        
        String string = "(" + terrain.name() + ") ";
        
        string += " @" + location.x + "," + location.y;
        
        return string;
    }
    
}
