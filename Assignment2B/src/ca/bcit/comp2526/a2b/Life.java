package ca.bcit.comp2526.a2b;


import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;


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

	// private ArrayList<Point> points;
	
    private static final int COLOR_VARIATION = 80;
    private static final double DARKEN_AMOUNT = (Settings.getInt("darkenPercent")) / 100.0;
    private static final int MAX_COLOR = 255;
  
    protected Color originalColor;
    protected Color color;
    private Cell location;
    
    protected Class<?>[] incompatibleTypes;
    protected Class<?>[] foodTypes;
    
    protected int minReproduce = 0;
    protected int maxReproduce = 0;
    
    protected int reproduceNeighbors = 0;
    protected int reproduceEmptySpaces = 0;
    protected int reproduceFood = 0;
    
    protected int life = 4;
    protected int eatAmount = 0;

    protected Cell previousLocation = null;
    
 
    /**
     * Creates a Life.
     * @param location (Cell) of this Life
     * @param color this Life is (to paint the Cell with)
     */
    public Life(final Cell location, final Color color, final int initialLife, 
    		final Class<?>[] foodTypes) {
        this.color = varyColor(location.getWorld().getSeed(), color);
        this.life = initialLife;
        this.originalColor = color;
        this.foodTypes = foodTypes;
        
      
        this.location = location;


        //setBackground(location.getEmptyColor());
        //setSubLocation();
        //location.setText(location.getText());
        //repaint();
    }
    
    protected abstract Life create(final Cell location);
    
    /**  
     * Can be overriden. Explicitly specify what happens to this type of Life
     *  At the end of each turn.
     * 
     */
    public void processTurn() {
    	
    	if(World.DEBUG) {
    		System.out.println("Processing turn for a " + this.getClass().getSimpleName() + " in " + getCell());
    	}
    	
    	this.darken();
    	if (--life < 0) {
            if (this.getCell() != null) {
            	this.getCell().removeLife(null);
            }
            this.destroy();
            return;
        }
    	
    	
    }
    
    /**
     * Destroys this Life, and its two-way Cell reference. 
     */
    public void destroy() {
        Cell oldCell = this.getCell();
        this.setCell(null);
        if (oldCell != null) {
        	oldCell.removeLife(null); // remove all
        }
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
    public Cell getCell() {
        return location;
    }
    
    /**
     * Returns the previous location of the life.
     * @return the previous location
     */
    public Cell getPreviousCell() {
    	return previousLocation;
    }
    
    
    /**
     * Returns the color of the life.
     * @return color
     */
    public Color getColor() {
        return color;
    }
    
    /**
     * Gets the incompatible types.
     * @return incompatible types.
     */
    public Class<?>[] getIncompatibleTypes() {
        return incompatibleTypes;
    }
    
    /**
     * Gets the incompatible types.
     * @return incompatible types.
     */
    public Class<?>[] getInvalidMoveToTypes() {
    	return getIncompatibleTypes();
    }
    
    /**
     * Reproduce if the conditions are right.
     * 
     */
    protected void reproduce() {
    	// System.out.println("Reproducing "+this.getClass().getSimpleName());
        Cell thisCell = getCell();
        if (thisCell == null) {
            System.err.println("CELL IS NULL!?!?!");
            return;
        }
      
        Random seed = getCell().getWorld().getSeed();
      
        // Gets adjacent cells containing this species.
        Cell[] nearbySpecies = getCell().getAdjacentCellsWith(new Class[]{getClass()});
        
        // Gets adjacent cells that 'aren't-a' and don't 'contain a' Life or Terrain object.
        Cell[] nearbyEmpty   = getCell().getAdjacentCellsWithout(new Class[]{Life.class, WaterCell.class});
        
        // Gets adjacent cells that are-a or contain-a type we can eat.
        Cell[] nearbyFood    = getCell().getAdjacentCellsWith(this.foodTypes);
        
       // System.out.println("REPRODUCING PLANT "+thisCell.toString()+" nearby cells: "+getCell().getAdjacentCells().length+": nearbyplants:"+nearbySpecies.length + " nearbyempty:" + nearbyEmpty.length);
     
        if (    nearbySpecies.length < reproduceNeighbors
            ||  nearbyEmpty.length < reproduceEmptySpaces
            ||  nearbyFood.length < reproduceFood) {
            return;
        }
        // System.out.println("DING DING Reproducing "+this);
        
        int offspringCount = getCell().getWorld().getSeed().nextInt(
            (maxReproduce - minReproduce) + 1) + minReproduce;
        
        
        ArrayList<Cell> openSpots = new ArrayList<Cell>(Arrays.asList(nearbyEmpty));
        for (int i = 0; i < offspringCount; i++) {
          
         //   System.out.println("reproducing... open spots: ");
            for(int j=0; j<nearbyEmpty.length; j++) {
        //        System.out.println(" "+nearbyEmpty[j]);
            }
          
            int chosenIndex = seed.nextInt(openSpots.size());
            Cell chosen = openSpots.get(chosenIndex);
            
            chosen.addLife(create(chosen));
           // System.out.println("REPRODUCED FROM PLANT "+this.getCell().toString()+" TO "+chosen.toString());
            
            openSpots.remove(chosenIndex);
        }
        
        
        //System.out.println("REPRODUCING PLANT "+thisCell.toString()+" nearby cells: "+nearby.length+": nearbyplants:"+nearbySpecies.length + " nearbyempty:" + nearbyEmpty.length);
    
    }
    
    
    private static int colorBound(int in) {
        return Math.max(Math.min(in, MAX_COLOR), 0);
    }
    
    private static Color varyColor(final Random seed, final Color in) {
       
        int red =   in.getRed() + seed.nextInt(COLOR_VARIATION);
        int green = in.getGreen() + seed.nextInt(COLOR_VARIATION);
        int blue =  in.getBlue() + seed.nextInt(COLOR_VARIATION);
        
      
        return new Color(   colorBound(red), 
                            colorBound(green), 
                            colorBound(blue));
    }
    
    private void darken() {
    	int red 	= (int)(color.getRed() * DARKEN_AMOUNT);
    	int green 	= (int)(color.getGreen() * DARKEN_AMOUNT);
    	int blue    = (int)(color.getBlue() * DARKEN_AMOUNT);
    
    	
    	this.color = new Color(
    						colorBound(red),
    						colorBound(green),
    						colorBound(blue));
    }
    
   
    public static String typesToString(Class<?>[] types) {
    	String string = "";
    	for (int i = 0; i < types.length; i++) {
    		string += types[i].getSimpleName() + ", ";
    	}
    	string = string.substring(0, (string.length())-2);
    	return string;
    }
    
    /**
     * Gets the amount of life left.
     * @return foodSupply (int)
     */
    public final int getLifeLeft() {
        return life;
    }
    
   
    
    public String toString() {
    	
    	Point point = getCell() == null ? new Point(0, 0) : getCell().getLocation();
        return this.getClass().getSimpleName() + " Life:" + getLifeLeft() 
        	+ " Loc:"+point.x+","+point.y;
    }
}
