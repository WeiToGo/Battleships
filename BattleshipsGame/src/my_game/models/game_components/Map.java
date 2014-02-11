/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
*/
package my_game.models.game_components;

import my_game.networking.util.Vector2;


/**
* This is the map object containing all game objects dispayed on the
* screen: ships, obstacles (corals), bases and weapons.
*/
public class Map {
    
    private final int WIDTH = 30;
    private final int HEIGHT = 30;
    /** X offset of the coral reef zone. */
    private final int X_OFFSET = 10;
    /** Y offset of the coral reef zone. */
    private final int Y_OFFSET = 3;
    /** 2D array representing the cells of the map grid which contain game objects. */
    private GameObject[][] grid = new GameObject[WIDTH][HEIGHT];
    
    
    /* By Ivo: I don't think these arrays are necessary.
    private GameObject[][] availableMoves;//including turns
// private GameObject[][] availableMovesP2;
    private GameObject[][] radarView;// all grids that can be seen by all its ships.
// private GameObject[][] radarP2;
    */
    private Ship[] player1Ships;
    private Ship[] player2Ships;
    
    
    public Map(CoralReef reef, Ship[] player1Ships, Ship[] player2Ships) {
        //clear the grid (init all to null)
        clearGrid();
        /* TODO when creating the mapGrid use a CoralReef to provide
        * the positions of all obstacles. */
        int x_start = X_OFFSET;
        int x_end = X_OFFSET + reef.WIDTH;
        int y_start = Y_OFFSET;
        int y_end = Y_OFFSET + reef.HEIGHT;
        int i,j;
        for (i = x_start; i < x_end; i++){
            for (j = y_start; j < y_end; j++){
                if (reef.hasObstacleIn(i, j)){
                    CoralUnit coralUnit = new CoralUnit();
                    //maybe setObjectAt should return void?
                    this.setObjectAt(i, j, coralUnit);
                }
            }
        }
        
        //init players' ships
        this.player1Ships = new Ship[player1Ships.length];
        this.player1Ships = new Ship[player2Ships.length];
        // copy the arrays by value to local arrays
        System.arraycopy(player1Ships, 0, this.player1Ships, 0, player1Ships.length);
        System.arraycopy(player2Ships, 0, this.player2Ships, 0, player2Ships.length);
        //Position ships on the map grid as well.
        initShips(player1Ships);
        initShips(player2Ships);
    }
    
/**
* Gather infomation about the ship to calculate the
* possible places that ship can move to.
*/
    public Vector2[][] prepareMoveShip(Ship ship){
        //TODO generate an array of points where the ship can safely move
        // NOTE: There is the Vector2 class which can be used to represent a point
        //return availableMoves;
        throw new UnsupportedOperationException("Not yet implemented");
    }
    
    /**
     * This method is called after the user selects a move.
     * @param x
     * @param y
     */
    public void moveShip(Ship ship, int x, int y){
        
    }
    
    /**
     * Inserts the ships in the shipsArray provided into the grid
     * of this map.
     * @param shipsArray 
     */
    private void initShips(Ship[] shipsArray) {
        //go through the array
        for(Ship s: shipsArray) {
            //and for insert every ship unit of every ship into the grid
            for(ShipUnit su: s.shipUnits) {
                Vector2 position = su.getPosition();
                grid[position.x][position.y] = su;
            }
        }
    }
    
    
     /**
* Gather infomation about the ship to calculate the
* possible places that ship can turn to.
*/
    public Vector2[][] prepareTurnShip(Ship ship){
        // TODO same as in prepareMoveShip
        //return availableMoves;
        throw new UnsupportedOperationException("Not yet implemented");
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

    /**
     * Sets every cell of the grid to null.
     */
    protected void clearGrid() {
        for(int i = 0; i < grid.length; i++) {
            for(int j = 0; j < grid[0].length; j++) {
                grid[i][j] = null;
            }
        }
    }
}