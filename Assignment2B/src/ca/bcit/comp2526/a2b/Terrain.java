package ca.bcit.comp2526.a2b;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

/**
 * A Terrain Type for Cells to have.
 * 
 * @author Brayden Traas
 * @version 2016-11-19
 */

public enum Terrain implements Edible {

    
    DIRT(   "dirt",     new Color(163, 117, 73),  getImage("dirt.png"),   false ),
    WATER(  "water",    new Color(0, 85, 255),    getImage("water.png"),  true ),
    
    DEFAULT( Terrain.DIRT ); // Default / Dirt 

    private final String identifier; // The String that represents this Enum.
    private final Color  color;
    private final Image  image;
    private boolean difficult;
    
    
    public Color getColor() {
        return color;
    }

    private Terrain(final Terrain in) {
        this(in.identifier, in.color, in.image, in.difficult);
    }
    
    // Providing a constructor for this Enum. Terrain.Water runs this constructor
    // (Terrain('water')) which sets the character identifier.
    private Terrain(final String identifier, 
            final Color color, final Image image, boolean difficult) {
        this.identifier = identifier;
        this.color = color;
        this.image = image;
        this.difficult = difficult;
    }

    /**
     * Decide if this Terrain is difficult or not.
     * @return true if difficult.
     */
    public boolean isDifficult() {
        return this.difficult;
    }
    
    /**
     * Get the "difficult" terrain enums.
     * @return an array of difficult terrains.
     */
    public static Terrain[] getDifficults() {
        ArrayList<Terrain> terrains = new ArrayList<Terrain>();
        Terrain[] values = Terrain.values();
        for (int i = 0; i < values.length; i++) {
            if (values[i].isDifficult()) {
                terrains.add(values[i]);
            }
        }
        Terrain[] difficults = new Terrain[terrains.size()];
        int iterator = 0;
        for (Terrain terrain : terrains) {
            difficults[iterator++] = terrain;
        }
        return difficults;
    }

    /**
     * Get the Enum from a string identifier.
     * 
     * @param input String to get the Enum for
     * @return Enum pertaining to string identifier
     */
    public static Terrain fromString(final String input) {
        for (Terrain tempTable : Terrain.values()) {
            if (input.equalsIgnoreCase(tempTable.identifier)) {
                return tempTable;
            }
        }
        return null;
    }

    
    private static Image getImage(final String filename) {
        try {
            return ImageIO.read(new File(filename));
        } catch (IOException exception) {
            exception.printStackTrace();
            return null;
        }
    }
        
    /**
     * Draw this Terrain's image on the given graphics.
     * 
     * @param graphics to draw on
     * @param offsetX to offset
     * @param offsetY to offset
     * @param sizeX of the image
     * @param sizeY of the image
     */
    public void drawImage(final Graphics graphics, int offsetX, int offsetY, int sizeX, int sizeY) {
        
        graphics.drawImage(image, offsetX, offsetY, sizeX, sizeY, null);
        
    }
    


}
