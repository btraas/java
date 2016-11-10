package ca.bcit.comp2526.a2a;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * A Hex Cell. Both a Hexagon and a Cell object.
 * Used for a hex grid (opposed to a square grid).
 * 
 * @author Brayden Traas
 * @version 2016-11-02
 */
public class HexCell extends Hexagon implements Cell {
   
    private static final long serialVersionUID = 3929010413552114577L;

    private static final String UNLIKE_TYPES = "Comparing unlike Cell types!!";

    private static final float HALF = 0.5f;
    
    private World world;
    private Point location;
    private Life occupier;
    
    /**
     * Instantiates a HexCell.
     * 
     * @param world this Cell belongs to.
     * @param row this Cell resides on.
     * @param col this Cell resides on.
     * @param valueX - true position x of this Hex.
     * @param valueY - true position y of this Hex.
     * @param radius of the Hexagon.
     */
    public HexCell(final World world, int row, int col, int valueX, int valueY, int radius) {
        super(new Point(valueX, valueY), radius);
        this.location = new Point(row, col);
        this.world = world;
    }
    
    /**
     * Gets the location (Point) of this Cell.
     * @return location (Point)
     */
    @Override
    public Point getLocation() {
        return location;
    }

    /**
     * Gets the possibilities of this Cell to move to
     *  (usually adjacent. Adjacent if distance = 1).
     *  
     *  @param types - An array of Classes, types we can move into.
     *  @param distance - include possibilities within this range.
     *  @return an array of Cells within range.
     */
    @Override
    public Cell[] getMoveToPossibilities(final Class<?>[] types, int distance) {
        List<Cell> cells = new ArrayList<Cell>();
        
        // For each from -1 to 1 in distance Y from this Cell
        for (int i = 0 - distance; i <= distance; i++) {
          
            // For each from -1 to 1 in distance X from this Cell
            for (int j = 0 - distance; j <= distance; j++) {
              
                // Get the Cell at this offset
                Cell newCell = world.getCellAt((int)location.getX() + i, (int)location.getY() + j);
                
                // If the found cell != this cell
                if (newCell != null && newCell != this) {
              
                    // special case for hex, not all within x / y distance
                    //  are within the true distance...
                    if (this.distance(newCell) > distance) {
                        continue;
                    }
                  
                    Life lifeInNewCell = newCell.getLife();
                    
                    // If the new Cell is empty, it's always valid.
                    if ( lifeInNewCell == null ) {
                        cells.add(newCell);
                        continue;
                    }
                    
                    // If defined types is null, we're allowing all types.
                    
                    if ( types == null ) {
                        cells.add(newCell);
                        continue;
                    }
                    
                    
                    // Run through each type of Life we can move into.
                    // If this is one of those types, it's valid.
                    
                    boolean added = false;
                    
                    for (int k = 0; k < types.length; k++) {
                        if (!added && types[k].isInstance(lifeInNewCell)) {
                            cells.add(newCell);
                            added = true;
                        }
                    }
                    
                }
            }
        }
        
        // Convert ArrayList to Array and return.
        return cells.toArray(new Cell[cells.size()]);   
    }
  
    /**
     * Gets all adjacent cells.
     *  This method is required, and calls the above
     *  getMoveToPossibilities() method; all cells
     *  including occupied ones, one length away.
     * 
     * @return an array of adjacent cells.
     */
    @Override
    public Cell[] getAdjacentCells() {
        return getMoveToPossibilities(null, 1);
    }
  
    /**
     * Gets the life in this Cell.
     * @return the occupier (Life)
     */
    @Override
    public Life getLife() {
        return occupier;
    }
  
    /**
     * Updates the Life object in this Cell.
     * @param occupier to set
     */
    @Override
    public void setLife(final Life occupier) {
        //if(this.occupier != null) this.occupier.destroy(); // already done.
        
      
        this.occupier = occupier;
        Color newColor = occupier == null ? EMPTY_COLOR : occupier.getColor();
        
        this.paint(newColor);
        
    }
    
    /**
     * Gets the row this Cell is found in.
     * @return the row this Cell is found in.
     */
    @Override
    public int getRow() {
        return location.x; //row;
    }
  
