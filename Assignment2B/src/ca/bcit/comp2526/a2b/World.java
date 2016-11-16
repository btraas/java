package ca.bcit.comp2526.a2b;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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
    public World(int rows, int columns) {
      
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
        cells = new Cell[rows][columns];
       
        /*
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                cells[i][j] = new Cell(this, i, j);
                cells[i][j].setLife(Creator.createLife(cells[i][j], randomSeed));
            }
        }
        */
    }

    /**
     * Gets the cells of the World.
     * @return an ArrayList of cells.
     */
    public ArrayList<Cell> getCells() {
    	ArrayList<Cell> cells = new ArrayList<Cell>();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
              
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
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
              
                if (cells[i][j] != null && cells[i][j].getLife(Object.class) != null) {
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
        
        // Shuffle the order of the lives to process with the World's seed.
       // Collections.shuffle(lives, getSeed());
        
        HashMap<Class<?>, Integer> map = new HashMap<Class<?>, Integer>();
        map.put(Plant.class, 0);
        map.put(Herbivore.class, 0);
        map.put(Carnivore.class, 0);
        
        // Do this separately so we don't reprocess tiles.
        for (Life occupier: lives) {
            if (occupier != null) {
          //  	map.put(occupier.getClass(), map.get(occupier.getClass())+1);
                occupier.processTurn();
            }
        }
        
       // int plants = map.get(Plant.class);
       //  int herbs = map.get(Herbivore.class);
       // int carns = map.get(Carnivore.class);
       // System.out.print("Plants:"+plants+" Herbs:"+herbs
       // 	+" Carns:"+carns + " Total: " + (plants+herbs+carns));
       // lastTotal = plants+herbs+carns;
        
        // Update text on each cell. Only for square cells... HexCells are handled differently.
       repaintCells();
        
    }

    
    /**
     * Repaints cells.
     */
    public void repaintCells() {
    	for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
              
                if (cells[i][j] != null && (cells[i][j] instanceof SquareCell)) {
                    ((SquareCell)cells[i][j]).setText();
                    //((SquareCell)cells[i][j]).recolor();
                }
            }
        }
    }
    
    
    /**
     * Creates & returns the Cell at this location.
     * 
     * @param row the row this Cell is found in
     * @param column the column this Cell is found in
     * @return the desired Cell object
     */
    public Cell createCellAt(final Cell newCell, int row, int column) {
      
        // Return null if this row/column is outside bounds.
        if (row < 0 || column < 0) {
            return null;
        }
        if (row >= rows || column >= columns) {
            return null;
        }
        cells[row][column] = newCell; // new Cell(this, row, column);
        cells[row][column].addLife(Creator.createLife(cells[row][column], randomSeed));

        return cells[row][column];
    }
    
    /**
     * Returns the Cell at this location.
     * 
     * @param row the row this Cell is found in
     * @param column the column this Cell is found in
     * @return the desired Cell object
     */
    public Cell getCellAt(int row, int column) {
      
        // Return null if this row/column is outside bounds.
        if (row < 0 || column < 0) {
            return null;
        }
        if (row >= rows || column >= columns) {
            return null;
        }
        
        return cells[row][column];
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
