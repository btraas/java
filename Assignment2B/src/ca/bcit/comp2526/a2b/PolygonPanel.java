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
public abstract class PolygonPanel extends JPanel {

	private static final Polygon ARROW_TEMPLATE = new Polygon(
			new int[] { 10, 20, 20, 25, 15, 5, 10 },
			new int[] { 0, 0, 10, 10, 20, 10, 10 },
			7);
	
	protected static final int CELL_RADIUS = Settings.getInt("cellRadius");

    protected World world;
	
	protected int width; // Number of columns
    protected int height; // Number of rows
	
    protected int offset = 0;
    protected int apothem = 2; 		// Apothem of the polygon = radius of inscribed circumference
    protected int rectWidth; // Width of the inner rectangle
    protected int rectHeight; // Height of the inner rectangle
    protected int gridWidth;  // offset + side (b + s)
    
    private boolean painting = false;
    
    
	public PolygonPanel(final World world) {
		this.world = world;
    	
    	this.width = world.getColumnCount();
    	this.height = world.getRowCount();
	}

	protected abstract Polygon buildPolygon(final Cell cell);
	protected abstract Point tileToPixel(int column, int row);
	
	@Override
	public abstract Dimension getPreferredSize();
	
	private static final Polygon getArrow(int posX, int posY) {
	
		Polygon arrow = new Polygon(
				ARROW_TEMPLATE.xpoints, 
				ARROW_TEMPLATE.ypoints, 
				ARROW_TEMPLATE.npoints);
		
		arrow.translate(posX, posY);
		
		return arrow;
	}
	
    private final double angle(final Cell self, final Cell other) {
    	
    	if (self == null || other == null
    		|| !other.getShape().equals(self.getShape())
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
        
        if(angle < 0){
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
	            			CELL_RADIUS*2, CELL_RADIUS*2);
	            	graphics.setClip(null);
	            }
	
	            
	            // Draw shape, if applicable
	            if (World.VISIBLE_LINES) {
	            	g2d.setStroke(new BasicStroke((int)(apothem/4 + 1), 
	                    BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER));
	            	g2d.setColor(Cell.BORDER_COLOR);
	            	g2d.drawPolygon(poly);
	            }
	
	            
	            
	
	        	
	            
	            
	            // Draw each life in this Cell.
	            for (Life life : cell.getLives()) {
	            	if (life != null) {
	            		g2d.setColor(life.getColor());
	            		
	            		Cell previous = life.getPreviousCell();
	            		
	            		if (previous != null && !previous.equals(life.getCell())) {
	            			double angle = Math.toRadians(angle(previous, life.getCell()));

	            			AffineTransform transform = new AffineTransform();
	            			
	            			int centerX = posX + (CELL_RADIUS / 2);
	            			int centerY = posY + (CELL_RADIUS / 2);
	            			transform.rotate(angle, centerX, centerY);

	            			
	            			
	            			Graphics2D g2d2 = (Graphics2D) g2d.create();
	            			
	            			g2d2.fill(transform.createTransformedShape(getArrow(posX, posY)));
	            			
	            			//g2d2.rotate(angle);
	            			//g2d2.fillPolygon(getArrow(posX, posY));
	            			
	            			g2d2.dispose();
	            			
	            		} else {
	            			g2d.fillOval(
	            				posX, 
	            				posY, 
	            				apothem, apothem);
	            		}
	            	}
	            }
	            
	        	graphics.setColor(Cell.TEXT_COLOR);
	            graphics.drawString(cell.getText(), location.x + offset, (location.y + rectHeight));
	            
	            
	        }
        } catch(Exception e) {
        	e.printStackTrace();
        	painting = false;
        	paintComponent(graphics);
        } finally {
        	painting = false;
        }
    }

	
}