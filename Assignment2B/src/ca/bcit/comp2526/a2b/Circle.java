package ca.bcit.comp2526.a2b;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;

import javax.swing.JPanel;


@SuppressWarnings("serial")
public class Circle extends JPanel {

	private Dimension dimension;
	private Color color;
	
	protected Point position = new Point(0, 0);
	
	public Circle(int size, Color color) {
		this.dimension = new Dimension(size, size);
		this.color = color;
		this.setBackground(color);
	}
	
	public Circle(int size, Color color, int offsetX, int offsetY) {
		this(size, color);
		this.position.x = offsetX;
		this.position.y = offsetY;
	}


	@Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g2d = (Graphics2D) graphics;
        g2d.setRenderingHint(
        		RenderingHints.KEY_ANTIALIASING, 
        		RenderingHints.VALUE_ANTIALIAS_ON);
        draw(g2d);

    }

    @Override
    public Dimension getPreferredSize() {
        return dimension;
    }
    
    protected void draw(Graphics2D g2d) {
    	// System.out.println("drawing circle here...");
        g2d.setColor(color);
        g2d.fillOval(position.x, position.y, dimension.width, dimension.height);
    }
    
    
    protected void paint(Color newColor) {
    	//System.out.println("painting circle to "+newColor);
    	this.color = newColor;
    	repaint();
    }

}
