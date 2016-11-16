package ca.bcit.comp2526.a2b;


import javafx.util.Pair;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Stroke;

/**
 * <p>A Hexagon object is-a Polygon.</p>
 * 
 * <p>http://stackoverflow.com/questions/20734438/
 *  algorithm-to-generate-a-hexagonal-grid-with-coordinate-system</p>
 * 
 * @author Mr. Polywhirl.
 * @version 1
 * 
 */
public class Hexagon extends Polygon {

    /**
     * Number of sides on this type of Polygon.
     */
    public static final int SIDES = 6;
    
    private static final long serialVersionUID = 1L;
    private static final int HALF_CIRCLE = 180;
    private static final int FULL_CIRCLE = 360;
    private static final double DOUBLE_PI = Math.PI * 2;
    
    private Point[] points = new Point[SIDES];
    protected Point center = new Point(0, 0);
    private int radius;
    private int rotation = 90;
    
    private Graphics2D graphics;
    private int valueX;
    private int valueY;
    private int thickness;
    
    /**
     * Instantiates a Hexagon with a Point object.
     * @param center - Point object to position this Hexagon.
     * @param radius - size of this Hexagon
     */
    public Hexagon(final Point center, int radius) {
        npoints = SIDES;
        xpoints = new int[SIDES];
        ypoints = new int[SIDES];
  
        this.center = center;
        this.radius = radius;
  
        updatePoints();
    }
  
    /**
     * Instantiates a Hexagon with x & y values
     * @param valueX - position X of this Hexagon
     * @param valueY - position Y of this Hexagon
     * @param radius - size of this Hexagon.
     */
    public Hexagon(int valueX, int valueY, int radius) {
        this(new Point(valueX, valueY), radius);
    }
  
    // Gets the radius.
    protected int getRadius() {
    	return radius;
    }

  
    /*
     * Gets the angle when passed a fraction.
     *  Used internally.
     *  
     * @param fraction (double)
     * @return angle (double)
     */
    private double findAngle(double fraction) {
        return fraction * DOUBLE_PI 
            + Math.toRadians((rotation + HALF_CIRCLE) % FULL_CIRCLE);
    }
  
    /*
     * Gets the Point of this Hexagon at a given angle.
     * 
     * @param angle (double) to find the point at.
     * @return Point that was found.
     */
    private Point findPoint(double angle) {
        int valueX = (int) (center.x + Math.cos(angle) * radius);
        int valueY = (int) (center.y + Math.sin(angle) * radius);
  
        return new Point(valueX, valueY);
    }
  
    /**
     * Update the points when the Hexagon is modified.
     */
    private void updatePoints() {
        for (int p = 0; p < SIDES; p++) {
            double angle = findAngle((double) p / SIDES);
            Point point = findPoint(angle);
            xpoints[p] = point.x;
            ypoints[p] = point.y;
            points[p] = point;
        }
    }
  
    /**
     * Draws this Hexagon on the given Graphics object.
     * 
     * @param graphics - Graphics2D object to draw on.
     * @param valueX - location to place (X).
     * @param valueY - location to place (Y).
     * @param lineThickness - stroke thickness of the line.
     * @param colorValue - Color to paint this Hexagon.
     * @param filled - Fill in or leave an outline only?
     */
    public void draw(final Graphics2D graphics, int valueX, int valueY, 
        int lineThickness, final Color colorValue, boolean filled) {
        
        if (filled) {
            this.graphics = graphics;
            this.valueX = valueX;
            this.valueY = valueY;
            thickness = lineThickness;
        }
      
        if (graphics == null) {
            return;
        }
  
        
        graphics.setColor(colorValue);
        graphics.setStroke(new BasicStroke(lineThickness, 
            BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER));
        
        // Must do this... ugly workaround to pass checkstyles.
        Pair<Color, Stroke> oldGraphics = drawOrFill(graphics, filled);
        
        // Set values to previous when done.
        graphics.setColor(oldGraphics.getKey());
        graphics.setStroke(oldGraphics.getValue());
    }
    
    /*
     * Brayden - Draw with points.
     * 
     * @param graphics - the Graphis2D object to draw on
     * @param filled - boolean (fill or stroke)
     * @return Pair of stroke & Color objects pre-draw
     */
    private Pair<Color, Stroke> drawOrFill(final Graphics2D graphics, boolean filled) {
        
    	if (graphics == null) {
    		return null;
    	}
    	
        Color oldColor = graphics.getColor();
        Stroke oldStroke = graphics.getStroke();
      
        if (xpoints == null || ypoints == null || npoints == 0) {
        	return new Pair<Color, Stroke>(oldColor, oldStroke);
        }
        
       // System.out.println(xpoints.length + " " + ypoints.length + " " + npoints);
        
        if (filled) {
            graphics.fillPolygon(xpoints, ypoints, npoints);
        } else {
            graphics.drawPolygon(xpoints, ypoints, npoints);
        }
        
        return new Pair<Color, Stroke>(oldColor, oldStroke);
        
        
    }
  
    /**
     * Brayden - paint this Hex with a new color.
     * @param newColor - Color to paint with
     * 
     */
    public void paint(final Color newColor) {
    //	System.out.println("Coloring hex " + this + "  with "+newColor);
        draw(graphics, valueX, valueY, thickness, newColor, true);
    }
    
}
