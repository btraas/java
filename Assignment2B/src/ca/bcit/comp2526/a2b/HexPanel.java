package ca.bcit.comp2526.a2b;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Polygon;


/**
 * Hexagonal Map.
 * Modified version from Magomar @ Github.com
 * 
 * 
 * @author Brayden Traas
 * @version 1
 *
 */
@SuppressWarnings("serial")
public class HexPanel extends PolygonPanel {
    

    //protected int hexOffset; // Distance from left horizontal vertex to vertical axis
    // protected int hexApotheme; // Apotheme of the hexagon = radius of inscribed circumference
    //protected int hexRectWidth; // Width of the circumscribed rectangle
    //protected int hexRectHeight; // Height of the circumscribed rectangle
    //protected int hexGridWidth;  // hexOffset + hexSide (b + s)
  
    public HexPanel(World world) {
        super(world);
        int hexSide = CELL_RADIUS;
    	apothem = (int) (hexSide * Math.cos(Math.PI / 6));
        offset = (int) (hexSide * Math.sin(Math.PI / 6));
        gridWidth = offset + hexSide;
        rectWidth = 2 * offset + hexSide;
        rectHeight = 2 * apothem;

        this.width = world.getColumnCount();
    	this.height = world.getRowCount();
        
        
        for (int i = 0; i < width; i++) {
        	for (int j = 0; j < height; j++) {
        		world.createCellAt(new HexCell(world, i, j), i, j);
        	}
        }
        
    }

    
        
    @Override
    public Dimension getPreferredSize() {
        int panelWidth = width * gridWidth + offset;
        int panelHeight = height * rectHeight + apothem + 1;
        return new Dimension(panelWidth, panelHeight);
    }

    
    public Polygon buildPolygon(Cell cell) {
        Polygon hex = new Polygon();
        Point origin = tileToPixel(cell.location.x, cell.location.y);
        hex.addPoint(origin.x + offset, origin.y);
        hex.addPoint(origin.x + gridWidth, origin.y);
        hex.addPoint(origin.x + rectWidth, origin.y + apothem);
        hex.addPoint(origin.x + gridWidth, origin.y + rectHeight);
        hex.addPoint(origin.x + offset, origin.y + rectHeight);
        hex.addPoint(origin.x, origin.y + apothem);
        return hex;
    }
	 

    protected Point tileToPixel(int column, int row) {
        Point pixel = new Point();
        pixel.x = gridWidth * column;
        if (Util.isEven(column)) {
        	pixel.y = rectHeight * row;
        } else {
        	pixel.y = rectHeight * row + apothem;
        }
        return pixel;
    }

    public boolean tileIsWithinBoard(Point coordinates) {
        int column = coordinates.x;
        int row = coordinates.y;
        return (column >= 0 && column < width) && (row >= 0 && row < height);
    }

}