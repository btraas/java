package ca.bcit.comp2526.a2a;

import java.awt.GridLayout;

import javax.swing.JFrame;

/**
 * GameFrame.
 * 
 * @author BCIT
 * @version 1.0
 */
@SuppressWarnings("serial")
public class GameFrame extends JFrame {
    
    private static final String TITLE = "Assignment 2a - Plain Square Cells";
  
    protected final World world;

    /**
     * Constructs an object of type GameFrame.
     * @param world a World for this Frame
     */
    public GameFrame(final World world) {
        this.world = world;
    }

    /**
     * Initializes this GameFrame.
     */
    public void init() {
        setTitle(TITLE);
        
        setLayout(new GridLayout(world.getRowCount(), world.getColumnCount()));

        for (int row = 0; row < world.getRowCount(); row++) {
            for (int col = 0; col < world.getColumnCount(); col++) {
                add((SquareCell)world.createCellAt(new SquareCell(world, row, col), row, col));
            }
        }
        addMouseListener(new TurnListener(this));
    }

    /**
     * Gets the world object.
     * @return the world.
     */
    protected final World getWorld() {
        return world;
    }
    
    /**
     * Takes a turn and repaints the world.
     */
    public final void takeTurn() {
        world.takeTurn();
        repaint();
    }
    

}
