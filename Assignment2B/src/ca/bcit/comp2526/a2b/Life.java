package ca.bcit.comp2526.a2b;


import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


/**
 * <p>A life.</p>
 * 
 * <p>A life belongs to a Cell, and
 *  a cell contains 0+ Lives.</p>
 *  
 * <p>If no Cell contains this life,
 *  it's considered dead. Coincidentally,
 *  there will be no reference to this
 *  Life, and thus it will be GC'd from
 *  memory.</p>
 * 
 * @author Brayden Traas
 * @version 2016-11-22
 */
public final class Life implements Moveable<Cell> {

    private static final String REPRODUCE_CELL_NULL = "ERROR: Cell is null!?";
    private static final String NO_MOVES = "NO MOVES";
    private static final String EATING = "Eating";
    
    private static final int COLOR_VARIATION = 120;
    private static final double DARKEN_AMOUNT = (Settings.getInt("darkenPercent")) / 100.0;
    private static final int MAX_COLOR = 255;
    private static final Matter[] NONEMPTY_MATTER = Matter.combine(
        Terrain.getDifficults(), 
        LifeType.values());
  
    protected LifeType type;
    
    private Color originalColor;
    protected Color color;
    private Cell location;
    
    protected int life;

    protected Cell previousLocation = null;
    
 
    /**
     * Creates a Life.
     * @param type - LifeTools.Type of this Life
     * @param location (Cell) of this Life
     */
    public Life(final LifeTools.Type type, final Cell location) {
        this(LifeTools.getLifeType(type), location);
    }
    
    /*
     * Does the heavy lifting of the constructing a Life.
     * Also called internally.
     */
    private Life(final LifeType type, final Cell location) {
        this.type = type;
        this.color = varyColor(
            location.getWorld().getSeed(), 
            this.type.color);

        // must save since we don't change the Enum's color.
        this.originalColor = this.color; 
        
        this.location = location;

        this.life = this.type.initFood;
    }
    
    /*
     * Creates a new life in the given location.
     * Used internally for reproduction.
     */
    private Life create(final Cell location) {
        return new Life(type, location);
    }
    
    /**  
     * Processes this Life's turn. Darken, decrease life, move, eat, reproduce.
     * 
     */
    public void processTurn() {
        
        if (World.DEBUG) {
            System.out.println("Processing turn for a " 
                    + this.getClass().getSimpleName() + " in " + getCell());
        }
        
        this.darken();
        if (--life < 0) {
            if (this.getCell() != null) {
                this.getCell().removeLife(this);
            }
            this.destroy();
            return;
        }
        
        int movesLeft = getMoveMax();
        
        if (movesLeft > 0) {
            this.move();
            movesLeft--;
        }
        
        boolean eaten = this.eat();
        
        // if not eaten and can still move
        while (!eaten && movesLeft > 0) {
            this.move();
            movesLeft--;
            eaten = this.eat();
        }

        
        
        this.reproduce();

    }
    
    /**
     * True if this Life can move.
     * @return true if this Life can move.
     */
    public boolean moveable() {
        return getMoveMax() > 0;
    }
    
