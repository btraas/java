package ca.bcit.comp2526.a2b;


import java.awt.Color;
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

    private static final int COLOR_VARIATION = 90;
    private static final int MAX_COLOR = 255;
  
    private Color color;
    private Cell location;
    
    protected int minReproduce = 0;
    protected int maxReproduce = 0;
    
    protected int reproduceNeighbors = 0;
    protected int reproduceEmptySpaces = 0;
    protected int reproduceFood = 0;
    
 
    /**
     * Creates a Life.
     * @param location (Cell) of this Life
     * @param color this Life is (to paint the Cell with)
     */
    public Life(final Cell location, final Color color) {
        this.color = varyColor(location.getWorld().getSeed(), color);
        
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
    
    
    /**
     *  Gets the types that the food can be eaten
     *   by this type.
     * @return an array of viable classes.
     */
    public abstract Class<?>[] getFoodTypes();
    
    /**
     * Reproduce if the conditions are right.
     * 
     */
    protected void reproduce() {
        Cell thisCell = getCell();
        if (thisCell == null) {
            System.err.println("CELL IS NULL!?!?!");
            return;
        }
      
        Random seed = getCell().getWorld().getSeed();
      
        
        
        
      
        Cell[] nearbySpecies = getCell().getAdjacentCells(new Class[]{getClass()});
        Cell[] nearbyEmpty   = getCell().getAdjacentCells(new Class[]{null});
        Cell[] nearbyFood    = getCell().getAdjacentCells(this.getFoodTypes());
        
        if (    nearbySpecies.length < reproduceNeighbors
            ||  nearbyEmpty.length < reproduceEmptySpaces
            ||  nearbyFood.length < reproduceFood) {
            return;
        }
        
        if(this.getClass().getName().equals("Herbivore")) System.out.println("Reproducing "+this);
        
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
            
            chosen.setLife(create(chosen));
           // System.out.println("REPRODUCED FROM PLANT "+this.getCell().toString()+" TO "+chosen.toString());
            
            openSpots.remove(chosenIndex);
        }
        
        
        //System.out.println("REPRODUCING PLANT "+thisCell.toString()+" nearby cells: "+nearby.length+": nearbyplants:"+nearbySpecies.length + " nearbyempty:" + nearbyEmpty.length);
    
    }
    
    protected abstract Life create(final Cell location);
    
    private static int colorBound(int in) {
        return Math.max(Math.min(in, MAX_COLOR), 0);
    }
    
    private static Color varyColor(final Random seed, final Color in) {
       
        int red =   in.getRed() + seed.nextInt(COLOR_VARIATION) 
          - (COLOR_VARIATION/2);
        int green = in.getGreen() + seed.nextInt(COLOR_VARIATION)
          - (COLOR_VARIATION/2);
        int blue =  in.getBlue() + seed.nextInt(COLOR_VARIATION)
          - (COLOR_VARIATION/2);
        
      
        return new Color(   colorBound(red), 
                            colorBound(green), 
                            colorBound(blue));
    }
    
    public String toString() {
        return getCell().toString();
    }
}
