package ca.bcit.comp2526.a2a;

import java.awt.Color;
import java.awt.Point;

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
public interface Cell {

    /**
     * The Cell's empty color.
     */
    Color EMPTY_COLOR = Color.WHITE;

    /**
     * The Cell's border color.
     */
    Color BORDER_COLOR = Color.BLACK;
   
    /**
     * Gets the Point location of this Cell.
     * @return location (Point)
     */
    Point getLocation();
    
    /**
     * Gets the possibilities to move to from here.
     * @param types - allowable types we can move into.
     * @param distance - max distance possible to move.
     * @return an array of possible destination Cells.
     */
    Cell[] getMoveToPossibilities(final Class<?>[] types, int distance);
    
    /**
     * Gets all adjacent cells, whether occupied or not.
     * @return an array of adjacent Cells.
     */
    Cell[] getAdjacentCells();
    
    /**
     * Gets the Life object this Cell contains.
     * @return the Life object or null.
     */
    Life getLife();
    
    /**
     * Sets the Life object this Cell contains.
     * @param occupier to set for this Cell.
     */
    void setLife(final Life occupier);
  
    /**
     * Gets the Row this Cell belongs to.
     * @return the row.
     */
    int getRow();
    
    /**
     * Sets the row this Cell exists in.
     * @param row - new row for this Cell.
     */
    void setRow(int row);
  
    /**
     * Gets the column this Cell exists in.
     * @return the column this Cell exists in.
     */
    int getColumn();
    
    /**
     * Updates the column this Cell exists in.
     * @param col - column to set.
     */
    void setColumn(int col);
    
    /**
     * Gets the World this Cell resides in.
     * @return the World object reference.
     */
    World getWorld();
    
    /**
     * Of a given haystack, finds the closest Cell to this one.
     * @param haystack - an array of Cells to choose from.
     * @return the decided cell.
     */
    Cell closest(final Cell[] haystack);
    
    /**
     * Calculates the distance between this Cell and a given Cell.
     * @param other Cell to compare with.
     * @return the distance.
     */
    double distance(final Cell other);
    
    /**
     * Gets the text to display on this Cell.
     * @return the text to display (String).
     *     
     */
    String getText();
    
    
}
