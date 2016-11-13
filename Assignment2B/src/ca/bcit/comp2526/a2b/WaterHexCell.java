package ca.bcit.comp2526.a2b;

import java.awt.Color;

@SuppressWarnings("serial")
public class WaterHexCell extends HexCell implements WaterCell {


	public WaterHexCell(HexCell origin) {
		super(origin.getWorld(), 
				origin.getRow(),
				origin.getColumn(), 
				origin.center.x, 
				origin.center.y, 
				origin.getRadius());

		emptyColor = Color.BLUE;

	}

}
