package ca.bcit.comp2526.a2b;


import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Listens for a new Turn click.
 * 
 * @author Brayden Traas
 * @version 2016-10-22
 */
public final class TurnListener extends MouseAdapter implements MouseListener, KeyListener {

    private static final int TURN_SPEED_MS = Settings.getInt("turnSpeedMS");
    private static boolean RUNNING = false;

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
    @Override
    public void mouseReleased(final MouseEvent event) {
        if (RUNNING) {
            return;
        }
        RUNNING = true;
        worldFrame.takeTurn();
        RUNNING = false;
    }
    
    /**
     * Decides what happens when a key is released.
     */
    @Override
    public void keyReleased(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.VK_SPACE) {
            
            // If already running, stop. Using a variable instead of killing the thread,
            //  so the turn is finished.
            if (RUNNING) {
                RUNNING = false;
            } else {
                RUNNING = true;
                final TurnListener parent = this;
                new Thread(new Runnable() {
                    public void run() {
                        World<?> world = parent.worldFrame.getWorld();
                        while (RUNNING && world.getLives().size() > 0) {
                            parent.worldFrame.takeTurn();
                            try {
                                Thread.sleep(TURN_SPEED_MS);
                            } catch (InterruptedException exception) {
                                exception.printStackTrace();
                            }
                        }
                        RUNNING = false;
                    }
                }).start();
            }
        }
    
    }

    @Override
    public void keyTyped(KeyEvent event) {
    }

    @Override
    public void keyPressed(KeyEvent event) { 
        
    }
    
}
