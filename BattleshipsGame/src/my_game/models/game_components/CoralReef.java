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
    /** The probability with which an obstacle can appear in a given cell. */
    private final double CHANCE_OF_OBSTACLE = 0.3;
    
    /** A grid of booleans. True means an obstacle is in the grid. */
    private boolean[][] reef;

    /** Constructs a coral reef with random coral positions. */
    public CoralReef() {
        reef = new boolean[WIDTH][HEIGHT];
        reef = randomizeObstaclePositions(reef);
    }
  
    /**
     * @param x Positive and within bounds x-coordinate on the reef.
     * @param y Positive and within bounds y-coordinate on the reef.
     * @return True if there is an obstacle in that grid space, false otherwise.
     */
    public boolean hasObstacleIn(int x, int y) {
        return reef[x][y];
    }
    
    /**
     * Generate random coral reef positions and set it to the reef variable.
     */
    public void generateNewReef(){
        this.reef = randomizeObstaclePositions(reef);
    }

    /**
     * Creates obstacles (sets boolean to true) at random points in the provided
     * array, then returns it.
     * @param reef 
     */
    private boolean[][] randomizeObstaclePositions(boolean[][] reef) {
        for(int x = 0; x < reef.length; x++) {
            for(int y = 0; y < reef[0].length; y++) {
                if(Math.random() < CHANCE_OF_OBSTACLE) {
                    reef[x][y] = true;
                } else {
                    reef[x][y] = false;
                }
            }
        }
        return reef;
    }
}
