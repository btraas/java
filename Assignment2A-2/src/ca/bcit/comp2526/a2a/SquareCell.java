package ca.bcit.comp2526.a2a;

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
  
    private static final String TYPE_AT = "Cell @";
    private static final String CONTAINS = " containing a ";
    
    private int row;
    private int column;
    private World world;
    private Point location;
    private Life occupier;
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
        this.setBackground(EMPTY_COLOR);
        
        this.text = new JLabel("");
        add(text);
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
     * Returns available cells within the given distance
     *  and either empty or one of the valid types we can 
     *  move 'into'. (ex. Herbivore can move into empty or
     *  Plant-occupied Cells).
     * @return adjacent cells
     */
    @Override
    public Cell[] getMoveToPossibilities(final Class<?>[] types, int distance) {
        List<Cell> cells = new ArrayList<Cell>();
        
        
        // For each from -1 to 1 in distance Y from this Cell
        for (int i = 0 - distance; i <= distance; i++) {
          
            // For each from -1 to 1 in distance X from this Cell
            for (int j = 0 - distance; j <= distance; j++) {
                // System.out.println("  Checking "+(row+i)+"x"+(column+j)+" for possible move");
                // Get the Cell at this offset
                Cell newCell = world.getCellAt(row + i, column + j);
                
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
        }
        
        return cells.toArray(new Cell[cells.size()]);   
    }
    
    /**
     * Gets the adjacent cells to this Cell
     * @return array of Cells that are adjacent to this one.
     */
    @Override
    public Cell[] getAdjacentCells() {
        return getMoveToPossibilities(null, 1);
    }
    
    
    /**
     * <p>Sets the life that's in this cell.</p>
     * 
     * <p>This is called at the end of the turn / move / eat process.</p>
     * 
     * @param occupier a Life object
     */
    @Override
    public void setLife(final Life occupier) {
        //if(this.occupier != null) this.occupier.destroy(); // already done.
        
        // Simply set this instance variable.
        this.occupier = occupier;
        
        // Get the new Color.
        Color newColor = occupier == null ? EMPTY_COLOR : occupier.getColor();
        
        // Set the new Color.
        this.setBackground(newColor);
        
        // Find & set the new text for this Cell.
        this.setText();
        
    }    
        
    /**
     * Gets the Text to display within this Cell.
     * @return String to display.
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
     * Gets the life object that's in this cell.
     * @return Life object 
     */
    @Override
    public Life getLife() {
        return this.occupier;
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
        String life = (getLife() == null ? "" : getLife().getClass().getSimpleName());
        return TYPE_AT + getLocation().toString() + CONTAINS + life;
    }
    
    
}






