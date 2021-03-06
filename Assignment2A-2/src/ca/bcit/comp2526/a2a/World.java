package ca.bcit.comp2526.a2a;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * World to contain cells.
 * Also holds the seed that all Classes refer to.
 * 
 * @author Brayden Traas
 * @version 2016-10-22
 */
public final class World {

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
     * Decides the empty Color for elements.
     */
    public static final Color EMPTY_COLOR = Color.WHITE;

    /**
     * Decides the border Color for elements.
     */
    public static final Color BORDER_COLOR = Color.BLACK;
    
    /**
     * Decides the text Color for elements.
     */
    public static final Color TEXT_COLOR = Color.BLACK;
    
    
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
     * Processes the turn. Essentially finds the Life objects
     *  within the Cells, and runs its processTurn() method.
     */
    public void takeTurn() {
      
        //boolean showFood = Boolean.parseBoolean(Settings.get.getProperty("showfood"));
        //System.out.println("\nSHOWFOOD: "+showFood+"\n");
      
        ArrayList<Life> lives = new ArrayList<Life>();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
              
                if (cells[i][j] != null && cells[i][j].getLife() != null) {
                    lives.add(cells[i][j].getLife());
                }
            }
        }
        
        // Do this separately so we don't reprocess tiles.
        for (Life occupier: lives) {
            if (occupier != null) {
                occupier.processTurn();
            }
        }
        
        // Update text on each cell. Only for square cells... HexCells are handled differently.
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
              
                if (cells[i][j] != null && (cells[i][j] instanceof SquareCell)) {
                    ((SquareCell)cells[i][j]).setText();
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
        cells[row][column].setLife(Creator.createLife(cells[row][column], randomSeed));

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
