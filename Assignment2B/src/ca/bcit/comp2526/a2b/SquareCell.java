package ca.bcit.comp2526.a2b;



/**
 * <p>Square Cells within a world.</p>
 * 
 * @author Brayden Traas
 * @version 2
 */
public final class SquareCell extends Cell {
   
    //protected Color emptyColor = EMPTY_COLOR;

  
    /**
     * Creates a Cell object.
     * 
     * @param world - the world this Cell belongs to
     * @param row this Cell is found in
     * @param column this cell is found in
     */
    public SquareCell(final World<SquareCell> world, int column, int row) {
        super(world, column, row);
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






