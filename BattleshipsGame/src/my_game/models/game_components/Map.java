/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
*/
package my_game.models.game_components;

import my_game.util.GameException;
import my_game.util.Vector2;
import java.util.ArrayList;
import my_game.util.Positions;


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
    /** 2D array giving the radar visibility for every grid cell of the map. */
    private boolean[][] player0Visibility, player1Visibility;   //TODO implement
    
    protected Ship[] player0Ships;
    protected Ship[] player1Ships;
    private Base p1Base;
    private Base p2Base;
    
    
    public Map(CoralReef reef, Ship[] player0Ships, Ship[] player1Ships, Base b0, Base b1) {
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
                    Vector2 position = new Vector2(i,j);
                    this.setObjectAt(position, coralUnit);
                }
            }
        }
        
        //init players' ships
        this.player0Ships = new Ship[player0Ships.length];
        this.player1Ships = new Ship[player1Ships.length];
        // copy the arrays by value to local arrays
        System.arraycopy(player0Ships, 0, this.player0Ships, 0, player0Ships.length);
        System.arraycopy(player1Ships, 0, this.player1Ships, 0, player1Ships.length);
        //Position ships on the map grid as well.
        initShips(player0Ships);
        initShips(player1Ships);
        
        this.p1Base = b0;
        this.p2Base = b1;
        
        initBase(b0);
        initBase(b1);
    }
    
    /**
     * This method gets all availableMoves generated by the ship and remove the
     * positions blocked by obstacle.
     * @param ship The ship to be moved.
     * @return An array of positions that are highlighted on the map.
     */
    public ArrayList<Vector2> prepareMoveShip(Ship ship){
        Positions allMoves = ship.availableMoves();
        ArrayList<Vector2> highlightedMoves = new ArrayList<Vector2>();
        //if there is any obstacle on left or right, the ship can't move sideways.
        ArrayList<Vector2> left = allMoves.getLeft();
        boolean canMove = true;
        for (int i = 0; i < left.size(); i++){
            if (isObstacle(left.get(i))){
                canMove = false;
            }
        }
        if (canMove){
            highlightedMoves.addAll(left);
        }

        ArrayList<Vector2> right = allMoves.getRight();
        canMove = true;
        for (int i = 0; i < right.size(); i++){
            if (isObstacle(right.get(i))){
                canMove = false;
            }
        }
        if (canMove){
            highlightedMoves.addAll(right);
        }
             
        
        ArrayList<Vector2> back = allMoves.getBackward();
        for (int i = 0; i < back.size(); i++){
            if (!isObstacle(back.get(i))){
                highlightedMoves.add(back.get(i));
            }
        }
        // if there is an obstacle in front, the ship can't move beyond that obstacle.
        ArrayList<Vector2> forward = allMoves.getForward();
        for (int i = 0; i < forward.size(); i++){
            if (isObstacle(forward.get(i))){
                highlightedMoves.add(forward.get(i));
            }else{
                break;
            }
        }
        
        return highlightedMoves;
    }
    
    /**
     * This method first validates the posititons, (for obstacles out of radar
     * range or mines). It then moves the ship to a selected new position by 
     * taking all of its parts to that new position. If no such ship exists or
     * the position is invalid a GameException will be thrown.
     * @param ship The ship we want to move.
     * @param newPosition The position of the bow of the new position.
     * @throws GameException 
     */
    public void moveShip(Ship ship,Vector2 newPosition) throws GameException {
         
        ArrayList<Vector2> validePositions = validateMove(ship,newPosition);
        ship.moveTo(validePositions);
        
    }
    
    /**
     * This method checks if there are ships or mines in positions that the 
     * player wants to move to. It's called by moveShip.
     * @param p The desired position of the bow of the ship.
     * @return The valide positions that the ship can move.
     */
    private ArrayList<Vector2> validateMove(Ship s, Vector2 p){     
        ArrayList<Vector2> shipPositions = getMovePositions(s,p);
        ArrayList<Vector2> validMoves = validateMovePositions(s,shipPositions);
        return validMoves;
    }
    /**
     * This method may need to be static?
     * @param p1 The current position of the bow of the ship.
     * @param p2 The desired position of the bow of this ship.
     * @return An array of positions in between p1 and p2. (should handle the 
     * cases of left, right, and back move as well. 
     */
    private ArrayList<Vector2> getMovePositions(Ship s, Vector2 p){
        throw new UnsupportedOperationException("Not yet implemented");

    }
    
    /**
     * This method checks if there are obstacles or mines in the positions,
     * if there are obstacles, the move should stop right before the obstacle,
     * if there is a mine, touchedMine is called. 
     * @param p The array of positions to validate.
     * @return The new positions that the ship will be moved to. It would be the 
     * same than the input is all positions are clear.
     */
    public ArrayList<Vector2> validateMovePositions(Ship s, ArrayList<Vector2> p){
     //   throw new UnsupportedOperationException("Not yet implemented"); 
        // to remember the position where an obstacle or mine is encountered.
        Vector2 obstacle, mine; 
        for (Vector2 v: p){
            if (isObstacle(v)){
                obstacle = v;
                break;
            }else if (isMine(v)){
                obstacle = v;
                break;
            }else{
               return p;
            }
        }
        // need generate new valid positions.
        
        return null; // TO CHANGE.
     
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
            for(ShipUnit su: s.getShipUnits()) {
                Vector2 position = su.getPosition();
                grid[position.x][position.y] = su;
            }
        }
    }
    
    private void initBase(Base b) {
        // TO DO: position the baseunits on the map. Location should be fixed
        // and the Base doesn't need to know its location.
     }
    
