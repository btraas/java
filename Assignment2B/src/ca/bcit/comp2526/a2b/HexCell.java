package ca.bcit.comp2526.a2b;


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
    
    
    private static final String TYPE_AT = "Cell @";
    private static final String CONTAINS = " containing a ";
    
    
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
     * Returns available cells within the given distance
     *  and either empty or one of the valid types we can 
     *  move 'into'. (ex. Herbivore can move into empty or
     *  Plant-occupied Cells).
     * @return adjacent cells
     */
    @Override
    public Cell[] getMoveToPossibilities(final Class<?>[] types, int min, int max) {
        List<Cell> cells = new ArrayList<Cell>();
        
        
        Cell[] possibilities = getNearbyCells(min, max);
                
        for (int i=0; i<possibilities.length; i++ ) {
          
            Cell newCell = possibilities[i];
          
            // If the found cell != this cell
            if (newCell != null && newCell != this) {
                Life lifeInNewCell = newCell.getLife();
                
                // If the new Cell is empty, it's valid
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
        
        return cells.toArray(new Cell[cells.size()]);   
    }
    
    /**
     * Gets the adjacent cells to this Cell
     * @return array of Cells that are adjacent to this one.
     */
    @Override
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
    

    /**
     * Gets the adjacent cells to this Cell
     * @return an array of Cells that are adjacent and of the given types.
     */
    @Override
    public Cell[] getAdjacentCells(Class<?>[] types) {
        Cell[] all = getAdjacentCells();
        ArrayList<Cell> valid = new ArrayList<Cell>();
        
        for (int i=0; i<all.length; i++) {
            for (int j=0; j<types.length; j++) {
                if ( (types[j] == null && all[i].getLife() == null)  
                    || (types[j] != null && types[j].isInstance(all[i].getLife()))) {
                    valid.add(all[i]);
                }

            }
        }
        
        return valid.toArray(new Cell[valid.size()]);
        
    }
    
    /**
     * Gets the adjacent Cells.
     * @return an array of adjacent Cells.
     */
    @Override
    public Cell[] getAdjacentCells() {
        return getNearbyCells(1, 1);
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

            Moveable<?> mover = (Moveable<?>)occupier;
          
            // Get types we can move into.
            Class<?>[] types = mover.getMoveToLifeTypes();

            
            // Get the number of possible moves.
            int moves = this.getMoveToPossibilities(
                types, mover.getMoveMin(), mover.getMoveMax()).length;
            return "" + moves;
        
        } else if (World.SHOW_COORDINATES) {
            
            // Simply ROWxCOL.
            return location.x + "x" + location.y;
        }
      
        return "";
      
    }
    
    /**
     * Gets the String of this SquareCell object.
     * @return String representing this object.
     */
    @Override
    public String toString() {
        String life = (getLife() == null ? "" : getLife().getClass().getSimpleName());
        return TYPE_AT + getLocation().toString() + CONTAINS + life;
    }
   
    
}

