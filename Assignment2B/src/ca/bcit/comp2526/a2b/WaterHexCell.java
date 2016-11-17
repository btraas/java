package ca.bcit.comp2526.a2b;

import java.awt.Color;

@SuppressWarnings("serial")
public class WaterHexCell extends HexCell implements WaterCell {


	public WaterHexCell(HexCell origin) {
		super(origin.getWorld(), 
				origin.getRow(),
				origin.getColumn(), 
				origin.hex.center.x, 
				origin.hex.center.y, 
				origin.hex.getRadius());

	}

	@Override
	public Color getEmptyColor() {
    	return WATER_COLOR;
    }
    

}
