/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
*/
package my_game.models.game_components;

import my_game.models.ships_impl.Cruiser;
import my_game.models.ships_impl.Destroyer;
import my_game.models.ships_impl.MineLayer;
import my_game.models.ships_impl.RadarBoat;
import my_game.models.ships_impl.TorpedoBoat;
import my_game.util.GameException;
import my_game.util.Vector2;

import java.util.ArrayList;

import my_game.util.Positions;
import my_game.util.Moves;
import my_game.util.Range;
import my_game.util.TurnPositions;
import my_game.util.Turns;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
* This is the map object containing all game objects dispayed on the
* screen: ships, obstacles (corals), bases and weapons.
*/
public class Map implements java.io.Serializable {
    
    private final int WIDTH = 30;
    private final int HEIGHT = 30;
    /** X offset of the coral reef zone. */
    private final int X_OFFSET = 10;
    /** Y offset of the coral reef zone. */
    private final int Y_OFFSET = 3;
    /** 2D array representing the cells of the map grid which contain game objects. */
    protected GameObject[][] grid = new GameObject[WIDTH][HEIGHT];
    /** 2D array giving the radar visibility for every grid cell of the map. */
    protected boolean[][] player0Visibility, player1Visibility;   //TODO implement
    protected Ship[] player0Ships;
    protected Ship[] player1Ships;
    protected Base p0Base;
    protected Base p1Base;

    public Map(Map m) {
        //shallow copy grid and visibility arrays
        for(int i = 0; i < WIDTH; i++) {
            for(int j = 0; j < HEIGHT; j++) {
                grid[i][j] = m.grid[i][j];
            }
        }
        //copy other fields
        this.player0Ships = new Ship[m.player0Ships.length];
        this.player1Ships = new Ship[m.player1Ships.length];
        //copy ships
        for(int i = 0; i < player0Ships.length; i++) {
            player0Ships[i] = m.player0Ships[i];
        }
        for(int i = 0; i < player1Ships.length; i++) {
            player1Ships[i] = m.player1Ships[i];
        }
        //copy bases
        this.p0Base = m.p0Base;
        this.p1Base = m.p1Base;
        
        //TODO Player visibility must be generated.
    }
    
