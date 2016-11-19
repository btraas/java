package ca.bcit.comp2526.a2b;

import java.awt.Color;

public class WaterSquareCell extends SquareCell implements WaterCell {

	
	public WaterSquareCell(SquareCell origin) {
		super(origin.getWorld(), origin.getRow(), origin.getColumn());
	}

	public Color getEmptyColor() {
		return super.getEmptyColor();
	}
}
