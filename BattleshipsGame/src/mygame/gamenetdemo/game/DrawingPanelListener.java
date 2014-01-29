package ca.gamenetdemo.game;

import java.awt.Color;

public interface DrawingPanelListener {

	/**
	 * This method is called every time a new circle is drawn
	 * as a result of a mouse click in the DrawingPanel.
	 * @param x
	 * @param y
	 * @param radius
	 * @param color
	 */
	public void onNewCircleDrawn(float x, float y, float radius, Color color);
}
