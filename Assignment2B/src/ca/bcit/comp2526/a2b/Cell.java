package ca.bcit.comp2526.a2b;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;

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
    Color EMPTY_COLOR = new Color(163, 117, 73);

    /**
     * The Cell's border color.
     */
    Color BORDER_COLOR = Color.WHITE;
   
    /**
     * The Cell's text color.
     */
    Color TEXT_COLOR = Color.WHITE;
    
    /**
     * Returns the EMPTY_COLOR variable.
     * @return Color for backgrounds.
     */
    Color getEmptyColor();
    
    /**
     * Gets the Point location of this Cell.
     * @return location (Point)
     */
    Point getLocation();
    
    
    /**
     * Gets the Cells within the given distance.
     * 
     * @param min distance to travel
     * @param max distance to travel
     * @return an array of valid Cells.
     */
    Cell[] getNearbyCells(int min, int max);
    
    /**
     * Gets all adjacent cells, whether occupied or not.
     * @return an array of adjacent Cells.
     */
    Cell[] getAdjacentCells();
    
    /**
     * Gets all adjacent cells occupied with the given array of types.
     * @return an array of valid adjacent Cells.
     */
    Cell[] getAdjacentCellsWith(final Class<?>[] validTypes);
    
    /**
     * Gets all adjacent cells occupied without the given array of types.
     * @return an array of valid adjacent Cells.
     */
    Cell[] getAdjacentCellsWithout(final Class<?>[] invalidTypes);
    
    /**
     * Gets the Life objects this Cell contains.
     * @return the Life objects (ArrayList) or null.
     */
    ArrayList<Life> getLives();
    
    /**
     * Sets the Life object this Cell contains.
     * @param occupier to set for this Cell.
     */
    void addLife(final Life occupier);
    
    /**
     * Removes a Life object this Cell contains.
     * @param occupier to remove for this Cell.
     */
    void removeLife(final Life occupier);
  
    
    /**
     * Get a Life object this Cell contains of this type.
     * @param type to select
     * @return Life of this type found.
     */
    Life getLife(final Class<?> type);
    
    /**
     * Get a Life object this Cell contains of these types.
     * @param types to select
     * @return Life of this type found.
     */
    Life getLife(final Class<?>[] types);
    
    /**
     * Returns true if this Cell has a Life of this type
     * @param types to check
     * @return true if it has this, otherwise false.
     */
    boolean has(final Class<?> type);
    
    /**
     * Returns true if this Cell has a Life of one of these types
     * @param types to check
     * @return true if it has this, otherwise false.
     */
    boolean has(final Class<?>[] types);
    
    /**
     * Returns true if this Cell is an instance of these types.
     * @param types to check for.
     * @return true if this is one of the given types.
     */
    boolean is(final Class<?>[] types);
    
    /**
     * Gets the size of this Cell.
     * @return the size as a Dimension.
     */
    Dimension getSize();
    
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
    
    /**
     * Sets the text to display on this Cell.
     * @param the text to display.
     * @param text
     */
    void setText(String text);
    
    /**
     * Recolor this cell.
     */
    void recolor();
    
}