    /**
     * Sets the row this Cell is found in.
     * @param row to set.
     */
    @Override
    public void setRow(int row) {
        location.setLocation(row, location.y);
    }
  
    
    /**
     * Gets the column this Cell is found in.
     * @return the column this Cell is found in.
     */
    @Override
    public int getColumn() {
        return location.y;
    }
  
    /**
     * Sets the column this Cell is found in.
     * @param column to set.
     */
    @Override
    public void setColumn(int column) {
        location.setLocation(location.x, column);
    }
  
    /**
     * Gets the World this Cell belongs to.
     * @return world object.
     */
    @Override
    public World getWorld() {
        return world;
    }
  
    /**
     * Gets the closest Cell in the haystack to this Cell.
     * @param haystack - an array of Cell objects to search through.
     * @return Cell from the haystack that's closest.
     */
    @Override
    public Cell closest(final Cell[] haystack) {
        Cell needle = this;
        
        if (haystack.length == 0 || needle == null) {
            return null;
        } else if (!(haystack[0] instanceof HexCell) ) {
            return null;
        } else if (haystack.length == 1) {
            return haystack[0];
        }
       
        Point goal = needle.getLocation();
       
        Cell closest = haystack[0];
        double closestDistance = goal.distance(closest.getLocation());
       
        for (int i = 0; i < haystack.length; i++) {
          
            if (goal.distance(haystack[i].getLocation()) < closestDistance) {
                closest = haystack[i];
                closestDistance = goal.distance(closest.getLocation());
            }
       
        }
       
        return closest;
    }

    /**
     * Finds the distance between this Cell and another Cell.
     * @param other Cell to compare with.
     * @return distance (double) between the two.
     */
    @Override
    public double distance(final Cell other) {

        if (!(other instanceof HexCell)) {
            throw new RuntimeException(UNLIKE_TYPES);
        }
        
        if (this.getRow() == other.getRow() && this.getColumn() == other.getColumn()) {
            return 0.0;
        }
        
        double diffX = Math.abs(this.getRow() - other.getRow());
        
        
        // If on the same row
        if (this.getRow() == other.getRow()) {
            return diffX;
        }
 
        Point otherTheoretical = new Point(other.getLocation());
        
        // If this is even and other isn't
        if ( (this.getRow() & 1) == 0 && (other.getRow() & 1) != 0) {
            
            if (this.getColumn() < other.getColumn()) {
                otherTheoretical.y--;
            }

        }
        
    
        // int distance = max(    
        int dist1 = (int)Math.ceil(1 - (other.getRow() * HALF)) + (int)other.getColumn();
        dist1 -= ((int)Math.ceil(1 - (this.getRow() * HALF)) + (int)this.getColumn());
        dist1 = Math.abs(dist1);
         
        int dist2 = -other.getRow() - (int)Math.ceil(1 - (other.getRow() * HALF)) 
                    - (int)other.getColumn();
        dist2 += this.getRow() + Math.ceil(1 - (this.getRow() * HALF)) + this.getColumn();
        dist2 = Math.abs(dist2);
          
        //System.out.println("\n diffX:"+diffX+" dist1:"+dist1+" dist2:"+dist2 );
        int distance = Math.max(Math.max(dist1, dist2), (int)diffX);
              
        return distance;
         
    }

    /**
     * Paints this Hexagon with its Occupier's Color.
     */
    public void paint() {
        Color color = (occupier == null) ? EMPTY_COLOR : occupier.getColor();
        super.paint(color);
    }
    
    /**
     * Gets the text to display on this Cell.
     * @return text to display.
     */
    @Override
    public String getText() {
    
        if (World.SHOW_FOOD
            && occupier != null && occupier instanceof Animal) {
          
            // Get food supply
            int supply = ((Animal)occupier).getFoodSupply();
            return "" + supply;
          
        } else if (World.SHOW_MOVES 
            && occupier != null && occupier instanceof Moveable) {
          
            // Get types we can move into.
            Class<?>[] types = ((Moveable<?>)occupier).getMoveToLifeTypes();
            
            // Get the number of possible moves.
            int moves = this.getMoveToPossibilities(types, 1).length;
            return "" + moves;
        
        } else if (World.SHOW_COORDINATES) {
            
            // Simply ROWxCOL.
            return location.x + "x" + location.y;
        }
      
        return "";
      
    }
    
   
    
}