    public Map(CoralReef reef, Ship[] player0Ships, Ship[] player1Ships, Base b0, Base b1) {
        //clear the grid (init all to null)
        clearGrid();
        /* TODO when creating the mapGrid use a CoralReef to provide
        * the positions of all obstacles. */
        int x_start = X_OFFSET;
        int x_end = X_OFFSET + reef.WIDTH;
        int y_start = Y_OFFSET;
        int y_end = Y_OFFSET + reef.HEIGHT;
        int xMap, yMap, xReef, yReef;
        for (xMap = x_start, xReef = 0; xMap < x_end; xMap++, xReef++){
            for (yMap = y_start, yReef = 0; yMap < y_end; yMap++, yReef++){
                if (reef.hasObstacleIn(xReef, yReef)){
                    CoralUnit coralUnit = new CoralUnit();
                    //maybe setObjectAt should return void?
                    Vector2 position = new Vector2(xMap,yMap);
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
        
        this.p0Base = b0;
        this.p1Base = b1;
        
        initBase(b0);
        initBase(b1);
        
        //TODO Player visibility must be generated.
    }
    
    /**
     * This method gets all availableMoves generated by the ship and remove the
     * positions blocked by obstacle.
     * @param ship The ship to be moved.
     * @return An array of positions that are highlighted on the map.
     */
    public Positions prepareMoveShip(Ship ship){
        Positions allMoves = ship.availableMoves(); 
        //not sure if it's a good idea.
        Positions highlightedMoves = new Positions(null,null,null,null);
        //if there is any obstacle on left or right, the ship can't move sideways.
        ArrayList<Vector2> left = allMoves.getLeft();
        boolean canMove = true;
        for (int i = 0; i < left.size(); i++){
            if (isVisibleObstacle(ship, left.get(i))){
                canMove = false;
            }
        }
        
        if (canMove){
           highlightedMoves.setLeft(left);
        }
        ArrayList<Vector2> right = allMoves.getRight();
        canMove = true;
        for (int i = 0; i < right.size(); i++){
            if (isVisibleObstacle(ship, right.get(i))){
                canMove = false;
            }
        }
        if (canMove){
            highlightedMoves.setRight(right);
        }
              
        ArrayList<Vector2> back = allMoves.getBackward();
        //maybe not necessary but just in case the rule changes.
        ArrayList<Vector2> validBack = new ArrayList<Vector2>();
        for (int i = 0; i < back.size(); i++){
            if (!isSelf(ship,back.get(i))){            
                if (!isVisibleObstacle(ship, back.get(i))) {
                    validBack.add(back.get(i));
                }
            }else{
                validBack.add(back.get(i));
            }
        }
        highlightedMoves.setBack(validBack);
        // if there is an obstacle in front, the ship can't move beyond that obstacle.
        ArrayList<Vector2> forward = allMoves.getForward();
        ArrayList<Vector2> validForward = new ArrayList<Vector2>();
        for (int i = 0; i < forward.size(); i++){
            if (!isSelf(ship,forward.get(i))){            
                if (!isVisibleObstacle(ship, forward.get(i))) {
                    validForward.add(forward.get(i));
                } else if (isVisibleObstacle(ship, forward.get(i))) {
                    break;
                } else {
                    //shouldn't happen
                }
            }else{
                validForward.add(forward.get(i));
            }
        }       
        highlightedMoves.setForward(validForward);         
        return highlightedMoves;
    }
    
    /**
     * Checks if a positions is occupied by itself.
     * @param selfPos
     * @param p
     * @return 
     */
    private boolean isSelf(Ship s, Vector2 p){
        ShipUnit[] shipUnits = s.getShipUnits();
        ArrayList<Vector2> selfPositions = new ArrayList<Vector2>();      
        for (ShipUnit su: shipUnits){
            selfPositions.add(su.getPosition());
        }        
        for (Vector2 v: selfPositions){
            if(v.equals(p)){
                return true;
            }
        }
        return false;
    }
    /**
     * This method first validates the posititons, (for obstacles out of radar
     * range or mines). It then moves the ship to a selected new position by 
     * taking all of its parts to that new position. If no such ship exists or
     * the position is invalid a GameException will be thrown.
     * @param ship The ship we want to move.
     * @param newPosition The position of the bow of the new position.
     * @param p The Gamestate will keep the highlighted moves so moveship can it
     * in getMovePositions.
     * @throws GameException 
     */
//    public void moveShip(Ship ship,Vector2 newPosition, Positions p) throws GameException {
    public void moveShip(Ship ship,Vector2 newPosition, Positions p) {
        Moves shipPositions = getMovePositions (newPosition, p);
    /*    for (Vector2 v : shipPositions.getPositions()){
            System.out.println(" getPos " + v.x + " " + v.y);
        }
   */
        ArrayList<Vector2> valide = new ArrayList<Vector2>();
        valide = validateMove(ship, shipPositions);
        
 /*       try{
        }catch (GameException e){
            Logger.getLogger(Map.class.getName()).log(Level.SEVERE, null,
                    new GameException("move error"));
        }
 */       if (valide != null){
            this.updateShipPositions(ship, valide);    
            ship.moveTo(valide);           
        }

    }
    
    /**
     * This method calcule all positions that need be checked in order move the
     * ship successfully.
     * @param p The new position(of the bow) that the player wants to move. 
     * @param positions All positions this ship can move to.
     * @return An array containing all positions on the path leading to the new 
     * position and a MoveDirection indicating which direction we moved.
     */
    private Moves getMovePositions(Vector2 p, Positions positions){
        Moves moves = new Moves();
        ArrayList<Vector2> back = positions.getBackward();
        if(back != null){
            for (Vector2 v : back) {
                if (v.x == p.x && v.y == p.y) {
                    moves.setMoveDirection(Moves.MoveDirection.B);
                    moves.setMoves(back);
                    return moves;// only works if we can only move backward one square.
                }
            }
        }
        ArrayList<Vector2> left = positions.getLeft();
        if (left != null){
            for (Vector2 v : left) {
                if (v.x == p.x && v.y == p.y) {
                    moves.setMoveDirection(Moves.MoveDirection.L);
                    moves.setMoves(left);
                    return moves;
                }
            }
        }
        ArrayList<Vector2> right = positions.getRight();
        if (right != null){
            for (Vector2 v : right) {
                if (v.x == p.x && v.y == p.y) {
                    moves.setMoveDirection(Moves.MoveDirection.R);
                    moves.setMoves(right);
                    return moves;
                }
            }
        }
        ArrayList<Vector2> forward = positions.getForward();
        boolean target = false;
        if (forward != null){
            ArrayList<Vector2> newforward = new ArrayList<Vector2>();
            for (Vector2 v : forward) {
                if (v.x == p.x && v.y == p.y) {
                    moves.setMoveDirection(Moves.MoveDirection.F);
                    newforward.add(v);
                    break;
                } else {
                    newforward.add(v);
                }
            }
            moves.setMoves(newforward);
        }
        return moves;
    }
    
    /**
     * This method checks if there are obstacles or mines in the positions,
     * if there are obstacles, the move should stop right before the obstacle,
     * if there is a mine, touchedMine is called. 
     * @param p The array of positions to validate.
     * @return The new positions that the ship will be moved to. It would be the 
     * same than the input is all positions are clear.
     */
    public ArrayList<Vector2> validateMove(Ship s, Moves p){
        //MAKE SURE THE 1ST IN THE RETURED ARRAY IS THE POSITION OF THE BOW OF THE SHIP.
        // to remember the position where an obstacle or mine is encountered.
        ArrayList<Vector2> moves = p.getPositions();
        Vector2 obstacle, mine; 
        int shipSize = s.getSize();
        int i;
        ShipUnit[] damagedUnits = new ShipUnit[2];
        if (p.getMoveDirection() == null){
            Logger.getLogger(Map.class.getName()).log(Level.SEVERE, null, 
                    new GameException("no move direction, something's wrong."));            
        }else{
            switch (p.getMoveDirection()) {
                case F:
                    // stores all valid positions between ship bow to target.
                    ArrayList<Vector2> forwardmoves = new ArrayList<Vector2>();
                    //need to generate new positions if needed.
                    for (Vector2 v : moves) {
                        if (isHiddenObstacle(s, v)) {
                            break;
                        }
                        if (isMine(v)) {
                            mine = v;
                            //get first 2 shipUnits?
                            touchedMine(mine, damagedUnits);
                            return moves;
                        } else {
                            forwardmoves.add(v);
                        }
                    }
                    
                    moves.clear();
                    for (i = forwardmoves.size()-1; i >= forwardmoves.size()-shipSize; i--){
                        moves.add(forwardmoves.get(i));                        
                    }
                    break;
                case B:
                    for (Vector2 v : moves) {
                        if (isHiddenObstacle(s, v)) {
                            return null;
                        }
                        if (isMine(v)) {
                            mine = v;
                            //get last 2 shipUnits?
                            touchedMine(mine, damagedUnits);
                            return null;
                        }
                    }    
                case L:
                    for (Vector2 v : moves) {
                        if (isHiddenObstacle(s, v)) {
                            return null;
                        }
                        if (isMine(v)) {
                            mine = v;
                            //get last 2 shipUnits?
                            touchedMine(mine, damagedUnits);
                            return null;
                        }
                    }
                    break;
                case R:
                    for (Vector2 v : moves) {
                        if (isHiddenObstacle(s, v)) {
                            return null;
                        }
                        if (isMine(v)) {
                            mine = v;
                            //get last 2 shipUnits?
                            touchedMine(mine, damagedUnits);
                            return null;
                        }
                    }
                    break;
                default: // do something?
            }
        }

        return moves;     
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
        BaseUnit[] baseUnits = b.getBaseUnits();
        for(BaseUnit bu: baseUnits) {
           Vector2 position = bu.getPosition();
           grid[position.x][position.y] = bu;
        }       
     }
/**
* Gather infomation about the ship to calculate the
* possible places that ship can turn to.
*/
    public TurnPositions prepareTurnShip(Ship ship){
   
        TurnPositions allTurns = ship.availableTurns();
        TurnPositions highlightedTurns = new TurnPositions(null,null,null,null,null);
        // highlight a particular turn only if all positions on the path are clear.
        ArrayList<Vector2> left = allTurns.getLeft();
    //    System.out.println("left size " + left.size());
        ArrayList<Vector2> leftPath = allTurns.getLeftPath();
        ShipDirection ld = allTurns.getLeftDirection();
        boolean canMoveLeft = true;
        for (int i = 0; i < left.size(); i++){
            if(!isSelf(ship,left.get(i))){
                if (isVisibleObstacle(ship, left.get(i))) {
                    canMoveLeft = false;
                }
            }
        }
        for (int i = 0; i < leftPath.size(); i++){
            if (isVisibleObstacle(ship, leftPath.get(i))){
                canMoveLeft = false;
            }
        }
        if (canMoveLeft){
           highlightedTurns.setLeft(left);
           highlightedTurns.setLeftPath(leftPath);
           highlightedTurns.setLeftDirection(ld);
        }    
        ArrayList<Vector2> right = allTurns.getRight();
        ArrayList<Vector2> rightPath = allTurns.getRightPath();
        ShipDirection rd = allTurns.getRightDirection();
     //   System.out.println("right size " + right.size());
        boolean canMoveRight = true;
        for (int i = 0; i < right.size(); i++){
            if(!isSelf(ship,right.get(i))){
                if (isVisibleObstacle(ship, right.get(i))) {
                    canMoveRight = false;
                }
            }
        }
        for (int i = 0; i < rightPath.size(); i++){
            if (isVisibleObstacle(ship, rightPath.get(i))){
                canMoveRight = false;
            }
        }
        if (canMoveRight){
           highlightedTurns.setRight(right);
           highlightedTurns.setRightPath(rightPath);
           highlightedTurns.setRightDirection(rd);
        }
        ArrayList<Vector2> back = allTurns.getBackward();
    //    System.out.println("back size " + back.size());
        ShipDirection bd = allTurns.getBackDirection();
        for (int i = 0; i < right.size(); i++){
            if (isVisibleObstacle(ship, right.get(i))){
                canMoveRight = false;//can also use canMoveLeft.
            }
        }
        for (int i = 0; i < rightPath.size(); i++){
            if (isVisibleObstacle(ship, rightPath.get(i))){
                canMoveRight = false;
            }
        }
        if (canMoveLeft && canMoveRight){
           highlightedTurns.setBack(back);
           highlightedTurns.setBackDirection(bd);
        }
        // careful in Game, path are NOT highlighted. 
        return highlightedTurns;
    }
    
    public void turnShip(Ship ship, Vector2 newPosition, TurnPositions p){
        Turns shipPositions = getTurnPositions (newPosition, p);
        ArrayList <Vector2> t = shipPositions.positions;
        if (t == null){
            return;
        }
        System.out.println("t size " + t.size());
    /*   for (Vector2 v: t){
            System.out.println("turnpositions " + v.x + " " + v.y);
        }
    */
        Turns validTurns = validateTurn(ship, shipPositions);
        // find the new ship direction
        ArrayList <Vector2> valid = validTurns.getTurns();
        ShipDirection newDirection = validTurns.getNewDirection();
        if (valid != null){
            this.updateShipPositions(ship, valid);    
            ship.turnTo(valid, newDirection);           
        }        
        
    }
    /**
     * This method calculate all positions that need be checked in order move the
     * ship successfully.
     * @param p The new position(of the bow) that the player wants to move. 
     * @param positions All positions this ship can turn to and all path this ship
     * need to pass.
     * @return An array containing all positions on the path leading to the new 
     * position and an array indicating new ship positions.
     */    
    private Turns getTurnPositions(Vector2 p, TurnPositions positions){
        Turns turns = new Turns();      
        ArrayList<Vector2> leftPath = new ArrayList<Vector2>();
        ArrayList<Vector2> rightPath = new ArrayList<Vector2>();
        ArrayList<Vector2> left = positions.getLeft();
        if (left != null){        
            leftPath = positions.getLeftPath();
            ShipDirection ld = positions.getLeftDirection();
            for (Vector2 v : left) {
                if (v.x == p.x && v.y == p.y) {
                    turns.setTurns(left);
                    turns.setPath(leftPath);
                    turns.setNewDirection(ld);
                    return turns;
                }
            }
        }
        ArrayList<Vector2> right = positions.getRight();
        if (right != null){
            rightPath = positions.getRightPath();
            ShipDirection rd = positions.getRightDirection();
            for (Vector2 v : right) {
                if (v.x == p.x && v.y == p.y) {
                    turns.setTurns(right);
                    turns.setPath(rightPath);
                    turns.setNewDirection(rd);
                    return turns;
                }
            }  
        }
        ArrayList<Vector2> back = positions.getBackward();
        if (back != null){
            ArrayList<Vector2> backPath = new ArrayList<Vector2>();
            ShipDirection bd = positions.getBackDirection();
            for (Vector2 v : back) {
                if (v.x == p.x && v.y == p.y) {
                    turns.setTurns(back);
                    backPath.addAll(leftPath);
                    backPath.addAll(rightPath);
                    turns.setPath(backPath);
                    turns.setNewDirection(bd);
                    return turns;
                }
            }
        }
        return turns;
   }
     /**
     * This method checks if there are ships, coral reef in positions that the 
     * player wants to turn to. It's called by turnShip.
     * @param p The desired position of the bow of the ship.
     * @return The validate positions that the ship can move.
     */
    public Turns validateTurn(Ship s, Turns t){
        Turns valid = new Turns();
        ArrayList<Vector2> turns = t.getTurns();
        ArrayList<Vector2> turnPath = t.getPath();
        ShipDirection d = t.getNewDirection();
  
        boolean canTurn = true;
        Vector2 obstacle, mine;   
        ShipUnit[] damagedUnits = new ShipUnit[2];

  //      if (!s.hasFlexibleTurn()){
            for (Vector2 v: turnPath){
                if (isHiddenObstacle(s,v)){
                    canTurn = false;
                    break; // don't turn if there is an obstacle.
                }else if (isMine(v)){
                    mine = v;
                    //get 2 shipUnits
                    touchedMine(mine, damagedUnits);
                    canTurn = false;
                    break;
                }
            }
            for (Vector2 v: turns){
                if (isHiddenObstacle(s,v)){
                    canTurn = false;
                    break; // don't turn if there is an obstacle.
                }else if (isMine(v)){
                    mine = v;
                    //get 2 shipUnits
                    touchedMine(mine, damagedUnits);
                    canTurn = false;
                    break;
                }
            }            
            if (canTurn){
                valid.setTurns(turns);
                valid.setPath(turnPath);
                valid.setNewDirection(d);
            }
            
   //     }else{
        return valid;
    }

    /**
     * This method checks if there is any visible obstacle at a position for 
     * a given ship. (Including Mine for minelayers)
     * @param s
     * @param p
     * @return 
     */
    public boolean isVisibleObstacle(Ship s, Vector2 p){
        boolean isVisibleObstacle = false;
        GameObject o = this.getObjectAt(p);    
        // null game object is the empty sea. 
        if (o == null){
            return false;
        }
        if (o.getObjectType().compareTo(GameObject.GameObjectType.Base)==0){
             isVisibleObstacle = true;
        }else if (o.getObjectType().compareTo(GameObject.GameObjectType.CoralReef)==0){
            isVisibleObstacle = true;

        }else if (o.getObjectType().compareTo(GameObject.GameObjectType.Ship)==0){
            ArrayList<Vector2> visible = s.getRadarPositions();
            
            for (Vector2 v: visible){
               // System.out.println("visible " +v.x + " " + v.y);
                if (v.equals(p)){
                    isVisibleObstacle = true;
                    break;
                }
            }
        }else if (o.getObjectType().compareTo(GameObject.GameObjectType.Mine) == 0){
            if (s.getShipType().compareTo(Ship.ShipType.MineLayer) == 0){
                ArrayList<Vector2> visible = s.getRadarPositions();
                for (Vector2 v: visible){
                    if (v.equals(p)){
                        isVisibleObstacle = true;
                        break;
                    }
                }                
            }
        }    
        
        return isVisibleObstacle;
    }
    /**
     * This method checks if there is an obstacle (NOT INCLUDING mines) at a 
     * position that is not visible for a given ship.
     * @param s
     * @param p
     * @return 
     */   
    public boolean isHiddenObstacle(Ship s, Vector2 p){
        boolean isHiddenObstacle = false;
        boolean canSee = false;
        GameObject o = this.getObjectAt(p);   
        if (o == null){
            return false;
        }  
        
        if (isSelf(s,p)){
            return false;
        }
        if (o.getObjectType().compareTo(GameObject.GameObjectType.Ship)==0){
            ArrayList<Vector2> visible = s.getRadarPositions();
            for (Vector2 v: visible){
                if (v.equals(p)){
                    canSee = true;
                    break;
                }
            }
            if (!canSee){
                isHiddenObstacle = true;
            }
        }
        
        return isHiddenObstacle;
    }  
    
    public boolean isMine(Vector2 p){
        boolean isMine = false;
        GameObject o = this.getObjectAt(p);  
        if (o == null){
            return false;
        }        
        if (o.getObjectType().compareTo(GameObject.GameObjectType.Mine) == 0){
            isMine = true;
        }
        return isMine;
    }
    
    public void touchedMine(Vector2 mineZone, ShipUnit[] damagedUnits){
        GameObject mine = getObjectAt(mineZone);
        //assume we can get the actual mine which is at the center of this mineZone,
        // and we destroyed it.
        for (ShipUnit s: damagedUnits){
            s.setDamage(1);
        }    
    }
    public boolean isClear(Vector2 position){
        boolean isClear = false;
        if (this.getObjectAt(position) == null){
            isClear = true;
        }
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
     * This method is called after every moves to get the map updated.
     * @param s The ship that we just moved.
     */
    public void updateShipPositions(Ship s, ArrayList<Vector2> positions){   
        if (s.getSize() != positions.size()){
        Logger.getLogger(GameState.class.getName()).log(Level.SEVERE, null, 
            new GameException("ship size not equal to position size."));          
        }else{
            ShipUnit[] shipUnits = s.getShipUnits();
            for (ShipUnit su: shipUnits){  
                Vector2 oldPosition = su.getPosition();
                this.setObjectAt(oldPosition, null);
            }
            int j = 0;
            for (ShipUnit su: shipUnits){  
                this.setObjectAt(positions.get(j), su);          
                j++;
            }            
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
           position.y >= 0 && position.y < HEIGHT) {
            grid[position.x][position.y] = object;
            return grid[position.x][position.y];
        } else {
            return null;
        }
    }
    
    public void cannonAttack(Ship attacker, Vector2 position){
    	GameObject target = getObjectAt(position);
    	int damage = 0;
    	
    	if(attacker.getClass() == new Cruiser(10000).getClass()){
    		damage = ((Cruiser) attacker).getCannonDamage();
    		
    		if (!((Cruiser) attacker).fireCannon(target))
    			return;
    	}
    	else if(attacker.getClass() == new TorpedoBoat(10000).getClass()){
    		damage = ((TorpedoBoat) attacker).getCannonDamage();
    		
    		if (!((TorpedoBoat) attacker).fireCannon(target))
    			return;
    	}
    	else if(attacker.getClass() == new Destroyer(10000).getClass()){
    		damage = ((Destroyer) attacker).getCannonDamage();
    		
    		if (!((Destroyer) attacker).fireCannon(target))
    			return;
    	}
    	else if(attacker.getClass() == new MineLayer(10000).getClass()){
    		damage = ((MineLayer) attacker).getCannonDamage();
    		
    		if (!((MineLayer) attacker).fireCannon(target))
    			return;
    	}
    	else if(attacker.getClass() == new RadarBoat(10000).getClass()){
    		damage = ((RadarBoat) attacker).getCannonDamage();
    		
    		if (!((RadarBoat) attacker).fireCannon(target))
    			return;
    	}

    	if (target.getClass() == new ShipUnit().getClass()){
			((ShipUnit)target).setDamage(damage);
		}
		
		if (target.getClass() == new BaseUnit().getClass()){
			((BaseUnit)target).setDamage();
		}
		
		if (target.getClass() == new Mine().getClass()){
			setObjectAt(position, null);
		}
    }
    
    public void torpedoAttack(Ship attacker, Vector2 position){
    	int damage = 0;
    	
    	ShipDirection d = attacker.getDirection();
    	Vector2 head = attacker.getShipUnits()[0].getPosition();
    	int x = head.x;
    	int y = head.y;
    	
    	ArrayList<Vector2> torpedoRange = new ArrayList<Vector2>();
    	
    	switch (d){
	        case East:
	            for(int i = 1; i <= 10; i++){
	            	if(x + i < 30){
	            		torpedoRange.add(new Vector2(x + i, y));
	            	}
	            	else
	            		break;
	            }
	        	break;
	        	
	        case North: 
	        	for(int i = 1; i <= 10; i++){
	            	if(y - i >= 0){
	            		torpedoRange.add(new Vector2(x, y - i));
	            	}
	            	else
	            		break;
	            }
	        	
	        case South: 
	        	for(int i = 1; i <= 10; i++){
	            	if(y + i < 30){
	            		torpedoRange.add(new Vector2(x, y + i));
	            	}
	            	else
	            		break;
	            }
	        	
	        case West: 
	        	for(int i = 1; i <= 10; i++){
	            	if(x - i >= 0){
	            		torpedoRange.add(new Vector2(x - i, y));
	            	}
	            	else
	            		break;
	            }
	            break;
    	} 

    	GameObject target = null;
    	for (Vector2 vec : torpedoRange){
    		if(target.getClass() == new ShipUnit().getClass() ||
    		           target.getClass() == new BaseUnit().getClass() ||
    		           target.getClass() == new Mine().getClass()) {
    			target = getObjectAt(vec);
    			break;
    		}	
    	}
    	
    	if(attacker.getClass() == new TorpedoBoat(10000).getClass()){
    		damage = ((TorpedoBoat) attacker).getTorpedoDamage();
    		
    		if (((TorpedoBoat) attacker).fireTorpedo(target)){
    			
    			if (target.getClass() == new ShipUnit().getClass()){
    				((ShipUnit)target).setDamage(damage);
    			}
    			
    			if (target.getClass() == new BaseUnit().getClass()){
    				((BaseUnit)target).setDamage();
    			}
    					
    			if (target.getClass() == new Mine().getClass()){
    				setObjectAt(position, null);
    			}
    		}
    	}
    	else if(attacker.getClass() == new Destroyer(10000).getClass()){
    		damage = ((Destroyer) attacker).getTorpedoDamage();
    		
    		if (((Destroyer) attacker).fireTorpedo(target)){
    			
    			if (target.getClass() == new ShipUnit().getClass()){
    				((ShipUnit)target).setDamage(damage);
    			}
    			
    			if (target.getClass() == new BaseUnit().getClass()){
    				((BaseUnit)target).setDamage();
    			}
    					
    			if (target.getClass() == new Mine().getClass()){
    				setObjectAt(position, null);
    			}
    			
    			
    		}
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
    
    
    public static void main(String[] args) {
        
    }
    
    @Override
    public String toString() {
        //just print out the map grid where obstacles are '#', empty spaces are '*'
        //and other objects are 'o'
        StringBuilder sb = new StringBuilder();
        
        for(int y = 0; y < HEIGHT; y++) { //first y so that we go horizontal line by line in the grid
            for(int x = 0; x < WIDTH; x++) {
                if(grid[x][y] == null) {
                    sb.append("-");
                } else if(grid[x][y] instanceof CoralUnit) {
                    sb.append("C");
                } else if(grid[x][y] instanceof ShipUnit) {
                    sb.append("S");
                } else if(grid[x][y] instanceof BaseUnit) {
                    sb.append("B");                    
                } else {
                    sb.append("o");
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public String toStringTry() {
        //just print out the map grid where obstacles are '#', empty spaces are '*'
        //and other objects are 'o'
        StringBuilder sb = new StringBuilder();
        GameObject o;
        Vector2 v;
        for(int y = 0; y < HEIGHT; y++) { //first y so that we go horizontal line by line in the grid
            for(int x = 0; x < WIDTH; x++){ 
                v = new Vector2(x,y);
                o = this.getObjectAt(v);
                if(o == null) {
                    sb.append("-");
                } else if(o.getObjectType().compareTo(GameObject.GameObjectType.CoralReef) == 0) {
                    sb.append("C");
                } else if(o.getObjectType().compareTo(GameObject.GameObjectType.Ship) == 0) {
                    sb.append("S");
                } else if(o.getObjectType().compareTo(GameObject.GameObjectType.Base) == 0) {
                    sb.append("B");                    
                } else {
                    sb.append("o");
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }    
}
