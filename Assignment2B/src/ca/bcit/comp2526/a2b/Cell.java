package ca.bcit.comp2526.a2b;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

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
@SuppressWarnings("serial")
public abstract class Cell extends JPanel {


  
	
    /**
     * The Cell's empty color.
     */
	public static final Color EMPTY_COLOR = new Color(163, 117, 73);

    /**
     * The Cell's border color.
     */
    public static final Color BORDER_COLOR = Color.WHITE;
   
    /**
     * The Cell's text color.
     */
    public static final Color TEXT_COLOR = Color.WHITE;
    
    
    private static final Dimension PREFERRED_SIZE = new Dimension(10, 10);
    private static final Border CELL_BORDER = BorderFactory.createLineBorder(BORDER_COLOR);
    private static final String CONTAINS = " containing a ";
    
    //protected int row;
    //protected int column;
    protected World world;
    private Point location;
    
    protected ArrayList<Life> occupiers = new ArrayList<Life>();
    protected Circle circle;
    protected JLabel text;
    
    protected Color lifeColor = getEmptyColor();
    
    public Cell(final World world, int row, int column) {
    	this.world = world;
    	location = new Point(row, column);
    	
        this.setPreferredSize(PREFERRED_SIZE);
        if (World.VISIBLE_LINES) {
            this.setBorder(CELL_BORDER);
        }
        
        setLayout(new BorderLayout());
        setBackground(getEmptyColor());
        
        this.text = new JLabel("");
        this.circle = new Circle(10, getEmptyColor());
        circle.setBackground(getEmptyColor());
        //this.circle = new Circle(10, Color.GREEN, circleOffsetX, circleOffsetY);
        
    }
    
    /**
     * 
     */
    public void init() {
        add(text);
        add(circle);
    }
    
    /**
     * Returns the EMPTY_COLOR variable.
     * @return Color for backgrounds.
     */
    public Color getEmptyColor() {
    	return EMPTY_COLOR;
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
    public Cell[] getAdjacentCellsWithout(Class<?>[] invalidTypes) {

    	return getAdjacentCellsWithOrWithout(invalidTypes, false);
        
    }
    
    /**
     * Gets the adjacent cells to this Cell
     * @return an array of Cells that are adjacent and of the given types.
     */
    public Cell[] getAdjacentCellsWith(Class<?>[] validTypes) {
        
    	return getAdjacentCellsWithOrWithout(validTypes, true);
        
    }
    
    /**
     * Gets the adjacent Cells.
     * @return an array of adjacent Cells.
     */
    public Cell[] getAdjacentCells() {
        return getNearbyCells(1, 1);
    }
  
    /**
     * Gets the life in this Cell.
     * @return the occupier (Life)
     */
    public ArrayList<Life> getLives() {
        return occupiers;
    }
    
    
    protected Color getLifeColor() {
    	int occupierCount = occupiers.size();
        Life last = occupiers.get(occupierCount-1);
        
        // Get the new Color.
        lifeColor = last == null ? getEmptyColor() 
        		: last.getColor();
        return lifeColor;
    }
    
    /**
     * Updates the Life object in this Cell.
     * @param occupier to set
     */
    public void addLife(final Life occupier) {
        //if(this.occupier != null) this.occupier.destroy(); // already done.
          
        this.occupiers.add(occupier);
        
        recolor();
    }
    
    /**
     * Removes a Life object this Cell contains.
     * @param occupier to remove for this Cell.
     */
    public void removeLife(final Life occupier) {
    	if (occupier == null) {
    		occupiers.clear();
    		occupiers.trimToSize();
    		return;
    	}
    	occupiers.remove(occupier);
    	occupiers.trimToSize();
    	
    	recolor();
    }
    
    /**
     * Get a Life object this Cell contains of this type.
     * @param type to select
     * @return Life of this type found.
     */
    public Life getLife(final Class<?> type) {
    	
    	for (Life occupier : occupiers) {
    		if (type.isInstance(occupier)) {
    			return occupier;
    		}
    	}
    	return null;
    	
    }
    
    /**
     * Get a Life object this Cell contains of these types.
     * @param types to select
     * @return Life of this type found.
     */
    public Life getLife(final Class<?>[] types) {
    	for (int i = 0; i < types.length; i++) {
    		Life thisType = getLife(types[i]);
    		if (thisType != null) {
    			return thisType;
    		}
    	}
    	return null;
    }
    
    /**
     * Returns true if this Cell has a Life of this type
     * @param types to check
     * @return true if it has this, otherwise false.
     */
    public boolean has(final Class<?> type) {
    	return getLife(type) != null;
    }
    
    /**
     * Returns true if this Cell has a Life of one of these types
     * @param types to check
     * @return true if it has this, otherwise false.
     */
    public boolean has(final Class<?>[] types) {
    	if (types == null) {
    		return false;
    	}
    	for (int i = 0; i < types.length; i++) {
    		if (this.has(types[i])) {
    			return true;
    		}
    	}
    	return false;
    }
    
    /**
     * Returns true if this Cell is an instance of these types.
     * @param types to check for.
     * @return true if this is one of the given types.
     */
    public boolean is(final Class<?>[] types) {
    	if (types == null) {
    		return false;
    	}
    	for (int i = 0; i < types.length; i++) {
    		if (types[i].isInstance(this)) {
    			return true;
    		}
    	}
    	return false;
    }
    
    /**
     * Gets the size of this Cell.
     * @return the size as a Dimension.
     */
	public Dimension getSize() {
		return super.getSize(null);
	}
    
    /**
     * Gets the Row this Cell belongs to.
     * @return the row.
     */
    public int getRow() {
        return location.x; //row;
    }
    
    /**
     * Sets the row this Cell exists in.
     * @param row - new row for this Cell.
     */
    public void setRow(int row) {
        location.setLocation(row, location.y);
    }
  
    /**
     * Gets the column this Cell exists in.
     * @return the column this Cell exists in.
     */
    public int getColumn() {
        return location.y;
    }
    
    /**
     * Updates the column this Cell exists in.
     * @param col - column to set.
     */
    public void setColumn(int column) {
        location.setLocation(location.x, column);
    }
    
    /**
     * Gets the World this Cell resides in.
     * @return the World object reference.
     */
    public World getWorld() {
        return world;
    }
    
    /**
     * Of a given haystack, finds the closest Cell to this one.
     * @param haystack - an array of Cells to choose from.
     * @return the decided cell.
     */
    public abstract Cell closest(final Cell[] haystack);
    
    /**
     * Calculates the distance between this Cell and a given Cell.
     * @param other Cell to compare with.
     * @return the distance.
     */
    public abstract double distance(final Cell other);
    
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
    
    /**
     * Sets the text to display on this Cell.
     * @param the text to display.
     * @param text
     */
    public abstract void setText(String text);
    
    /**
     * Recolor this cell.
     */
    public final void recolor() {
    	occupiers.trimToSize();
    	int occupierCount = occupiers.size();
        Life last = occupierCount > 0 ? 
        		occupiers.get(occupierCount-1) : null;
        
        // Get the new Color.
        Color newColor = (last == null ? getEmptyColor() 
        		: last.getColor());
        
        circle.paint(newColor);
        
    }
    
    /**
     * Gets the String of this SquareCell object.
     * @return String representing this object.
     */
    @Override
    public String toString() {
        String life = getLives().toString();
        return getClass().getSimpleName() + " @" + location.x + "," + location.y + CONTAINS + life;
    }
    
    
}
