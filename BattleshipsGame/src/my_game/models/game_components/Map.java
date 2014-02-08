/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my_game.models.game_components;


/**
 * This is the map object containing all game objects dispayed on the
 * screen: ships, obstacles (corals), bases and weapons.
 */
public class Map {
    
    public final int WIDTH = 30;
    public final int HEIGHT = 30;
    
    private GameObject[][] grid = new GameObject[WIDTH][HEIGHT];
    private CoralReef reef; 
    
    private GameObject[][] availableMoves;//including turns
//    private GameObject[][] availableMovesP2;
    private GameObject[][] radarView;// all grids that can be seen by all its ships.
//    private GameObject[][] radarP2;
    
    private Ship[] player1Ships;
    private Ship[] player2Ships;
    
    
    public Map(CoralReef reef) {
        /* TODO when creating the mapGrid use a CoralReef to provide 
         * the positions of all obstacles. */
        
    }
    
    /**
     * Gather infomation about the ship to calculate the 
     * possible places that ship can move to.
     */
    public GameObject[][] prepareMoveShip(Ship ship){
        
        return availableMoves;
    }
    
    /**
     * This method is called after the user selects a move.
     * @param x
     * @param y 
     */
    public void moveShip(Ship ship, int x, int y){
        
    }
    
     /**
     * Gather infomation about the ship to calculate the 
     * possible places that ship can turn to.
     */
    public GameObject[][] prepareTurnShip(Ship ship){
        
        return availableMoves;
    }
    
    public void turnShip(Ship ship, int degree){
        // check if degree is valid (+-90, +-180)
        
    }
    
    public boolean isObstacle(int x, int y){
        boolean isObstacle = false; //set to false for now, TO CHANGE    
        return isObstacle;
                
    }
    
    public boolean isMine(int x, int y){
        boolean isMine = false;
        return isMine;
    }
    
    public boolean isClear(int x, int y){
        boolean isClear = false;
        return isClear;
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
