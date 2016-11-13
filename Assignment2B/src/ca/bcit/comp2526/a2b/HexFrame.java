package ca.bcit.comp2526.a2b;


import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

import javax.swing.JPanel;


/**
 * <p>A Hex Frame.
 * Used for a hex grid (opposed to a square grid).</p>
 * 
 * <p>A HexFrame is-a GameFrame with a few changes.
 *  -New init() method creates a GUI of Hexes instead of Squares.
 *      -Hex-shaped Cells, aka HexCells instead of SquareCells.</p>
 * 
 * @author Brayden Traas
 * @version 2016-11-02
 */
@SuppressWarnings("serial")
public class HexFrame extends GameFrame {
 
    private static final String TITLE = "Assignment 2b - HexFrame";
    
   // private static final int PANEL_WIDTH = 1100;
    private static final int WORLD_SIZE_X = Settings.getInt("WorldSizeX");
    private static final int WORLD_SIZE_Y = Settings.getInt("WorldSizeY");
    private static final Font DEFAULT_FONT = new Font("Verdana", Font.PLAIN, 10);
    
    private static final double POINTX_MULTIPLIER = 1;//6.25;
    private static final double POINTY_MULTIPLIER = 1;//5.8;
    
    private static final double POINTX_OFFSET = 270;
    private static final double POINTY_OFFSET = 250;
    
    private static final float STROKE_WIDTH = 2.0f;
    private static final double ANGLE_30 = Math.toRadians(30);
    
    private static final float HALF = 0.5f;
//    private static final float DOUBLE = 2.0f;
    private static final float TRIPLE = 3.0f;
    
    private static final double EXPECTED_MAX = 25.0;
    private int hexRadius = 12;
    
    public HexFrame(final World world) {
        super(world);
    }

    
    /**
     * Creates a gameframe with hex tiles instead of square.
     */
    public void init() {
        setTitle(TITLE);
        
        int max = Math.max(WORLD_SIZE_X, WORLD_SIZE_Y);
        double multiplier = (EXPECTED_MAX/max);
        hexRadius = (int)(hexRadius * multiplier) + 1;
        System.out.println("mult: "+multiplier+" hex:"+hexRadius);
        
        // HexPanel is an inner class below.
        add(new HexPanel(world, WORLD_SIZE_X, WORLD_SIZE_Y));
        addMouseListener(new TurnListener(this));
        addKeyListener(new TurnListener(this));
    }
    
    /**
     * Inner class HexPanel - Separating from outer Frame class.
     *  Some code is from Stackoverflow - see Hexagon.java
     *  
     * @author Brayden Traas
     * @version 2016-11-02
     *
     */
    private final class HexPanel extends JPanel {
        
        private static final long serialVersionUID = -383945404001367101L;

        private Font font = DEFAULT_FONT;
        private FontMetrics metrics;
        
        private World world;
        private int sizeX;
        private int sizeY;

        /**
         * Instantiates a HexPanel.
         * 
         * @param world this HexPanel exists to.
         * @param size of the World / HexPanel.
         * @param width of the panel.
         */
        private HexPanel(final World world, int sizeX, int sizeY) {
            this.world = world;
            this.sizeX = sizeX;
            this.sizeY = sizeY;
        
        }
      
        /**
         * Override the paintComponent method. Modified version of the example
         *  provided on Stackoverflow - see Hexagon.java
         *  
         * @param graphics - Graphics object
         */
        @Override
        public void paintComponent(final Graphics graphics) {
          
            Graphics2D g2d = (Graphics2D) graphics;
     
            g2d.setStroke(new BasicStroke(STROKE_WIDTH, 
                    BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER));
            g2d.setFont(font);
            metrics = graphics.getFontMetrics();

            int point1 = (int)(sizeX * POINTX_MULTIPLIER + POINTX_OFFSET);
            int point2 = (int)(sizeY * POINTY_MULTIPLIER + POINTY_OFFSET);
            
            //Point origin = new Point(width/2, width/2);
            Point origin = new Point(point1, point2);
            //Point origin = new Point(width/2, width/2);
            
            //Point origin = new Point((int)(0.4*width), (int)(0.4*width));
            
            //Point origin = new Point((int)(11.2*size+5), (int)(9.8*size+15));
            
            drawGrid(g2d, origin, sizeX, sizeY, hexRadius);
        }
        
        /*
         * Draw the hexagon grid. Modified version of the example
         *  provided on Stackoverflow - see Hexagon.java
         * 
         * @param graphics - Graphics object to draw on.
         * @param origin - (Point) origin to draw from.
         * @param size - (int) number of cells in each direction.
         * @param radius - (int) set the size of each hexagon.
         */
        private void drawGrid(final Graphics graphics, final Point origin, int sizeX, int sizeY, int radius) {
          
          
            double offX = Math.cos(ANGLE_30) * radius;
            double offY = Math.sin(ANGLE_30) * radius;
            int half = (int)(sizeY * HALF);
      
            for (int row = 0; row < sizeY; row++) {

                // Check if even without using magic number 2
                //  If not even, use one less column for this row.
                int cols = ((row & 1) == 0) ? sizeX : sizeX - 1;
                for (int col = 0; col < cols; col++) {
                    
                
                    int valueX = (int) (origin.x + offX * (col * 2 + 1 - cols));
                    int valueY = (int) (origin.y + offY * (row - half) * TRIPLE);
      
                    drawHex(graphics, col, row, valueX, valueY, radius);
                }
            }
        }
        
        /*
         * Draw a single hexagon. Modified version of the example
         *  provided on Stackoverflow - see Hexagon.java
         * 
         * @param graphics - Graphics object to draw on.
         * @param row - Where to add this on the World object.
         * @param col - Where to add this on the World object.
         * @param valueX - Physical x position on the grid.
         * @param valueY - Physical y position on the grid.
         * @param r - radius (int) of the hexagon.
         */
        private void drawHex(final Graphics graphics, int row, int col, 
            int valueX, int valueY, int radius) {
            Graphics2D graphics2d = (Graphics2D) graphics;
       
            // Get the HexCell here (if it exists).
            HexCell initHex = (HexCell)world.getCellAt(row, col);
            
            // Create a Cell here if it doesn't exist.
            if (initHex == null) {
                HexCell newHex = (HexCell) Creator.createCell(
                		new HexCell(world, row, col, valueX, valueY, radius), 
                		world.getSeed());
                initHex = (HexCell)world.createCellAt(newHex, row, col);
            }
           
            // System.out.println(graphics2d + " " + valueX + " " + valueY + " " + 0 + " " + initHex.emptyColor + " " + true);
            
            // Draw Hexagon inner shape.
            initHex.draw(graphics2d, valueX, valueY, 0, initHex.emptyColor, true);
            
            // Draw Hexagon outline.
            if (World.VISIBLE_LINES) {
                initHex.draw(graphics2d, valueX, valueY, (int)STROKE_WIDTH, 
                    Cell.BORDER_COLOR, false);
            }
        
            // Paint with the color if its life.
            initHex.paint();
            
            String text = initHex.getText();
            
            int width = metrics.stringWidth(text);
            int height = metrics.getHeight();
            
            graphics.setColor(Cell.TEXT_COLOR);
            graphics.drawString(text, valueX - (int)( width * HALF ), 
                                valueY + (int)( height * HALF ) );
        }
      
      
    }
    
    
    
}
