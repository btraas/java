package ca.bcit.comp2526.a2b;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

/**
 * <p>Square Cells within a world.</p>
 * 
 * <p>Each Cell has 0 or 1 Life,
 *  each Life must have a Cell
 *  (or it's dead with no reference).</p>
 * 
 * @author Brayden Traas
 * @version 2016-10-22
 */
@SuppressWarnings("serial")
public class SquareCell extends JPanel implements Cell {

      
    private static final Dimension PREFERRED_SIZE = new Dimension(10, 10);
    private static final Border CELL_BORDER = BorderFactory.createLineBorder(BORDER_COLOR);
  
    private static final String CONTAINS = " containing a ";
    
    protected Color emptyColor = EMPTY_COLOR;
    
    private int row;
    private int column;
    private World world;
    private Point location;
    // private ArrayList<Life> occupiers = new ArrayList<Life>();
    // private Circle circle;
    private JLabel text;
  
    /**
     * Creates a Cell object.
     * 
     * @param world - the world this Cell belongs to
     * @param row this Cell is found in
     * @param column this cell is found in
     */
    public SquareCell(final World world, int row, int column) {
        
        this.row = row;
        this.column = column;
        this.world = world;
      
        location = new Point(row, column);
        
        this.setPreferredSize(PREFERRED_SIZE);
        if (World.VISIBLE_LINES) {
            this.setBorder(CELL_BORDER);
        }
        setBackground(emptyColor);
        
        setLayout(new BorderLayout());
        
        this.text = new JLabel("");
        //this.circle = new Circle(getSize().width / 2, emptyColor);
        
        add(text);
        //add(circle);
        
    }
  
    /*
    protected void recolor() {
    	occupiers.trimToSize();
    	int occupierCount = occupiers.size();
        Life last = occupierCount > 0 ? 
        		occupiers.get(occupierCount-1) : null;
        
        // Get the new Color.
        Color newColor = (last == null ? emptyColor 
        		: last.getColor());
        
        //circle.paint(newColor);
        
    }
    */
    
    /**
     * Gets the empty color.
     * @return Color for backgrounds
     */
    @Override
    public Color getEmptyColor() {
    	return EMPTY_COLOR;
    }
    
    /**
     * Returns the location of the Cell on the World.
     * @return the Cell's location
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
                Cell newCell = world.getCellAt(row + i, column + j);
                
                // If the found cell != this cell
                if (newCell != null && newCell.distance(this) >= min) {
                  
                    cells.add(newCell);
                }
            }
        }

        return cells.toArray(new Cell[cells.size()]);
    }
    

    private Cell[] getAdjacentCellsWithOrWithout(Class<?>[] types, boolean with) {
        Cell[] all = getAdjacentCells();
        ArrayList<Cell> valid = new ArrayList<Cell>();
        
      //  System.out.println("\nCell "+this+" adj: "+all.length);
      //  if (types.length > 0) System.out.println(" Checking adjacent cells with"
      //  +(with?"":"out")+" a "+Life.typesToString(types));
        
        
        for (int i=0; i<all.length; i++) {
        
        	boolean validTerrain = !with;
            boolean validOccupiers = !with;
        	
        	for (int j=0; j<types.length; j++) {
            	
            	
                if ( (types[j] != null ) ) {
                
                	if (all[i].has(types[j])) {
                		validOccupiers = with;
             //   		System.out.println("--"+all[i]+" has "+types[j].getSimpleName());
                	}
                	if (types[j].isInstance(all[i])) {
                		validTerrain = with;
                	}
                	
                }
            }
            if (with && (validTerrain || validOccupiers)) {
            	valid.add(all[i]);
           // 	System.out.println("  valid: "+all[i] + " "+(with?"does contain":"does not contain")+" a ["+Life.typesToString(types)+"]");
            } else if (validTerrain && validOccupiers) {
            	valid.add(all[i]);
           // 	System.out.println("  valid: "+all[i] + " "+(with?"does contain":"does not contain")+" a ["+Life.typesToString(types)+"]");
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
     * <p>Sets the life that's in this cell.</p>
     * 
     * <p>This is called at the end of the turn / move / eat process.</p>
     * 
     * @param occupier a Life object
     */
    @Override
    public void addLife(final Life occupier) {
        //if(this.occupier != null) this.occupier.destroy(); // already done.
        
    	if (occupier != null) {
    		//this.occupiers.add(occupier);	// add Life to ArrayList
    		
    		// UI
    		this.add(occupier, BorderLayout.CENTER);
    		// set color of circle to last occupier's color
    		//recolor();
    		// repaint();
    		occupier.setVisible(true);
    		setText(getText());
    		repaint();
    		
    	}
    }
    