    /**
     * Destroys this Life, and its two-way Cell reference. 
     */
    public void destroy() {
        Cell oldCell = this.getCell();
        this.setCell(null);
        if (oldCell != null) {
            oldCell.removeLife(this); // remove this life
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
    public Matter[] getIncompatibleTypes() {
        // System.out.println(type + " incompatible " + Arrays.asList(type.getIncompatibleTypes()));
        return type.getIncompatibleTypes();
    }
    
    /**
     * Gets the incompatible types.
     * @return incompatible types.
     */
    public Matter[] getInvalidMoveToTypes() {
        return getIncompatibleTypes();
    }
    
    /**
     * Gets the possibilities to move to from here.
     * @param min - min distance possible to move.
     * @param max - max distance possible to move.
     * @return an array of possible destination Cells.
     */
    @Override
    public Cell[] getMoveToPossibilities(int min, int max) {
        
        Matter[] invalidTypes = type.getIncompatibleTypes();
        List<Cell> cells = new ArrayList<Cell>();
        
        
        Cell[] possibilities = getCell().getNearbyCells(min, max);
                
        for (int i = 0; i < possibilities.length; i++ ) {
          
            Cell newCell = possibilities[i];
          
            // If the found cell != this cell
            if (newCell != null && newCell != getCell()) {
                
                // If defined types is null, we're allowing all types.
                if ( invalidTypes == null 
                        || invalidTypes.length == 0) {
                    cells.add(newCell);
                    continue;
                }
            
                boolean occupiersValid = true;
                boolean terrainValid = true;
                
                for (int k = 0; k < type.getIncompatibleTypes().length; k++) {
                    // if one of the lives is this type, or this cell is this type.
                    if (newCell.has(invalidTypes[k])) {
                        occupiersValid = false;
                    } 
                    if (newCell.is(new Matter[]{ invalidTypes[k] })) {
                        terrainValid = false;
                    }
                }
                
                if (occupiersValid && terrainValid) {
                    cells.add(newCell);
                }
            }
        }
      
        return cells.toArray(new Cell[cells.size()]);   
    }
    
    
    /**
     * Reproduce if the conditions are right.
     * 
     */
    protected void reproduce() {
        // System.out.println("Reproducing "+this.getClass().getSimpleName());
        Cell thisCell = getCell();
        if (thisCell == null) {
            System.err.println(REPRODUCE_CELL_NULL);
            return;
        }
      
        Random seed = getCell().getWorld().getSeed();
      
        // Gets adjacent cells containing this species.
        Cell[] nearbySpecies = getCell().getAdjacentCellsWith(new Matter[] { type });
        
        // Gets adjacent cells that 'aren't-a' and don't 'contain a' Life or Terrain object.
        Cell[] nearbyEmpty   = getCell().getAdjacentCellsWithout( NONEMPTY_MATTER );
        
        // Gets adjacent cells that are-a or contain-a type we can eat.
        Cell[] nearbyFood    = getCell().getAdjacentCellsWith(type.getFoodTypes());
            
        if (    nearbySpecies.length < type.reproduction.minNeighbors
            ||  nearbyEmpty.length < type.reproduction.minEmpty
            ||  nearbyFood.length < type.reproduction.minFood) {
            return;
        }
        if (World.DEBUG) {
            System.out.println("Reproducing " + this);
        }
        
        int offspringCount = getCell().getWorld().getSeed().nextInt(
            (type.reproduction.maxSpawn - type.reproduction.minSpawn) + 1) 
                + type.reproduction.minSpawn;
        
        if (World.DEBUG) {
            System.out.println("reproducing " + offspringCount + " " + type + "s");
        }
        
        ArrayList<Cell> openSpots = new ArrayList<Cell>(Arrays.asList(nearbyEmpty));
        
        offspringCount = Math.min(offspringCount, openSpots.size());
        for (int i = 0; i < offspringCount; i++) {
          
            if (World.DEBUG) {
                for (int j = 0; j < nearbyEmpty.length; j++) {
                    System.out.println(" " + nearbyEmpty[j]);
                }
            }
            
          
            int chosenIndex = seed.nextInt(openSpots.size());
            Cell chosen = openSpots.get(chosenIndex);
            
            chosen.addLife(create(chosen));
            if (World.DEBUG) {
                System.out.println("REPRODUCED FROM PLANT " + this.getCell().toString() 
                        + " TO " + chosen.toString());
            }
            
            openSpots.remove(chosenIndex);
        }
        
        
      
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
        int red     = (int)(color.getRed() * DARKEN_AMOUNT);
        int green   = (int)(color.getGreen() * DARKEN_AMOUNT);
        int blue    = (int)(color.getBlue() * DARKEN_AMOUNT);
    
        
        this.color = new Color(
            colorBound(red),
            colorBound(green),
            colorBound(blue));
    }
    
   
    
    /**
     * Gets the amount of life left.
     * @return foodSupply (int)
     */
    public final int getLifeLeft() {
        return life;
    }
    
   
    
    /**
     * Gets all the possible move options for this animal.
     * @return an array of new locations (Cell objects)
     */
    protected final Cell[] getMoveOptions() {
        
        // getMoveToLifeTypes() and getMoveDistance() must be defined in child class, 
        //  as per interface.
        // return this.getMoveToPossibilities(getMoveMin(), getMoveMax());
        return this.getMoveToPossibilities(getMoveMin(), 1);
    }
    
    /**
     * Moves an animal to any valid adjacent cell at random.
     * Uses a seed that's dependent on the World object.
     */
    @Override
    public void move() {
      
        if (this.getCell() == null) {
            return;
        }
      
        World<?> thisWorld = this.getCell().getWorld();
        Random seed = thisWorld.getSeed();
        
        //MoveDecision decision = this.getMoveDecision(seed, getMoveOptions());
        Cell newCell = this.decideMove(seed, getMoveOptions()); //decision.decide();
        
        
        //  System.out.println("Old cell: "+this.getCell().toString());
        
        
        
        // System.out.println("Moving to: "+newCell.getLocation().toString());
      
        if (newCell == null) {
            if (World.DEBUG) {
                System.err.println(NO_MOVES);
            }
            return;
        }
        
        // Clear current Cell's reference to this Life
        this.getCell().removeLife(this);
        
        // Save previous location
        this.previousLocation = this.getCell();
        // Set two-way reference (to Cell & Life)
        this.setCell(newCell);
        /*
        if (this.getCell().getLife() != null) {
            this.getCell().getLife().destroy();
        }
        */
        this.getCell().addLife(this);
        
        if (World.DEBUG && this.getCell().terrain == Terrain.WATER) {
            System.out.println(getCell() + " NOW CONTAINS A " + getClass().getSimpleName());
        }
    }

    @Override
    public int getMoveMin() {
        return type.move.min;
    }
    
    @Override
    public int getMoveMax() {
        return type.move.max;
    }

    @Override
    public Cell decideMove(Random seed, Cell[] options) {
        
        // Either life left or cap
        int senseDistance = Math.max(Settings.getInt("maxSenseDistance"), life + 1);
        
        // Can't move
        if (type.move.max < 1) {
            return null;
        }
        
        // Can move more than one space.
        if (type.move.max > 1) {
            return (new DistanceDecision(seed, options, 
                    (Matter[])type.getFoodTypes(), 
                    type.getAvoid(), this, 
                    senseDistance)).decide();
        }
        
        Matter[] avoid = type.getAvoid();
        
        return (new NearbyDecision(
                seed, options, type.getFoodTypes(), avoid)).decide();  
    
    }

    /**
     * Defines what happens when an animal eats.
     */
    public final boolean eat() {
        
        boolean eaten = false;
        
        // See if there's food in this Cell. Eat it if so.
        // But not this one!
        // has() -> one of food types, not null, not this.
        if (this.getCell().has(type.getFoodTypes(), null, this)) {
            Life food = this.getCell().getLife(type.getFoodTypes());
            if (World.DEBUG) {
                System.out.println("  " + EATING + " " + food.getClass().getSimpleName());
                        
            }
            this.getCell().removeLife(food);
            
            eaten = true;
            
        // See if this Cell is edible. Eat it if so.
        //  currently, a Cell doesn't change when eaten.
        } else if (this.getCell().is(type.getFoodTypes())) {
            eaten = true;
        }
        
        if (eaten) {
            this.life = type.initFood;
            this.color = originalColor;
        }
        
        return eaten;

    } 
    
    @Override
    public String toString() {
    
        Point point = getCell() == null ? new Point(0, 0) : getCell().getLocation();
        return type + " Life:" + getLifeLeft() 
            + " @" + point.x + "," + point.y;
    }
}