/**
* Gather infomation about the ship to calculate the
* possible places that ship can turn to.
*/
    public Vector2[] prepareTurnShip(Ship ship){
        // TODO same as in prepareMoveShip
        //return availableMoves;
        throw new UnsupportedOperationException("Not yet implemented");
    }
    
    public void turnShip(Ship ship, int degree){
        // check if degree is valid (+-90, +-180)
        
        // calls validateTurn(), if returns null, does nothing.
        
    }
     /**
     * This method checks if there are ships, coral reef in positions that the 
     * player wants to turn to. It's called by turnShip.
     * @param p The desired position of the bow of the ship.
     * @return The valide positions that the ship can move.
     */
    public Vector2[] validateTurn(Ship s, Vector2 p){
        throw new UnsupportedOperationException("Not yet implemented");
        // needs to know which ship unit is the pivot for the turn.
        
        //Vectors2[] shipPositions = getTurnPositions(p1,p2,pivot);
        
        //calls validateTurnPositions(shipPositions), if returns false, return
        //null
    }
    
    /**
     * This method may need to be static?
     * @param p1 The current position of the bow of the ship.
     * @param p2 The desired position of the bow of this ship.
     * @return An array of positions corresponding to positions in between s 
     * and p.
     */
    public Vector2[] getTurnPositions(Vector2 p1, Vector2 p2, Vector2 pivot){
        throw new UnsupportedOperationException("Not yet implemented");

    }
    
    /**
     * This method checks if there are obstacles or mines in the positions,
     * if there are obstacles, the move should stop right before the obstacle,
     * if there is a mine, touchedMine is called. 
     * @param p The array of positions to validate.
     * @return The new positions that the ship will be moved to. It would be the 
     * same than the input is all positions are clear.
     */
    public boolean validateTurnPositions(Vector2[] p){
  //      throw new UnsupportedOperationException("Not yet implemented"); 
        // to remember the position where an obstacle or mine is encountered.
        boolean turn = true;
        for (Vector2 v: p){
            if (isObstacle(v)){
                turn = false;
                break; // don't turn if there is an obstacle.
            }else if (isMine(v)){
                //call touchedMine
                turn = false;
                break;
            }
        }
        
        return turn;
    }
    
   
    public boolean isObstacle(Vector2 position){
        boolean isObstacle = false; //set to false for now, TO CHANGE
        return isObstacle;
                
    }
    
    public boolean isMine(Vector2 position){
        boolean isMine = false;
        return isMine;
    }
    
    public boolean isClear(Vector2 position){
        boolean isClear = false;
        return isClear;
    }
    
    /**
* Accessor method for game objects in the grid.
* @param position Positive and within bounds on the grid.
* @return Game object at the specified coordinates in the grid, or null
* if invalid coordinate.
*/
      
    public GameObject getObjectAt(Vector2 position) {
        if(position.x >= 0 && position.x < WIDTH &&
           position.y >= 0 && position.y < HEIGHT) {
            return grid[position.x][position.y];
        } else {
            return null;
        }
    }
  
    
    /**
* Mutator method for inserting game objects into the grid.
* @param position Positive and within bounds on the grid.
* @param object Game object to insert.
* @return The game object which was successfully inserted, or null if
* the coordinates are invalid, or the object is null.
*/
    public GameObject setObjectAt(Vector2 position, GameObject object) {
        if(position.x >= 0 && position.x < WIDTH &&
           position.y >= 0 && position.y < HEIGHT && object != null) {
            grid[position.x][position.y] = object;
            return grid[position.x][position.y];
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
