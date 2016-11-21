package ca.bcit.comp2526.a2b;

import java.awt.Point;

/**
 * A Hex Cell. Both a Hexagon and a Cell object.
 * Used for a hex grid (opposed to a square grid).
 * 
 * @author Brayden Traas
 * @version 2016-11-02
 */
public class HexCell extends Cell {
   
    private static final String UNLIKE_TYPES = "Comparing unlike Cell types!!";

   
    /**
     * Instantiates a HexCell.
     * 
     * @param world this Cell belongs to.
     * @param col this Cell resides on.
     * @param row this Cell resides on.
    */
    public HexCell(final World world, int col, int row) {
    	super(world, col, row);
        //hex = new Hexagon(new Point(valueX, valueY), radius, getEmptyColor());
        //this.circle = new Circle(10, Color.GREEN);
        
        init();     
    }
    
    
    @Override
    public Class<?> getShape() {
    	return HexCell.class;
    }
    

    
    /**
     * Finds the distance between this Cell and another Cell.
     * @param other Cell to compare with.
     * @return distance (double) between the two.
     */
    @Override
    public double distance(final Cell other) {

    	// Must be a HexCell.
        if (!(other instanceof HexCell)) {
            throw new RuntimeException(UNLIKE_TYPES);
        }
        
        // Instantly return Cell if this is the same cell.
        if (this.equals(other)) {
            return 0.0;
        }
        

        // If on the same column
        if (this.getColumn() == other.getColumn()) {
            return Math.abs(this.getRow() - other.getRow());
        }
 
        Point otherTheoretical = new Point(other.getLocation());
        
        /*
         * Backwards, I know... Too late to go back now
         * 
         * Scratch that, changing now. TODO remove this
         * 
         */
        int thisX = getColumn();
        int thisY = getRow();
        int otherX = other.getColumn();
        int otherY = other.getRow();
        
        
        
        double diffY = Math.abs(thisY - otherY);
        
        // If this is even and other isn't
        if ( (thisX % 2) == 0 && (otherX % 2) != 0) {
            
            if (thisY < otherY) {
                otherTheoretical.y--;
            }

        }
        
    
        // int distance = max(    
        int dist1 = (int)Math.ceil(1 - (otherX / 2)) + (int)otherY;
        dist1 -= ((int)Math.ceil(1 - (thisX / 2)) + (int)thisY);
        dist1 = Math.abs(dist1);
         
        int dist2 = -otherX - (int)Math.ceil(1 - (otherX / 2)) 
                    - (int)otherY;
        dist2 += thisX + Math.ceil(1 - (thisX / 2)) + thisY;
        dist2 = Math.abs(dist2);
          
        //System.out.println("\n diffX:"+diffX+" dist1:"+dist1+" dist2:"+dist2 );
        int distance = Math.max(Math.max(dist1, dist2), (int)diffY);
              
        //System.out.println("distance from "+this+" to "+other+" is "+distance);
        
        return distance;
         
    }
    
}

