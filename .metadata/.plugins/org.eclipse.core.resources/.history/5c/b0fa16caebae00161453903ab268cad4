package ca.bcit.comp2526.a2b;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public interface WaterCell extends DifficultTerrain {
	Color WATER_COLOR = new Color(0, 85, 255);
	Image image = getImage("water.png"); 
		
	static Image getImage(String filename) {
		try {
			return ImageIO.read(new File(filename));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see ca.bcit.comp2526.a2b.Terrain#getEmptyColor()
	 * 
	 * I'd rely on fancy Java 8 default methods,
	 *  but the implementations of WaterCell (HexCell, SquareCell etc)
	 *  inherit Cell's getEmptyColor(), thus overriding this one.
	 */
	@Override
	default Color getEmptyColor() {
		return WATER_COLOR;
	}
	
	@Override
	default void drawImage(Graphics graphics, int offsetX, int offsetY, int sizeX, int sizeY) {
		graphics.drawImage(image, offsetX, offsetY, sizeX, sizeY, null);
	}
	
}
