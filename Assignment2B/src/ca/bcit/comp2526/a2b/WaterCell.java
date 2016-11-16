package ca.bcit.comp2526.a2b;

import java.awt.Color;

// Empty for instanceof checks.
public interface WaterCell extends Terrain {
	Color WATER_COLOR = new Color(0, 55, 255);
	
	Color getEmptyColor();
}
