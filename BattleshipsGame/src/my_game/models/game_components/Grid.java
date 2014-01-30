/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my_game.models.game_components;

/**
 * This class contains a grid of predefined size
 * which represents a map that can contain different
 * objects such as bases, ships, and obstacles (coral reef).
 */
class Grid {
    
    /** Number of cells the grid is wide (x-axis dimension in top projection). */
    public final int WIDTH = 30;
    /** Number of cells the grid is high (y-axis dimension in top projection). */
    public final int HEIGHT = 30;
    
    /** This is the map. Every coordinate of the 2-D array contains a reference to a game object. */
    private GameObject[][] grid;
    
    public Grid() {
        // initialize the grid array
        grid = new GameObject[WIDTH][HEIGHT];
        // TODO change constructor as necessary during development
    }
    
    /**
     * Accessor method for game objects in the grid.
     * @param x Positive and within bounds x-coordinate on the grid.
     * @param y Positive and within bounds y-coordinate on the grid.
     * @return Game object at the specified coordinates in the grid, or null
     * if invalid coordinate.
     */
    public GameObject getObjectAt(int x, int y) {
        if(x >= 0 && x < WIDTH &&
           y >= 0 && y < HEIGHT) {
            return grid[x][y];
        } else {
            return null;
        }
    }
    
    /**
     * Mutator method for inserting game objects into the grid.
     * @param x Positive and within bounds x-coordinate on the grid.
     * @param y Positive and within bounds y-coordinate on the grid.
     * @param object Game object to insert.
     * @return The game object which was successfully inserted, or null if
     * the coordinates are invalid, or the object is null.
     */
    public GameObject setObjectAt(int x, int y, GameObject object) {
        if(x >= 0 && x < WIDTH &&
           y >= 0 && y < HEIGHT && object != null) {
            grid[x][y] = object;
            return grid[x][y];
        } else {
            return null;
        }
    }
}
