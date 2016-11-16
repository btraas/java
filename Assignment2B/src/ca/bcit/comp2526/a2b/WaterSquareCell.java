package ca.bcit.comp2526.a2b;

import java.awt.Color;

@SuppressWarnings("serial")
public class WaterSquareCell extends SquareCell implements WaterCell {

	
	public WaterSquareCell(SquareCell origin) {
		super(origin.getWorld(), origin.getRow(), origin.getColumn());
	}

	@Override
	public Color getEmptyColor() {
    	return WATER_COLOR;
    }
	
}
