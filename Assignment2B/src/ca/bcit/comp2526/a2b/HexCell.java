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

    private static final float HALF = 0.5f;

   
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
    public HexCell(final World world, int row, int col) {
    	super(world, row, col);
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
    //@Override
    public double distanceOld(final Cell other) {

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
         */
        int thisX = getRow();
        int thisY = getColumn();
        int otherX = other.getRow();
        int otherY = other.getColumn();
        
        
        
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

