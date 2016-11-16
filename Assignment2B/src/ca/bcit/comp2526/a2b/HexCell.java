package ca.bcit.comp2526.a2b;


import java.awt.Color;
import java.awt.Dimension;
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
    
    
    private World world;
    private Point location;
    private ArrayList<Life> occupiers = new ArrayList<Life>();
    
    protected Color emptyColor = EMPTY_COLOR;
    
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
     * Gets the empty color.
     * @return Color for backgrounds
     */
    @Override
    public Color getEmptyColor() {
    	return EMPTY_COLOR;
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
                       // System.err.println("invalid distance from "+this+" to "+newCell+" = "+this.distance(newCell));
                    	continue;
                    }
                    
                    cells.add(newCell);
                }
            }
        }

        return cells.toArray(new Cell[cells.size()]);
    }
    
    private Cell[] getAdjacentCellsWithOrWithout(Class<?>[] types, boolean with) {
        Cell[] all = getAdjacentCells();
        ArrayList<Cell> valid = new ArrayList<Cell>();
        
        //System.out.println("Cell adj: "+all.length);
        
        boolean validTerrain = !with;
        boolean validOccupiers = !with;
        
        for (int i=0; i<all.length; i++) {
        	
        	validTerrain = !with;
        	validOccupiers = !with;
        	
            for (int j=0; j<types.length; j++) {
            	
            	
                if ( (types[j] != null ) ) {
                
                	if (all[i].has(types[j])) {
                		validOccupiers = with;
                	}
                	if (types[j].isInstance(all[i])) {
                		validTerrain = with;
                	}
                	
                }
            }
            if (with && (validTerrain || validOccupiers)) {
            	valid.add(all[i]);
            } else if (validTerrain && validOccupiers) {
            	valid.add(all[i]);
            }
        }
         
        return valid.toArray(new Cell[valid.size()]);
        
    }
    
    
    /**
     * Gets the adjacent cells to this Cell
     * @return an array of Cells that are adjacent and of the given types.
     */
    @Override
    public Cell[] getAdjacentCellsWithout(Class<?>[] invalidTypes) {

    	return getAdjacentCellsWithOrWithout(invalidTypes, false);
        
    }
    
    /**
     * Gets the adjacent cells to this Cell
     * @return an array of Cells that are adjacent and of the given types.
     */
    @Override
    public Cell[] getAdjacentCellsWith(Class<?>[] validTypes) {
        
    	return getAdjacentCellsWithOrWithout(validTypes, true);
        
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
    public ArrayList<Life> getLives() {
        return occupiers;
    }
  
    /**
     * Updates the Life object in this Cell.
     * @param occupier to set
     */
    @Override
    public void addLife(final Life occupier) {
        //if(this.occupier != null) this.occupier.destroy(); // already done.
        
      
        this.occupiers.add(occupier);
        
        int occupierCount = occupiers.size();
        Life last = occupiers.get(occupierCount-1);
        
        // Get the new Color.
        Color newColor = last == null ? emptyColor 
        		: last.getColor();
        
        this.paint(newColor);
        
    }
    
    
    @Override
    public void removeLife(final Life occupier) {
    	if (occupier == null) {
    		occupiers.clear();
    		occupiers.trimToSize();
    		return;
    	}
    	occupiers.remove(occupier);
    	occupiers.trimToSize();
    }
    
    @Override
    public Life getLife(final Class<?> type) {
    	for (Life occupier : occupiers) {
    		if (type.isInstance(occupier)) {
    			return occupier;
    		}
    	}
    	return null;
    	
    }
    
    @Override
    public Life getLife(final Class<?>[] types) {
    	for (int i = 0; i < types.length; i++) {
    		Life thisType = getLife(types[i]);
    		if (thisType != null) {
    			return thisType;
    		}
    	}
    	return null;
    }
    
    @Override
    public boolean has(final Class<?> type) {
    	return getLife(type) != null;
    }
    
    @Override
    public boolean has(final Class<?>[] types) {
    	for (int i = 0; i < types.length; i++) {
    		if (this.has(types[i])) {
    			return true;
    		}
    	}
    	return false;
    }
    
    @Override
    public boolean is(final Class<?>[] types) {
    	for (int i = 0; i < types.length; i++) {
    		if (types[i].isInstance(this)) {
    			return true;
    		}
    	}
    	return false;
    }
    
    @Override
    public Dimension getSize() {
    	return new Dimension(this.getRadius()*2, getRadius() * 2);
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
            return Math.abs(this.getColumn() - other.getColumn());
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
              
        //System.out.println("distance from "+this+" to "+other+" is "+distance);
        
        return distance;
         
    }

    @Override
    public void recolor() {
    	paint();
    }
    
    /**
     * Paints this Hexagon with its Occupier's Color.
     */
    public void paint() {
    	
    	 Life last = occupiers.size() == 0 ? null :
    		 occupiers.get(occupiers.size() - 1);
         
         // Get the new Color.
         Color color = last == null ? emptyColor 
         		: last.getColor();
    	
       // if (color == emptyColor) System.out.println("setting empty color "+emptyColor);
        super.paint(color);
    }
    
    /**
     * Gets the text to display on this Cell.
     * @return text to display.
     */
    @Override
    public String getText() {
    
    	Life occupier = occupiers.size() == 0 ? null : 
    		occupiers.get(occupiers.size() - 1);
    	
        if (World.SHOW_FOOD
            && occupier != null) {
          
            // Get food supply
            int supply = occupier.getLifeLeft();
            return "" + supply;
          
        } else if (World.SHOW_MOVES 
            && occupier != null && occupier instanceof Moveable) {

            Moveable<?> mover = (Moveable<?>)occupier;
          
            // Get types we can move into.
            Class<?>[] types = mover.getInvalidMoveToTypes();

            
            // Get the number of possible moves.
            int moves = mover.getMoveToPossibilities(
                types, mover.getMoveMin(), mover.getMoveMax()).length;
            return "" + moves;
        
        } else if (World.SHOW_COORDINATES) {
            
            // Simply ROWxCOL.
            return location.x + "," + location.y;
        }
      
        return " ";
      
    }
    
    public void setText(String text) {
    	
    }
    
    /**
     * Gets the String of this SquareCell object.
     * @return String representing this object.
     */
    @Override
    public String toString() {
        //String life = getLives().toString();
        //return TYPE_AT + getLocation().toString() + CONTAINS + life;
    	return TYPE_AT + getLocation().x + "," + getLocation().y;
    }

	
   
    
}

