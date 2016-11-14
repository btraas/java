package ca.bcit.comp2526.a2b;


import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Listens for a new Turn click.
 * 
 * @author Brayden Traas
 * @version 2016-10-22
 */
public final class TurnListener extends MouseAdapter implements MouseListener {

    private GameFrame worldFrame;
  
    /**
     * Creates a TurnListener for this frame.
     * 
     * @param frame to add a listener for.
     */
    public TurnListener(final GameFrame frame) {
        worldFrame = frame;
    }
  
    /**
     * Decides what happens when a mouse click is released.
     */
    public void mouseReleased(final MouseEvent event) {
        worldFrame.takeTurn();
    }
    
}
