package ca.bcit.comp2526.a2b;

import javax.swing.JFrame;

/**
 * GameFrame.
 * 
 * @author BCIT
 * @version 1.0
 */
@SuppressWarnings("serial")
public class GameFrame extends JFrame {
    
    protected static final String TITLE = "Assignment 2b";
  
    
    private TurnListener turnListener = new TurnListener(this);
    protected final World<? extends Cell> world;

    /**
     * Constructs an object of type GameFrame.
     * @param world a World for this Frame
     */
    public GameFrame(final World<? extends Cell> world) {

        this.world = world;
        setBackground(Cell.EMPTY_COLOR);
    
        addMouseListener(turnListener);
        addKeyListener(turnListener);
    }

    /**
     * Initializes this GameFrame.
     */
    public void init() {
        pack();
    }
    
    
    
    /**
     * Gets the world object.
     * @return the world.
     */
    protected final World<? extends Cell> getWorld() {
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
