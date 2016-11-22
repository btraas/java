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
public class HexPanel extends PolygonPanel<HexCell> {
    
    public HexPanel(final World<HexCell> world) {
        super(world);
        int hexSide = CELL_RADIUS;
    	apothem = (int) (hexSide * Math.cos(Math.PI / 6));
        offset = (int) (hexSide * Math.sin(Math.PI / 6));
        gridWidth = offset + hexSide;
        rectWidth = 2 * offset + hexSide;
        rectHeight = 2 * apothem;

        System.out.println(columns+","+rows);
        for (int i = 0; i < columns; i++) {
        	System.out.println("adding column "+i+"/"+columns+"-"+world.getColumnCount());
        	for (int j = 0; j < rows; j++) {
        		//System.out.println("attempting "+i+","+j);
        		world.addCell(new HexCell(world, i, j));
        	}
        }
        
    }

    
        
    @Override
    public Dimension getPreferredSize() {
        int panelWidth = columns * gridWidth + offset;
        int panelHeight = rows * rectHeight + apothem + 1;
        return new Dimension(panelWidth, panelHeight);
    }

    @Override
    public Polygon buildPolygon(final Cell cell) {
    	if (!(cell instanceof HexCell)) {
    		return null;
    	}
    	
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

    public boolean tileIsWithinBoard(final Point coordinates) {
        int column = coordinates.x;
        int row = coordinates.y;
        return (column >= 0 && column < columns)
        		&& (row >= 0 && row < rows);
    }

}