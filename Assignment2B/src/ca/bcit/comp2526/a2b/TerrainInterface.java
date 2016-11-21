package ca.bcit.comp2526.a2b;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Terrain Interface. Indicates that this type 
 *  has a background color and background image.
 *  
 *  
 * @author Brayden Traas
 * @version 2
 *
 */
public interface TerrainInterface {

	Color DIRT_COLOR = new Color(163, 117, 73); // brown
	Color EMPTY_COLOR = DIRT_COLOR;
	
	Image image = getImage("dirt.png"); 
	
	static Image getImage(String filename) {
		try {
			return ImageIO.read(new File(filename));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	static Color getEmptyColor() {
		return EMPTY_COLOR;
	}
	
	default void drawImage(Graphics graphics, int offsetX, int offsetY, int sizeX, int sizeY) {
		
		graphics.drawImage(image, offsetX, offsetY, sizeX, sizeY, null);
		
	}
	
}
