package ca.bcit.comp2526.a2b;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Polygon;

/**
 * Square Map.
 * Modified version from Magomar @ Github.com
 * 
 * 
 * @author Brayden Traas
 * @version 1
 *
 */
@SuppressWarnings("serial")
public final class SquarePanel extends PolygonPanel<SquareCell> {
    
	/**
	 * Creates a Panel for the given world.
	 * Ensures the passed world's Cell type is
	 * "SquareCell"
	 * @param world to display
	 */
    public SquarePanel(final World<SquareCell> world) {
        
    	super(world);
    	int side = CELL_RADIUS;

       // this.hexSide = hexSide;
        apothem = (int) (side / 2);
        offset = (int) 0;
        gridWidth = offset + side;
        rectWidth = 2 * offset + side;
        rectHeight = 2 * apothem;

        for (int i = 0; i < width; i++) {
        	for (int j = 0; j < height; j++) {
        		world.addCell(new SquareCell(world, i, j));
        	}
        }
    }
    
    
    
    @Override
    public Dimension getPreferredSize() {
        int panelWidth = width * gridWidth;
        int panelHeight = height * rectHeight + apothem + 1;
        return new Dimension(panelWidth, panelHeight);
    }

    @Override
    public Polygon buildPolygon(final Cell cell) {
    	
    	if (!(cell instanceof SquareCell)) {
    		return null;
    	}
    	
        Polygon square = new Polygon();
        Point origin = tileToPixel(cell.location.x, cell.location.y);
        square.addPoint(origin.x, origin.y);
        square.addPoint(origin.x + gridWidth, origin.y);
        //square.addPoint(origin.x + hexRectWidth, origin.y + hexApotheme);
        square.addPoint(origin.x + gridWidth, origin.y + rectHeight);
        square.addPoint(origin.x, origin.y + rectHeight);
        //square.addPoint(origin.x, origin.y + hexApotheme);
        return square;
    }
	 
    @Override
    protected Point tileToPixel(int column, int row) {
        Point pixel = new Point();
        pixel.x = gridWidth * column;
        pixel.y = rectHeight * row;

        return pixel;
    }



}