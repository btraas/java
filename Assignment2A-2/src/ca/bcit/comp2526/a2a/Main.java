package ca.bcit.comp2526.a2a;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.IOException;

import javax.swing.JFrame;

/**
 * Main.
 * 
 * @author Brayden Traas
 * @version 1.0
 */
public final class Main {
    
  
    private static final boolean HEX_GRID; 
    private static final int WORLD_SIZE;
    
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
      
        Settings.load();
      
        TOOLKIT = Toolkit.getDefaultToolkit();
        
        HEX_GRID = Settings.get("gridtype").equalsIgnoreCase("hex");
        WORLD_SIZE = Settings.getInt("worldsize");    
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
        final World world; // You need a World class
        final GameFrame frame;

        // Now loaded at Main Class initialization, before main() method.
        //Settings.load();
        
        world = new World(WORLD_SIZE, WORLD_SIZE);
        world.init();

        // Load the grid type. Either Square or Hex.
        
        if (HEX_GRID) {
            frame = new HexFrame(world);
        } else {
            frame = new GameFrame(world);
        }
        
        position(frame);
        frame.init();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    /*
     * Sets the position of the specified GameFrame on the screen.
     * @param frame the GameFrame
     */
    private static void position(final GameFrame frame) {
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
