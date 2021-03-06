package ca.bcit.comp2526.a2b;

import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;

import javax.swing.JPanel;


/**
 * A Polygon Panel. Used for
 *  defining and displaying shapes.
 *  
 *  @author Brayden Traas
 *  @version 1
 * 
 */
@SuppressWarnings("serial")
public abstract class PolygonPanel<CellT> extends JPanel {
    
    private static final double REFERENCE_CELL_SIZE = 25;
    
    private static final Polygon ARROW_TEMPLATE = new Polygon(
        new int[] { 10, 20, 20, 25, 15, 5, 10 },
        new int[] { 0, 0, 10, 10, 20, 10, 10 },
        7);
    
    protected static final double CELL_RADIUS = Settings.getInt("cellRadius");
    
    protected World<? extends Cell> world;
    
    protected final int columns; // Number of columns
    protected final int rows; // Number of rows
    
    protected int offset = 0;
    protected int apothem = 2;  // Apothem of the polygon = radius of inscribed circumference
    protected int rectWidth;    // Width of the inner rectangle
    protected int rectHeight;   // Height of the inner rectangle
    protected int gridWidth;    // offset + side (b + s)
    
    private boolean painting = false;

    /**
     * Creates a PolygonPanel within a World.
     * @param world that this PolygonPanel pertains to.
     */
    protected PolygonPanel(final World<? extends Cell> world) {
        this.world = world;
        
        this.columns = world.getColumnCount();
        this.rows = world.getRowCount();
    }
    
    protected abstract Polygon buildPolygon(final Cell cell);
    
    protected abstract Point tileToPixel(int column, int row);
    
    @Override
    public abstract Dimension getPreferredSize();
    
    private static final Polygon getArrow(int posX, int posY, double scale) {
        
        int[] x2Points = new int[ARROW_TEMPLATE.npoints];
        int[] y2Points = new int[ARROW_TEMPLATE.npoints];
        
        for (int i = 0; i < ARROW_TEMPLATE.npoints; i++) {
            x2Points[i] = (int)Math.ceil(ARROW_TEMPLATE.xpoints[i] * scale);
            y2Points[i] = (int)Math.ceil(ARROW_TEMPLATE.ypoints[i] * scale);
            // System.out.println("point "+x2Points[i]+","+y2Points[i]);
        }
        
        //System.out.println(xPoints);
        
        Polygon arrow = new Polygon(
            x2Points, 
            y2Points, 
            ARROW_TEMPLATE.npoints);
        
        arrow.translate(posX, posY);
        
        
        return arrow;
    }
    
    /*
     * Ensure it's of the parameterized type
     */
    private final double angle(final Cell selfOrig, final Cell otherOrig) {
        
        final Cell self = (Cell)selfOrig;
        final Cell other = (Cell)otherOrig;
        
        if (self == null || other == null
            || !other.getClass().equals(self.getClass())
            || other.getWorld() != self.getWorld()
            || other.getLocation() == null
            || self.getLocation() == null) {
            return 0;
        }
        
        
        Point selfPixel = tileToPixel(self.getLocation().x, self.getLocation().y);
        Point targetPixel = tileToPixel(other.getLocation().x, other.getLocation().y);
    
    
        double angle = Math.toDegrees(
                Math.atan2(targetPixel.y - selfPixel.y, targetPixel.x - selfPixel.x));
    
        angle -= 90;
        
        if (angle < 0) {
            angle += 360;
        }
    
        return angle;
    }
    
    @Override
    protected void paintComponent(final Graphics graphics) {
        super.paintComponent(graphics);
        
        if (painting) {
            return;
        }
        painting = true;
        
        try {
        
            // Setup
            Graphics2D g2d = (Graphics2D) graphics;
            g2d.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING, 
                    RenderingHints.VALUE_ANTIALIAS_ON);
            
            // For each Cell
            for (Cell cell : world.getCells()) {
        
                Polygon poly = buildPolygon(cell);
            
                // Fill shape
                graphics.setColor(cell.getEmptyColor());
                graphics.fillPolygon(poly);
                
                Point location = tileToPixel(cell.location.x, cell.location.y);
                
                int posX = location.x + (apothem / 2);
                int posY = location.y + (apothem / 2); 
                
                // Difficult Terrain
                if (cell.getTerrain() != null) {
                    graphics.setClip(poly);
                    cell.getTerrain().drawImage(
                            graphics, location.x, location.y, 
                            (int)CELL_RADIUS * 2, (int)CELL_RADIUS * 2);
                    graphics.setClip(null);
                }
    
                
                // Draw shape, if applicable
                if (World.VISIBLE_LINES) {
                    g2d.setStroke(new BasicStroke((int)(apothem / 4 + 1), 
                        BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER));
                    g2d.setColor(Cell.BORDER_COLOR);
                    g2d.drawPolygon(poly);
                }
    
                            
                
                // Draw each life in this Cell.
                for (Life life : cell.getLives()) {
                    if (life != null) {
                        g2d.setColor(life.getColor());
                        
                        Cell previous = life.getPreviousCell();
                        
                        // If moved since the last turn
                        if (previous != null && !previous.equals(life.getCell())) {
                            
                            // Find the angle between the last Cell and this one
                            double angle = Math.toRadians(angle(previous, life.getCell()));
                            
                            // Find the scale as per the settings.properties cell size
                            double scale = CELL_RADIUS / REFERENCE_CELL_SIZE; 
                            
                            AffineTransform transform = new AffineTransform();
                            
                            int centerX = posX + (int)(CELL_RADIUS / 2);
                            int centerY = posY + (int)(CELL_RADIUS / 2);
                            
                            // Rotate the graphics according to the moved angle
                            transform.rotate(angle, centerX, centerY);
                            
                            AffineTransform scaleTransform = new AffineTransform();

                            Graphics2D g2d2 = (Graphics2D) g2d.create();
                            
                            // Scale the graphics as per the set cell size
                            g2d2.setTransform(scaleTransform);
                            
                            // Fill the graphics with an Arrow
                            g2d2.fill(
                                    transform.createTransformedShape(getArrow(posX, posY, scale)));
                            
                            //g2d2.rotate(angle);
                            //g2d2.fillPolygon(getArrow(posX, posY));
                            
                            g2d2.dispose();

                        } else {
                            // Draw a circle
                            g2d.fillOval(
                                    posX, 
                                    posY, 
                                    apothem, apothem);
                        }
                    }
                }
                
                graphics.setColor(Cell.TEXT_COLOR);
                graphics.drawString(cell.getText(), 
                        location.x + offset, (location.y + (rectHeight / 2)));
                
                
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            painting = false;
            paintComponent(graphics);
        } finally {
            painting = false;
        }
    }
    

}
