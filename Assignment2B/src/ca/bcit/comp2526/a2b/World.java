package ca.bcit.comp2526.a2b;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * World to contain cells.
 * Also holds the seed that all Classes refer to.
 * 
 * @author Brayden Traas
 * @version 2016-10-22
 */
public final class World {

	private static long lastTime;
	//private static int lastTotal;
	
	/**
	 * Debug this application?
	 */
    public static final boolean DEBUG = Settings.getBoolean("debug");
	
    /**
     * Decides if we show food on each Cell.
     */
    public static final boolean SHOW_FOOD = Settings.get("filltext").equalsIgnoreCase("food");
    
    /**
     * Decides if we show moves on each Cell.
     */
    public static final boolean SHOW_MOVES = Settings.get("filltext").equalsIgnoreCase("moves");
    
    /**
     * Decides if we have chosen to show coordinates on each Cell.
     */
    public static final boolean SHOW_COORDINATES = 
        Settings.get("filltext").equalsIgnoreCase("coordinate");
    
    /**
     * Decides if borders are visible.
     */
    public static final boolean VISIBLE_LINES = Settings.getBoolean("visibleLines");
    
    /**
     * True if this is a hex grid.
     */
    public static final boolean HEX = Settings.get("gridType").equalsIgnoreCase("hex");
    
    private final long seed;
    private final Random randomSeed;
    private int rows;
    private int columns;
    
    private Cell[][] cells;
  
    /** 
     * Creates a World. If a seed is loaded from settings, use it.
     *  Otherwise, a random seed is used (based on current time).
     * 
     * @param rows to instantiate with.
     * @param columns to instantiate with.
     */
    public World(int columns, int rows) {
      
        double seed = Settings.getDouble("seed");
        seed = seed > 0 ? seed : System.currentTimeMillis();
      
        System.out.println("Seed: " + seed);
        
        this.seed = (long)seed;
        this.randomSeed = new Random((long)seed);
        this.rows = rows;
        this.columns = columns;
    }
    
    /**
     * Initializes the World with Cells and Life within the cells.
     */
    public void init() throws IOException {
      
        // Cell objects now created later; 
        // the World object doesn't know the type of the Cell (Hexagon or Square)
        cells = new Cell[columns][rows];
       
       
    }

    /**
     * Gets the cells of the World.
     * @return an ArrayList of cells.
     */
    public ArrayList<Cell> getCells() {
    	ArrayList<Cell> cells = new ArrayList<Cell>();
        for (int i = 0; i < columns; i++) {
            for (int j = 0; j < rows; j++) {
              
                if (this.cells[i][j] != null) {
                    cells.add(this.cells[i][j]);
                }
            }
        }
        return cells;
    }
    
    /**
     * Gets the current lives of the World.
     * @return an ArrayList of lives.
     */
    public ArrayList<Life> getLives() {
    	ArrayList<Life> lives = new ArrayList<Life>();
        for (int i = 0; i < columns; i++) {
            for (int j = 0; j < rows; j++) {
              
            	/*
            	 *  getLife(LifeType.values()) denotes:
            	 * 	 Get any life that:
            	 *   	: is-a Life
            	 *   
            	 *   Ergo, any life.
            	 */
            	
                   if (cells[i][j] != null 
                		   && cells[i][j].getLife(LifeType.values()) != null) {
                    lives.addAll(cells[i][j].getLives());
                }
            }
        }
        //System.out.println(lives);
        return lives;
    }
    
    /**
     * Processes the turn. Essentially finds the Life objects
     *  within the Cells, and runs its processTurn() method.
     */
    public void takeTurn() {
      
    	long thisTime = System.currentTimeMillis();
    	long diff = (thisTime - lastTime);
    	
    	// long total = lastTotal+1;
    	
    	System.out.println(" Turn time: " + diff + "ms");// + " total: "+lastTotal+" ratio:"
        		//	+ratio);
    	
    	lastTime = thisTime;
    	
        //boolean showFood = Boolean.parseBoolean(Settings.get.getProperty("showfood"));
        //System.out.println("\nSHOWFOOD: "+showFood+"\n");
      
        ArrayList<Life> lives = getLives();
        
        boolean debug = Settings.getBoolean("debug");
        // Shuffle the order of the lives to process with the World's seed.
        if (!debug) {
        	Collections.shuffle(lives, getSeed());
        } else {
        	runDebug();
        }
        //if(debug) return;
        
        // Do this separately so we don't reprocess tiles.
        for (Life occupier: lives) {
            if (occupier != null) {
          //  	map.put(occupier.getClass(), map.get(occupier.getClass())+1);
                occupier.processTurn();
            }
        }
        
    
        
    }

    private void runDebug() {

    	int lives = 0;
    	
    	for (int i = 0; i < columns; i++) {
    		for (int j = 0; j < rows; j++) {
    			
    			// if(cells[i][j] == null) continue;
    			Life l = cells[i][j].getLife(LifeType.values());
    			if (l != null)
    				lives += cells[i][j].getLives().size();
    				//System.out.print(cells[j][i].getLives().size()+",");
    		}
    		//System.out.println();
    	}
    	System.out.println("lives: "+lives+" found: "+getLives().size());
    	
    }
    

    
    /**
     * Creates & returns the Cell at this location.
     * 
     * @param row the row this Cell is found in
     * @param column the column this Cell is found in
     * @return the desired Cell object
     */
    public Cell createCellAt(final Cell newCell, int column, int row) {
      
        // Return null if this row/column is outside bounds.
        if (row < 0 || column < 0) {
            return null;
        }
        if (row >= rows || column >= columns) {
            return null;
        }
        cells[column][row] = newCell; // new Cell(this, column, row);
        cells[column][row].addLife(Creator.createLife(cells[column][row], randomSeed));

        return cells[column][row];
    }
    
    /**
     * Returns the Cell at this location.
     * 
     * @param row the row this Cell is found in
     * @param column the column this Cell is found in
     * @return the desired Cell object
     */
    public Cell getCellAt(int column, int row) {
      
        // Return null if this row/column is outside bounds.
        if (row < 0 || column < 0) {
            return null;
        }
        if (row >= rows || column >= columns) {
            return null;
        }
        
        return cells[column][row];
    }
    
    /**
     * Gets the row count.
     * @return number of rows.
     */
    public int getRowCount() {
        return rows;
    }
    
    /**
     * Gets the column count.
     * @return number of columns.
     */
    public int getColumnCount() {
        return columns;
    }
  
    /**
     * Returns the seed of the current world.
     * Available to whole package but not public.
     * @return seed of this world
     */
    public Random getSeed() {
        return randomSeed;
    }

    /**
     * Returns the seed (long form) of the current world.
     * @param raw to differentiate between getSeed()
     * @return long seed
     */
    public long getSeed(boolean raw) {
        return seed;
    }
    
    
}