    @Override
    public void removeLife(final Life occupier) {
    	
    	// All
    	if (occupier == null) {
    		this.removeAll();
    		this.add(text);
    		return;
    	}
    	
    	// Single
    	this.remove(occupier);

    }
    
    @Override
    public boolean has(final Class<?> type) {
    	for (Life life : getLives()) {
    		if (type.isInstance(life)) {
    			return true;
    		}
    	}
    	return false;
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
    
    /*
    @Override
    public t getSize() {
    	return this.getWidth();
    }
    */

	@Override
	public ArrayList<Life> getLives() {
		Component[] components = this.getComponents();
		ArrayList<Life> lives = new ArrayList<Life>();
		for (int i = 0; i < components.length; i++) {
			if (Life.class.isInstance(components[i])) {
				lives.add((Life)components[i]);
			}
		}

		return lives;
	}

    
    @Override
    public Life getLife(final Class<?> type) {
    	for (Life occupier : getLives()) {
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

        
    /**
     * Gets the Text to display within this Cell.
     * @return String to display.
     */
    @Override
    public String getText() {
        
    	ArrayList<Life> occupiers = getLives();
    	Life occupier = occupiers.size() == 0 ? null : 
    		occupiers.get(occupiers.size() - 1);
    	
        if (World.SHOW_FOOD
              && occupier != null && occupier instanceof Animal) {
            
            // Get food supply
            int supply = occupier.getLifeLeft();
            return "" + supply;
            
        } else if (World.SHOW_MOVES 
            && occupier != null && occupier instanceof Moveable) {
          
            Moveable<?> mover = (Moveable<?>)occupier;
          
            // Get types we can move into.
            Class<?>[] types = mover.getInvalidMoveToTypes();
            
            // Get the number of possible moves.
            int moves = mover.getMoveToPossibilities(types, 
                mover.getMoveMin(), mover.getMoveMax()).length;
            return "" + moves;
        
        } else if (World.SHOW_COORDINATES) {
            
            // Simply ROWxCOL.
            return row + "x" + column;
        }
        
        return "";
    }
    
    /**
     * Sets the text of the JLabel.
     * @param text to set.
     */
    public void setText(final String text) {
        this.text.setText(text);
    }
    
    /**
     * Finds the right text and sets the text of the JLabel.
     */
    public void setText() {
        // Finds the new Text to set, and sets it.
        this.setText(getText());
    }
    
    /**
     * Gets the row of this Cell.
     * @return row (int).
     */
    @Override
    public int getRow() {
        return row;
    }

    /**
     * Sets the row of this Cell.
     * @param row (int) to set.
     */
    @Override
    public void setRow(int row) {
        this.row = row;
    }

    /**
     * Gets the column of this Cell.
     * @return the column.
     */
    @Override
    public int getColumn() {
        return column;
    }

    /**
     * Sets the column of the Cell.
     * @param column to set.
     */
    @Override
    public void setColumn(int column) {
        this.column = column;
    }

    /**
     * Get the current world.
     * @return World object this cell belongs to
     */
    @Override
    public World getWorld() {
        return world;
    }

    /**
     * Gets the closest Cell in the haystack to this Cell.
     * 
     * @param haystack - an array of Cells to choose from
     * @return the closest Cell from the haystack to the this
     */
    @Override
    public final Cell closest(final Cell[] haystack) {
     
            
        // If no haystack provided.
        if (haystack == null || haystack.length == 0) {
            return null;
        } else if (!(haystack[0] instanceof SquareCell) ) {
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
     * Gets the distance between this Cell and another. 
     *  Essentially the distance of this Cell's Point to
     *  the other Cell's point.
     * @param other (Cell) to measure distance to.
     * @return distance (double);
     */
    @Override
    public double distance(final Cell other) {
        return this.getLocation().distance(other.getLocation());
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






