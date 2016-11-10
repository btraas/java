package ca.bcit.comp2526.a2a;

import java.awt.Color;

/**
 * <p>A life.</p>
 * 
 * <p>Parent class of Animal, Plant etc.
 *  A life belongs to a Cell, and
 *  a cell contains 0 or 1 Life.</p>
 *  
 * <p>If no Cell contains this life,
 *  it's considered dead. Coincidentally,
 *  there will be no reference to this
 *  Life, and thus it will be GC'd from
 *  memory.</p>
 * 
 * @author Brayden Traas
 * @version 2016-10-22
 */
public abstract class Life {

    private Color color;
    private Cell location;
 
    /**
     * Creates a Life.
     * @param location (Cell) of this Life
     * @param color this Life is (to paint the Cell with)
     */
    public Life(final Cell location, final Color color) {
        this.color = color;
        setLocation(location);
    }
    
    /**  
     * Must be implemented. Explicitly specify what happens to this type of Life
     *  At the end of each turn.
     * 
     */
    public abstract void processTurn();
    
    /**
     * Destroys this Life, and its two-way Cell reference. 
     */
    public void destroy() {
        Cell oldCell = this.getCell();
        this.setCell(null);
        oldCell.setLife(null);
    }
    
    /**
     * Sets the location of this Life object.
     * @param location to set
     */
    public void setLocation(final Cell location) {
        this.location = location;
    }
    
    
    /**
     * Alias for setLocation().
     * @param location to set
     */
    public void setCell(final Cell location) {
        setLocation(location);
    }
    
    /**
     * Returns the location of the life.
     * @return the location
     */
    public Cell getLocation() {
        return location;
    }
    
    /**
     * Alias for getLocation().
     * @return the location of the cell
     */
    public Cell getCell() {
        return getLocation();
    }
    
    /**
     * Returns the color of the life.
     * @return color
     */
    public Color getColor() {
        return color;
    }
    
    
}
