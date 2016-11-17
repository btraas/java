package ca.bcit.comp2526.a2b;

import java.awt.Point;



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
public class SquareCell extends Cell {


   
    //protected Color emptyColor = EMPTY_COLOR;

  
    /**
     * Creates a Cell object.
     * 
     * @param world - the world this Cell belongs to
     * @param row this Cell is found in
     * @param column this cell is found in
     */
    public SquareCell(final World world, int row, int column) {
        super(world, row, column);
        init();
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
 


    
}






