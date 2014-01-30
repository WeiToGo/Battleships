/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my_game.models.game_components;

/**
 * A zone of predefined size containing
 * obstacles generated at runtime randomly.
 */
public class CoralReef {
    /** Number of cells the coral reef is wide (x-axis dimension in top projection). */
    public final int WIDTH = 10;
    /** Number of cells the coral reef is high (y-axis dimension in top projection). */
    public final int HEIGHT = 24;
    
    /** A grid of booleans. True means an obstacle is in the grid. */
    private boolean[][] reef;
    
    /**
     * @param x Positive and within bounds x-coordinate on the reef.
     * @param y Positive and within bounds y-coordinate on the reef.
     * @return True if there is an obstacle in that grid space, false otherwise.
     */
    public boolean hasObstacleIn(int x, int y) {
        return reef[x][y];
    }
}
