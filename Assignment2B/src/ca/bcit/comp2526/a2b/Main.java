package ca.bcit.comp2526.a2b;


import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Main.
 * 
 * @author Brayden Traas
 * @version 1.1
 */
public final class Main {
    
    private static final String WORLD_SIZE_X = "worldColumns";
    private static final String WORLD_SIZE_Y = "worldRows";
    
    private static final String GRID_TYPE = "gridType";
    private static final String HEX_GRID = "hex";

    private static final float SCREENSIZE_MULTIPLIER = 0.8f;
    private static final String NULL_SIZE = "Size cannot be null";
    private static final float MAX_PCT = 100.0f;
    private static final float MIN_PCT = 0.0f;
    private static final String PCT_ERROR = "Percent cannot be <= " + MIN_PCT 
        + " or > " + MAX_PCT + " - got: ";
    private static final String HEIGHT = "height";
    private static final String WIDTH = "width";
    private static final float HALF = 0.5f;
    
    private static final Toolkit TOOLKIT;

    /**
     * This is a static initialization block.  What is the purpose of a
     * static initialization block?
     */
    static {
      
        TOOLKIT = Toolkit.getDefaultToolkit();
        
    }

    /**
     * Private constructor prevents instantiation of Main.
     */
    private Main() {
    
    }

    /**
     * Drives the program.
     * @param argv command line arguments
     */
    public static void main(final String[] argv) throws IOException {
        final Settings settings = new Settings();
    
        position(settings);
        settings.init();
        settings.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        settings.setVisible(true);
    
    }
    
    
    
    
    /**
     * Creates a world.
     */
    public static void createWorld() {
        World<? extends Cell> world;
        final GameFrame frame; 

        // Now loaded at Main Class initialization, before main() method.
        //Settings.load();
        
        
        
        int sizeX = Settings.getInt(WORLD_SIZE_X);
        int sizeY = Settings.getInt(WORLD_SIZE_Y);
        
        // Load the grid type. Either Square or Hex.
        
        JPanel panel;
        String gridType = Settings.get(GRID_TYPE);
        if (gridType.equalsIgnoreCase(HEX_GRID)) {
        
            World<HexCell> tmpWorld = new World<HexCell>(sizeX, sizeY);
            panel = new HexPanel(tmpWorld);
            world = tmpWorld;
        
        } else {
            World<SquareCell> tmpWorld = new World<SquareCell>(sizeX, sizeY);
            panel = new SquarePanel(tmpWorld);
            world = tmpWorld;
        }
        
        
        frame = new GameFrame(world);
        frame.setTitle("Assignment 2B");
        frame.add(panel);
        
        position(frame);
        frame.init();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
    

    /*
     * Sets the position of the specified GameFrame on the screen.
     * @param frame the GameFrame
     */
    private static void position(final JFrame frame) {
        final Dimension size;
        size = calculateScreenArea(SCREENSIZE_MULTIPLIER, SCREENSIZE_MULTIPLIER);
        frame.setSize(size);
        frame.setLocation(centreOnScreen(size));
    }

    /**
     * Returns the centre point of the screen.
     * @param size a Dimension
     * @return a Point that refers to the centre point of the screen.
     */
    public static Point centreOnScreen(final Dimension size) {
        final Dimension screenSize;
        if (size == null) {
            throw new IllegalArgumentException(NULL_SIZE);
        }
        screenSize = TOOLKIT.getScreenSize();
        
        int width = screenSize.width - size.width;
        int height = screenSize.height - size.height;
        return (new Point((int)(width * HALF), (int)(height * HALF)));
    }

    /**
     * Returns correct size of the game given the specified width and height
     * percentages.
     * @param widthPct a float
     * @param heightPct a float
     * @return size as a Dimension
     */
    public static Dimension calculateScreenArea(final float widthPct, final float heightPct) {
        final Dimension screenSize;
        final Dimension area;
        final int width;
        final int height;
        final int size;

        if ((widthPct <= MIN_PCT) || (widthPct > MAX_PCT)) {
            String msg = WIDTH + PCT_ERROR + widthPct;
            throw new IllegalArgumentException(msg);
        }

        if ((heightPct <= MIN_PCT) || (heightPct > MAX_PCT)) {
            String msg = HEIGHT + PCT_ERROR + heightPct;
            throw new IllegalArgumentException(msg);
        }

        screenSize = TOOLKIT.getScreenSize();
        width = (int) (screenSize.width * widthPct);
        height = (int) (screenSize.height * heightPct);
        size = Math.min(width, height);
        area = new Dimension(size, size);

        return (area);
    }
}
