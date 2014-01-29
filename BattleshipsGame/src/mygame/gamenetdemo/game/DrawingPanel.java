package ca.gamenetdemo.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.List;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.JPanel;

import mygame.networking.util.Misc;

/**
 * A JPanel which upon mouse click draws a circle of
 * a random colour and notifies all DrawingPanelListeners
 * of the new circle drawn.
 * @author Ivaylo Parvanov
 *
 */
public class DrawingPanel extends JPanel implements MouseListener, MouseMotionListener {

	class Circle {
		/* Data used to paint a circle at a given position with given radius and colour. */
		float x, y, radius;
		Color col;
		
		Circle(float x, float y, float radius, Color color) {
			this.x = x;
			this.y = y;
			this.radius = radius;
			this.col = color;
		}
	}

	private static final int MAX_RADIUS = 60;

	private static final int MIN_RADIUS = 10;

	/** Limits the total number of circles allowed to be drawn at the same time. */
	private static final int CIRCLES_LIMIT = 600;

	private CopyOnWriteArrayList<Circle> circlesToDraw;
	private ArrayList<DrawingPanelListener> listeners;
	
	private boolean clearPanel = false;
	
	public DrawingPanel() {
		circlesToDraw = new CopyOnWriteArrayList<Circle>();
		listeners = new ArrayList<DrawingPanelListener>(1);
		this.setBackground(Color.white);
		this.setVisible(true);
		
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	
	/**
	 * Draws the specified circle.
	 * @param x
	 * @param y
	 * @param radius
	 * @param color
	 */
	public void drawCircle(float x, float y, float radius, Color color) {
		circlesToDraw.add(new Circle(x, y, radius, color));
		repaint();
	}
	
	/**
	 * Draws a circle of random radius and colour 
	 * (within the max and min radius) at the specified
	 * coordinates.
	 * @param x
	 * @param y
	 */
	public Circle drawRandomCircle(float x, float y) {
		//generate new random circle at the coordinates
		Color randomColor = new Color((float)Math.random(),(float) Math.random(), (float) Math.random());
		
		Circle c = new Circle(x, y, (float)Math.random() * (MAX_RADIUS - MIN_RADIUS) + MIN_RADIUS, randomColor);
		//add the circle to the drawing list
		circlesToDraw.add(c);

		//force to refresh the whole panel
		repaint();
		return c;
	}

	public void addDrawingPanelListener(DrawingPanelListener l) {
		listeners.add(l);
	}
	
	
	public void clear() {
		circlesToDraw.clear();
	}
	
	/**
	 * Removes the oldest circles until the size limit is ensured.
	 */
	private void limitNumberOfCircles() {
		while(circlesToDraw.size() >= CIRCLES_LIMIT) {
			circlesToDraw.remove(0);
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		//make sure noone is changing the list of circles when drawing them
		synchronized(this) {
			for(Circle c: circlesToDraw) {
				//draw every circle which has not yet been drawn
				g.setColor(c.col);
				g.fillOval((int) (c.x - c.radius),(int) (c.y - c.radius),(int) (2 * c.radius),(int) (2 * c.radius));
			}
			limitNumberOfCircles();
		}
	}
	
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		Circle c = drawRandomCircle(arg0.getX(), arg0.getY());
		//notify listeners
		for(DrawingPanelListener l: listeners) {
			l.onNewCircleDrawn(c.x, c.y, c.radius, c.col);
		}
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		Circle c = drawRandomCircle(e.getX(), e.getY());
		//notify listeners
		for(DrawingPanelListener l: listeners) {
			l.onNewCircleDrawn(c.x, c.y, c.radius, c.col);
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
