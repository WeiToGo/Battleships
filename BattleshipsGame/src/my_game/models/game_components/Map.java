/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
*/
package my_game.models.game_components;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import my_game.models.ships_impl.Cruiser;
import my_game.models.ships_impl.Destroyer;
import my_game.models.ships_impl.MineLayer;
import my_game.models.ships_impl.RadarBoat;
import my_game.models.ships_impl.TorpedoBoat;
import my_game.util.GameException;
import my_game.util.Moves;
import my_game.util.Positions;
import my_game.util.ShipDirection;
import my_game.util.TurnPositions;
import my_game.util.Turns;
import my_game.util.Vector2;

/**
* This is the map object containing all game objects dispayed on the
* screen: ships, obstacles (corals), bases and weapons.
*/
public class Map implements java.io.Serializable {
    
    public final static int WIDTH = 30;
    public final static int HEIGHT = 30;
    /** X offset of the coral reef zone. */
    private final int X_OFFSET = 10;
    /** Y offset of the coral reef zone. */
    private final int Y_OFFSET = 3;
    /** 2D array representing the cells of the map grid which contain game objects. */
    protected GameObject[][] grid = new GameObject[WIDTH][HEIGHT];
    /** 2D array giving the radar visibility for every grid cell of the map. */
    boolean[][] player0Visibility, player1Visibility;   //TODO implement
    protected Ship[] player0Ships;
    protected Ship[] player1Ships;
    protected Base p0Base;
    protected Base p1Base;
    private ArrayList<Vector2> dockingZone = new ArrayList<Vector2>();

    public Map(Map m) {
        //shallow copy grid and visibility arrays
        player0Visibility = new boolean[WIDTH][HEIGHT];
        player1Visibility = new boolean[WIDTH][HEIGHT];
        
        for(int i = 0; i < WIDTH; i++) {
            for(int j = 0; j < HEIGHT; j++) {
                grid[i][j] = m.grid[i][j];
                player0Visibility[i][j] = m.player0Visibility[i][j];
                player1Visibility[i][j] = m.player1Visibility[i][j];
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
        initDockingZone();
        
        player0Visibility = new boolean[WIDTH][HEIGHT];
        player1Visibility = new boolean[WIDTH][HEIGHT];

        updateRadarVisibilityArrays();
    }
    
    private void initDockingZone(){
        Vector2 d0 = new Vector2(0,9);
        Vector2 d1 = new Vector2(0,20);
        Vector2 d2 = new Vector2(29,9);
        Vector2 d3 = new Vector2(29,20);
        dockingZone.add(d0);
        dockingZone.add(d1);
        dockingZone.add(d2);
        dockingZone.add(d3);
        Vector2 d;
        int i = 1;
        for (int j = 10; j < 20; j++){
            d = new Vector2(i,j);
            dockingZone.add(d);
        }
        i = 28;
        for (int j = 10; j < 20; j++){
            d = new Vector2(i,j);
            dockingZone.add(d);
        }        
        
    }
   
    /**
     * This method gets all availableMoves generated by the ship and remove the
     * positions blocked by obstacle.
     * @param ship The ship to be moved.
     * @return An array of positions that are highlighted on the map.
     */
    public Positions prepareMoveShip(Ship ship){
        Positions highlightedMoves = new Positions(null,null,null,null);        
        if (ship.getShipType().compareTo(Ship.ShipType.KamikazeBoat)==0){
            highlightedMoves = prepareMoveKam(ship);
            return highlightedMoves;
        }         
        Positions allMoves = ship.availableMoves(); 
        //not sure if it's a good idea.               
        //if there is any obstacle on left or right, the ship can't move sideways.
        ArrayList<Vector2> left = allMoves.getLeft();
        boolean canMove = true;
        if (left != null){
            for (int i = 0; i < left.size(); i++) {
                if (isVisibleObstacle(ship, left.get(i))) {
                    canMove = false;
                }
            }
            if (canMove) {
                highlightedMoves.setLeft(left);
            }
        }
        ArrayList<Vector2> right = allMoves.getRight();
        canMove = true;
        if (right != null){
            for (int i = 0; i < right.size(); i++) {
                if (isVisibleObstacle(ship, right.get(i))) {
                    canMove = false;
                }
            }
            if (canMove) {
                highlightedMoves.setRight(right);
            }
        }      
        ArrayList<Vector2> back = allMoves.getBackward();
        if (back != null){
            //maybe not necessary but just in case the rule changes.
            ArrayList<Vector2> validBack = new ArrayList<Vector2>();
            for (int i = 0; i < back.size(); i++) {
                if (!isSelf(ship, back.get(i))) {
                    if (!isVisibleObstacle(ship, back.get(i))) {
                        validBack.add(back.get(i));
                    }
                } else {
                    validBack.add(back.get(i));
                }
            }
            highlightedMoves.setBack(validBack);
        }
        // if there is an obstacle in front, the ship can't move beyond that obstacle.
        ArrayList<Vector2> forward = allMoves.getForward();
        if (forward != null){
            ArrayList<Vector2> validForward = new ArrayList<Vector2>();
            for (int i = 0; i < forward.size(); i++) {
                if (!isSelf(ship, forward.get(i))) {
                    if (!isVisibleObstacle(ship, forward.get(i))) {
                        validForward.add(forward.get(i));
                    } else if (isVisibleObstacle(ship, forward.get(i))) {
                        break;
                    } else {
                        //shouldn't happen
                    }
                } else {
                    validForward.add(forward.get(i));
                }
            }
        highlightedMoves.setForward(validForward);   
        }
        return highlightedMoves;
    }
    /**
     * A special method to prepare moves for the KamikazeBoat.
     * @param ship
     * @return 
     */
    private Positions prepareMoveKam(Ship ship){
        Positions highlightedMoves = new Positions(null,null,null,null);
        ArrayList<Vector2> moves = ship.availableMovesKam();
        ArrayList<Vector2> forward = new ArrayList<Vector2>();
        if (moves.size() > 0){
            for (int i = 0; i < moves.size(); i++){
                if(!isVisibleObstacle(ship, moves.get(i)) && !isSelf(ship,moves.get(i))){
                    forward.add(moves.get(i));
                }
            }
            // since we don't need to differentiate different directions, just set
            //to forward as default.           
            highlightedMoves.setForward(forward);
        }
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
    public boolean moveShip(Ship ship,Vector2 newPosition, Positions p) {
        boolean found = false;
        for(Vector2 v: p.getAll()) {
            if(v.equals(newPosition)) {
                found = true;
            }
        }
        if(!found) {
            return false;
        }
        if (ship.getShipType().compareTo(Ship.ShipType.KamikazeBoat)== 0){
            boolean b = moveShipKamikaze(ship, newPosition);
            return b;
        }        
        Moves shipPositions = getMovePositions (newPosition, p);
        ArrayList<Vector2> valid = new ArrayList<Vector2>();
        valid = validateMove(ship, shipPositions);
        if (valid != null){
            this.updateShipPositions(ship, valid);    
            ship.moveTo(valid);           
        } else {
            return false;
        }       
        this.updateRadarVisibilityArrays();
        return true;
    }
    private boolean moveShipKamikaze(Ship ship, Vector2 p){
        Vector2 mine;
        ShipUnit[] damagedUnits = new ShipUnit[2];
        if (isMine(p) || isMineZone(p)) {
            mine = p;
            damagedUnits[0] = ship.getShipUnits()[0];
            damagedUnits[1] = null;
            touchMine(mine, damagedUnits); 
            return false;
        }else{
            ArrayList<Vector2> positions = new ArrayList<Vector2>();
            positions.add(p);
            this.updateShipPositions(ship, positions);
            ship.moveToKam(p);
            this.updateRadarVisibilityArrays();
            return true;
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
     * if there is a mine, touchMine is called. 
     * @param p The array of positions to validate.
     * @return The new positions that the ship will be moved to. It would be the 
     * same than the input is all positions are clear.
     */
    public ArrayList<Vector2> validateMove(Ship s, Moves p){
        //MAKE SURE THE 1ST IN THE RETURED ARRAY IS THE POSITION OF THE BOW OF THE SHIP.
        // to remember the position where an obstacle or mine is encountered.
        ArrayList<Vector2> moves = p.getPositions();

        ShipUnit[] shipUnits = s.getShipUnits();
        Vector2 obstacle, mine; 
        int shipSize = s.getSize();
        int i, count;
        ShipUnit[] damagedUnits = new ShipUnit[2];
        if (p.getMoveDirection() == null){
            Logger.getLogger(Map.class.getName()).log(Level.SEVERE, null, 
                    new GameException("no move direction, something's wrong."));            
        }else{
            switch (p.getMoveDirection()) {
                case F:
                    System.out.println(" FORWARD ");
                    // stores all valid positions between ship bow to target.
                    ArrayList<Vector2> forwardmoves = new ArrayList<Vector2>();
                    //need to generate new positions if needed.
                    for (Vector2 v : moves) {
                        if (isHiddenObstacle(s, v)) {
                            break;
                        }
                        if (isMine(v)){
                            if(s.getShipType().compareTo(Ship.ShipType.MineLayer) == 0){
                                break;
                            }else{
                                mine = v;
                                damagedUnits[0] = shipUnits[0];
                                damagedUnits[1] = shipUnits[1];
                                touchMine(mine, damagedUnits);
                                forwardmoves.add(v);
                                break;                                  
                            }
                        }else if (isMineZone(v)) {
                            if (s.getShipType().compareTo(Ship.ShipType.MineLayer) == 0){
                                forwardmoves.add(v);
                            }else{
                                mine = v;
                                damagedUnits[0] = shipUnits[0];
                                damagedUnits[1] = shipUnits[1];
                                touchMine(mine, damagedUnits);
                                forwardmoves.add(v);
                                break;  
                            }
                        }else{
                            forwardmoves.add(v);
                        }
                    }
                    
                    moves.clear();
                    if (forwardmoves.size() > 0){
                        i = forwardmoves.size()-1;
                        int k = shipSize;
                        while(k > 0){
                            moves.add(forwardmoves.get(i));
                            k--;
                            i--;
                       }
                    }
                    for (Vector2 v: moves){
                        System.out.println(" VALID moves " + v.x + " " + v.y);
                    }
                    break;
                case B:
                    for (Vector2 v : moves) {
                        if (isHiddenObstacle(s, v)) {
                            return null;
                        }
                        if (isMine(v)){
                            if(s.getShipType().compareTo(Ship.ShipType.MineLayer) == 0){
                                return null;
                            }// can;t move backward into a mine.
 
                        }else if (isMineZone(v)) {
                            if(s.getShipType().compareTo(Ship.ShipType.MineLayer) == 0){
                                return moves;
                            }else{    
                                mine = v;
                                damagedUnits[0] = shipUnits[shipUnits.length-2];
                                damagedUnits[1] = shipUnits[shipUnits.length-1];
                                touchMine(mine, damagedUnits);         
                                return null; 
                            }
                             
                        }
                    }
                case L:
                    count = 0;
                    for (Vector2 v : moves) {
                        if (isHiddenObstacle(s, v)) {
                            return null;
                        }
                        if (isMine(v)) {
                            if(s.getShipType().compareTo(Ship.ShipType.MineLayer) == 0){
                                return null;
                            }else{    
                                mine = v;
                                damagedUnits[0] = shipUnits[count];
                                damagedUnits[1] = shipUnits[count+1];
                                touchMine(mine, damagedUnits);   
                                return null;
                            }
                        }else if (isMineZone(v)){
                            if(s.getShipType().compareTo(Ship.ShipType.MineLayer) == 0){
                                return moves;
                            }else{
                                mine = v;
                                damagedUnits[0] = shipUnits[count];
                                damagedUnits[1] = shipUnits[count+1];
                                touchMine(mine, damagedUnits);                                   
                                return null;
                            }
                        }else{
                           count++;
                        }
                    }
                    break;
                case R:
                    count = 0;
                    for (Vector2 v : moves) {
                        if (isHiddenObstacle(s, v)) {
                            return null;
                        }
                        if (isMine(v)) {
                            if(s.getShipType().compareTo(Ship.ShipType.MineLayer) == 0){
                                return null;
                            }else{
                                mine = v;
                                damagedUnits[0] = shipUnits[count];
                                damagedUnits[1] = shipUnits[count+1];
                                touchMine(mine, damagedUnits);
                                return null;
                            }
                        }else if (isMineZone(v)){
                            if(s.getShipType().compareTo(Ship.ShipType.MineLayer) == 0){
                                return moves;
                            }else{                            
                                mine = v;
                                damagedUnits[0] = shipUnits[count];
                                damagedUnits[1] = shipUnits[count+1];
                                touchMine(mine, damagedUnits);   
                                return null;
                            }
                         }else{
                           count++;
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
        
        ArrayList<Vector2> leftPath = allTurns.getLeftPath();
        ShipDirection ld = allTurns.getLeftDirection();
        boolean canMoveLeft = true;
        if(left != null && leftPath != null){       
            for (int i = 0; i < left.size(); i++) {
                if (!isSelf(ship, left.get(i))) {
                    if (isVisibleObstacle(ship, left.get(i))) {
                        canMoveLeft = false;                            
                    }
                }
            }
            for (int i = 0; i < leftPath.size(); i++) {
                if (isVisibleObstacle(ship, leftPath.get(i))) {
                    canMoveLeft = false;
                }
            }
            if (canMoveLeft) {
                highlightedTurns.setLeft(left);
                highlightedTurns.setLeftPath(leftPath);
                highlightedTurns.setLeftDirection(ld);
            }
        }
        ArrayList<Vector2> right = allTurns.getRight();
        ArrayList<Vector2> rightPath = allTurns.getRightPath();
        ShipDirection rd = allTurns.getRightDirection();
  
        boolean canMoveRight = true;
        if (right != null && rightPath != null){       
            for (int i = 0; i < right.size(); i++) {
                if (!isSelf(ship, right.get(i))) {
                    if (isVisibleObstacle(ship, right.get(i))) {
                        canMoveRight = false;
                    }
                }
            }
            
            for (int i = 0; i < rightPath.size(); i++) {
                if (isVisibleObstacle(ship, rightPath.get(i))) {
                    canMoveRight = false;
                }
            }
            if (canMoveRight) {
                highlightedTurns.setRight(right);
                highlightedTurns.setRightPath(rightPath);
                highlightedTurns.setRightDirection(rd);
            }
        }
        ArrayList<Vector2> back = allTurns.getBackward();
        ShipDirection bd = allTurns.getBackDirection();
        if (back != null){
            if (canMoveLeft && canMoveRight) {
                highlightedTurns.setBack(back);
                highlightedTurns.setBackDirection(bd);
            }
        }
        // careful in Game, path are NOT highlighted. 
        return highlightedTurns;
    }
    
    public boolean turnShip(Ship ship, Vector2 newPosition, TurnPositions p){
        boolean found = false;
        for(Vector2 v: p.getAll()) {
            if(v.equals(newPosition)) {
                found = true;
            }
        }
        if(!found) {
            return false;
        }      
        Turns shipPositions = getTurnPositions (newPosition, p);     
        ArrayList <Vector2> t = shipPositions.positions;
        if (t == null){
            return false;
        }

        Turns validTurns = validateTurn(ship, shipPositions);
        // find the new ship direction
        ArrayList <Vector2> valid = validTurns.getTurns();
        ShipDirection newDirection = validTurns.getNewDirection();
        if (valid != null){
            this.updateShipPositions(ship, valid);    
            ship.turnTo(valid, newDirection);           
        }        
        
        updateRadarVisibilityArrays();
        return true;
    }
    
    /**
     * @param ship
     * @return The positions of every ship unit in this ship.
     */
    public static ArrayList<Vector2> getShipPositions(Ship ship) {
        ShipUnit[] units = ship.getShipUnits();
        ArrayList<Vector2> list = new ArrayList<Vector2>();
        for(ShipUnit s: units) {
            list.add(new Vector2(s.getPosition()));
        }
        return list;
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
            for (Vector2 v : left){
                if (v.equals(p)){
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
            for (Vector2 v : right){
                if (v.equals(p)) {
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
                if (v.equals(p)) {
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

        for (Vector2 v: turnPath){
            if (isHiddenObstacle(s,v)){
                canTurn = false;
                break; // don't turn if there is an obstacle.
            /*don't need to check for mine because turning always touches a mine 
                zone first. */
            }else if (isMineZone(v)){
                mine = v;
                damagedUnits = getDamagedUnits(mine,s);
                touchMine(mine, damagedUnits);
                canTurn = false;
                break;
            }
        }
        //keep a count of cleared positions.
        int count = -1;
        for (Vector2 v: turns){
            if (isHiddenObstacle(s,v)){
                canTurn = false;
                break; // don't turn if there is an obstacle.
            }else if (isMineZone(v)){
                if(s.getShipType().compareTo(Ship.ShipType.MineLayer) != 0){
                    mine = v;
                    ShipUnit s1,s2;
                    if (count == s.getSize()-1){
                        s1 = s.getShipUnits()[count-1];
                        s2 = s.getShipUnits()[count];
                    }else{
                        s1 = s.getShipUnits()[count];
                        s2 = s.getShipUnits()[count+1];
                    }
                    damagedUnits[0] = s1;
                    damagedUnits[1] = s2;
                    touchMine(mine, damagedUnits);
                    canTurn = false;
                    break;
                }else{ // minelayer
                //    canTurn = true;
                //    break;
                    System.out.println ("can STILL turn");
                }
            }else{
                //if there is nothing, increment count
                count++;
            }
        }            
        if (canTurn){
            valid.setTurns(turns);
            valid.setPath(turnPath);
            valid.setNewDirection(d);
        } 
        return valid;
    }

    /**
     * 
     * @param minePosition
     * @param s
     * @param turnPath True if the mine is on turn path, false if the mine is at 
     * the new position of the ship.
     * @return 
     */
    private ShipUnit[] getDamagedUnits(Vector2 minePosition, Ship s){
        ShipUnit[] shipUnits = s.getShipUnits();
        ShipUnit[] damagedUnits = new ShipUnit[2];  
        ShipUnit s1 = new ShipUnit();
        ShipUnit s2 = new ShipUnit();
        int size = s.getSize();
        if (s.hasFlexibleTurn()){
            Vector2 p0 = shipUnits[0].getPosition();
            Vector2 p2 = shipUnits[shipUnits.length-1].getPosition();
            if (minePosition.x == p0.x || minePosition.y == p0.y){
                s1 = shipUnits[0];
                s2 = shipUnits[1];
            }else if (minePosition.x == p2.x || minePosition.y == p2.y){
                s1 = shipUnits[1];
                s2 = shipUnits[2];
            }else{
                //shouldn't happen
            }
        }else{
            Vector2 pivot = shipUnits[shipUnits.length-1].getPosition();
            int distance = getDistance(pivot,minePosition);
            s1 = shipUnits[size-distance];
            s2 = shipUnits[size-distance+1];
        }
        
        damagedUnits[0] = s1;
        damagedUnits[1] = s2;
        return damagedUnits;
    }
    
    /**
     * This method returns the Euclidean distance between 2 positions.
     * It's used by validateTurn to figure out the ship units to be destroyed by
     * the mine.
     */
    private int getDistance(Vector2 v1, Vector2 v2){
        int distance = Math.abs(v1.x-v2.x) + Math.abs(v1.y-v2.y);
        return distance;
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
            ShipUnit su = (ShipUnit)o;
            if (su.getShip().getPlayerID()== s.getPlayerID()){
                isVisibleObstacle = true;
            }
            ArrayList<Vector2> visible = s.getRadarPositions();            
            for (Vector2 v: visible){
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
                        System.out.println("MINELAYER can see mine");
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
   
    public boolean isMineZone(Vector2 p){
        Vector2 v;
        Vector2[] minezone;
        ArrayList<Vector2> allMinezone = new ArrayList<Vector2>();
        for (int i = 0; i < WIDTH; i++){
            for (int j = 0; j < HEIGHT; j++){
                v = new Vector2(i,j);
                GameObject o = getObjectAt(v);
                if (o != null){
                    if (o.getObjectType().compareTo(GameObject.GameObjectType.Mine) == 0){
                        Mine m = (Mine)o;
                        minezone = m.getMineZone();
                        for (Vector2 zone : minezone){
                            allMinezone.add(zone);
                        }
                    }
                }
            }
        }
        boolean isMineZone = false;
        for (Vector2 mz: allMinezone){
            if (mz.equals(p)){
                isMineZone = true;
            }            
        }

        return isMineZone;
    }    

    
    /**
     * @param position 
     * @return True if at the specified position there is a ship, otherwise false.
     */
    public boolean isShip(Vector2 position) {
        return this.getObjectAt(position) instanceof ShipUnit;
    }
    
    /**
     * If there is no object at the specified position in the grid, isClear()
     * returns true. Otherwise, it returns false.
     * @param position
     * @return 
     */
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
    
    /**
     * This function is an interface for Map to access the concrete attack action; as a controller Map takes two inputs
     * then finds out what ship type the attacker is and get the GameObject at the specific location; then it invokes the
     * specific fireCannon function; finally Map checks if the target is a mine and if so Map removes it.
     * @param attacker = user selected ship that perform the attack operation
     * @param position = the aiming grid on the Map where the target lies
     * @return The ship which is hit by the cannon, or null if no ship was hit.
     */
    public GameObject cannonAttack(Ship attacker, Vector2 position){
    	GameObject target = getObjectAt(position);
    	
    	if(attacker.getClass() == new Cruiser(10000).getClass()){
            return((Cruiser) attacker).fireCannon(target);
                
    	}
    	else if(attacker.getClass() == new TorpedoBoat(10000).getClass()){
    		return ((TorpedoBoat) attacker).fireCannon(target);
    	}
    	else if(attacker.getClass() == new Destroyer(10000).getClass()){
    		return ((Destroyer) attacker).fireCannon(target);
    	}
    	else if(attacker.getClass() == new MineLayer(10000).getClass()){
    		return ((MineLayer) attacker).fireCannon(target);
    	}
    	else if(attacker.getClass() == new RadarBoat(10000).getClass()){
    		return ((RadarBoat) attacker).fireCannon(target);
    	}
    	else if (target.getClass() == new Mine().getClass()){
    		setObjectAt(position, null);
    	}
        return null;
    }
    
    public GameObject layMine(Ship mineLayer, Vector2 position) {
    	if(mineLayer.getClass() != new MineLayer(10000).getClass())
    		return null;
    	
    	
    	if(getObjectAt(position) != null)
    		return null;
    	
    	Mine mine = new Mine();
    	mine.setPosition(position);
    	
    	Vector2[] zone = mine.getMineZone();
    	int count = 0;
    	for(Vector2 temp: zone) {
            if(getObjectAt(temp) != null)              
                    count++;
    	}
    	
    	if(count > 1)
    		return null;
    	
    	mine.setActive(true);
    	setObjectAt(position, mine);  	
    	return mine;
    }
    
    public Vector2[] getFilteredMineDropZone(Ship mineLayer){
    	ArrayList<Vector2> zone = new ArrayList<Vector2>();
    	
    	if(mineLayer.getClass() != new MineLayer(10000).getClass())
    		return null;
    	
    	for(Vector2 pos: ((MineLayer)mineLayer).getMineDropPickupZone()) {
    		if(getObjectAt(pos) != null)
        		continue;	
        	
    	Mine mine = new Mine();
        mine.setPosition(pos);
        int count = 0;
        	
       	for(Vector2 temp: mine.getMineZone()) {
       		if(getObjectAt(temp) != null)              
       		count++;
        }
        	
        if(count <= 1)
        	zone.add(pos);
    	}
    	
    	Vector2[] result = new Vector2[zone.size()];
    	for(int i = 0; i < zone.size(); i++){
    		result[i] = zone.get(i);
    	}
    	
    	return result;
    }
    
    public void touchMine(Vector2 m, ShipUnit[] damagedUnits){
        GameObject temp = getObjectAt(m);
        //assume we can get the actual mine which is at the center of this mineZone,
        // and we destroyed it.
        for (ShipUnit s: damagedUnits){
            s.setDamage(1);
        }
        
        if(temp!= null && temp.getClass() == new Mine().getClass()){
            ((Mine)temp).setDestoryed(true);
            setObjectAt(m, null);
         /*   for(Vector2 mz: ((Mine)temp).getMineZone()){
        	setObjectAt(mz, null);
            }
       */
        }
        else{ //temp is a MineZone,which is not a GameObject
            int x = m.x;
            int y = m.y;
            Mine mine = null;
            Vector2[] possibleZone =  {new Vector2(x, y-1), new Vector2(x, y+1), new Vector2(x-1, y), new Vector2(x+1, y)};
            for(Vector2 pos: possibleZone){
            	GameObject possibleMine = getObjectAt(pos);
            	if(possibleMine != null && possibleMine.getClass() == new Mine().getClass()){
            		mine = (Mine)possibleMine;
            	}
            }
            
            mine.setDestoryed(true);
            setObjectAt(m, null);
            Vector2 realMine = mine.getPosition();
            setObjectAt(realMine, null);
        /*    for(Vector2 mz: mine.getMineZone()){
        	setObjectAt(mz, null);
            }
    */    }
    }
    
    public void pickupMine(MineLayer mineLayer, Vector2 pos) {
    	Mine mine = (Mine)getObjectAt(pos);
    	mineLayer.pickupMine(mine);
    	setObjectAt(pos, null);
    }
    
    /**
     * loops through all ship units of both players, and return false as soon
     * as it finds a ship unit that is not destroyed. called by GameState after 
     * each turn.
     * @return 
     */
    public boolean isEndGame(){
        boolean gameOver = false;
        boolean defeatedP0 = true;
        boolean defeatedP1 = true;
        for (Ship s0: player0Ships){
            ShipUnit[] shipUnits = s0.getShipUnits();
            for (ShipUnit su: shipUnits){
                if (!su.isDestroyed()){
                    defeatedP0 = false;
                    break;
                }
            }
        }
        if (defeatedP0){
            gameOver = true;;
        }else{
            for (Ship s1: player1Ships){
                ShipUnit[] shipUnits = s1.getShipUnits();
                for (ShipUnit su: shipUnits){
                    if (!su.isDestroyed()){
                        defeatedP1 = false;
                        break;
                    }
                }
            }
            if (defeatedP1){
                gameOver = true;
            }
        }
        return gameOver;
    }
    
    // checks if a ship is docked at the base. called for ship repair.
    public boolean isDocked(Ship s){
        boolean docked = false;
        ShipUnit[] shipUnits = s.getShipUnits();
        ArrayList<Vector2> ship = new ArrayList<Vector2>();
        for (ShipUnit su: shipUnits){
            ship.add(su.getPosition());
        }
        for (Vector2 v: ship){
            for (Vector2 docking: dockingZone){
                if(v.equals(docking)){
                    docked = true;
                    break;
                }
            }
        }
        return docked;
    }
    /**
     * Checks whether the specified ship belongs to the blue player (player on the
     * west side of the map, a.k.a. player0). If it does, returns true, otherwise
     * returns false.
     * @param s
     * @return 
     */
    public boolean isBlue(Ship s) {
        //player0 is blue
        boolean found = false;
        for(int i = 0; i < player0Ships.length && !found; i++) {
            if(player0Ships[i] == s) {
                found = true;
            }
        }
        return found;
    }
    
    /**
     * Checks whether the specified base belongs to the blue player (player on the
     * west side of the map, a.k.a. player0). If it does, returns true, otherwise
     * returns false.
     * @param b
     * @return 
     */
    public boolean isBlue(Base b) {
        return b == p0Base;
    }
    
    /**
     * Checks whether the specified ship belongs to the red player (player on the
     * east side of the map, a.k.a. player1). If it does, returns true, otherwise
     * returns false.
     * @param s
     * @return 
     */
    public boolean isRed(Ship s) {
        return !isBlue(s);
    }
    
    /**
     * Returns the radar visibility array for the player with the specified index.
     * @param playerIndex
     * @return 
     */
    public boolean[][] getRadarVisibility(int playerIndex) {
        switch(playerIndex) {
            case 0:
                boolean[][] newArray = copyArray(player0Visibility);
                return newArray;
            case 1:
                boolean[][] newArray1 = copyArray(player1Visibility);
                return newArray1;
            default:
                Logger.getLogger(Map.class.getName()).log(Level.SEVERE, null,                         
                        new GameException("Unknown player index: " + playerIndex));
                break;
        }
        return null;
    }
    
    /**
     * Returns the sonar visibility array for the player with the specified index.
     * @param playerIndex
     * @return 
     */
    public boolean[][] getSonarVisibility(int playerIndex) {
        boolean[][] map = new boolean[WIDTH][HEIGHT];
        switch(playerIndex) {
            case 0:
                for(Ship s: player0Ships) {
                    if(s.getShipType().equals((Ship.ShipType.MineLayer))) {
                        //add the mine layer's radar range as sonar range
                        for(Vector2 v: s.getRadarPositions()) {
                            map[v.x][v.y] = true;
                        }
                    }
                }
                return map;
            case 1:
                for(Ship s: player1Ships) {
                    if(s.getShipType().equals((Ship.ShipType.MineLayer))) {
                        //add the mine layer's radar range as sonar range
                        for(Vector2 v: s.getRadarPositions()) {
                            map[v.x][v.y] = true;
                        }
                    }
                }
                return map;
            default:
                Logger.getLogger(Map.class.getName()).log(Level.SEVERE, null,                         
                        new GameException("Unknown player index: " + playerIndex));
                break;
        }
        return null;
    }
    
    public static boolean[][] copyArray(boolean[][] array) {
        boolean[][] newArray = new boolean[array.length][array[0].length];
        for(int x = 0; x < newArray.length; x++) {
            for(int y = 0; y < newArray[0].length; y++) {
                newArray[x][y] = array[x][y];
            }
        }
        return newArray;
    }
    
    /**
     * This function is an interface for Map to access the concrete attack action; as a controller Map takes two inputs
     * then finds out what ship type the attacker is and get the GameObject at the specific location; then it invokes the
     * specific fireCannon function; finally Map checks if the target is a mine and if so Map removes it.
     * @param attacker = user selected ship that perform the attack operation
     * @param position = the aiming grid on the Map where the target lies 
     */
    public void torpedoAttack(Ship attacker, Vector2 position){
    	
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
    	
    	Vector2 pos = null; 
    	for (Vector2 vec : torpedoRange){
    		target = getObjectAt(vec);
    		if(target == null)
    			continue;
    		
    		if(target.getClass() == new ShipUnit().getClass() ||
    		           target.getClass() == new BaseUnit().getClass() ||
    		           target.getClass() == new Mine().getClass()) {
    			pos = vec;
    			break;
    		}	
    	}
    	
    	if(target == null){
    		return;
    	}
    	
    	if(attacker.getClass() == new TorpedoBoat(10000).getClass()){
    		((TorpedoBoat) attacker).fireTorpedo(target);
    	}
    	else if(attacker.getClass() == new Destroyer(10000).getClass()){
    		((Destroyer) attacker).fireTorpedo(target);	
    	}
    	else return;
    	
    	if (target.getClass() == new Mine().getClass()){
    		setObjectAt(pos, null);
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
                    ShipUnit su = (ShipUnit)getObjectAt(new Vector2(x,y));
                    if (su.isDamaged() || su.isDestroyed()){
                        sb.append("D");
                    }else if (su.isHealthy()){
                        sb.append("H");
                    }
                } else if(grid[x][y] instanceof BaseUnit) {
                    sb.append("B");                    
                } else if(grid[x][y] instanceof Mine) {
                    sb.append("M");
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }
    

    /**
     * Recalculates the visibility of every grid cell for every player and
     * saves the results in player0Visibility and player1Visibility.
     */
    public void updateRadarVisibilityArrays() {
        player0Visibility = generateRadarVisibility(player0Ships);
        player1Visibility = generateRadarVisibility(player1Ships);
        
        for(BaseUnit u: this.p0Base.getBaseUnits()) {
            //make visible every grid cell around the base unit
            Vector2 pos = u.getPosition();
            makeVisibleAllNeighbours(pos, player0Visibility);
            //add only the base blocks as visible to the other player
            player1Visibility[pos.x][pos.y] = true;
            
        }

        for(BaseUnit u: this.p1Base.getBaseUnits()) {
            //make visible every grid cell around the base unit
            Vector2 pos = u.getPosition();
            makeVisibleAllNeighbours(pos, player1Visibility);
            //add only the base blocks as visible to the other player
            player0Visibility[pos.x][pos.y] = true;
        }
        //add all obstacles as visible
        for(int x = 0; x < WIDTH; x++) {
            for(int y = 0; y < HEIGHT; y++) {
                if(grid[x][y] instanceof CoralUnit) {
                    //make the corals visible
                    player0Visibility[x][y] = true;
                    player1Visibility[x][y] = true;
                }
            }
        }
    }

    /**
     * Returns a grid with booleans for every cell determining whether the cell
     * is in the radar range of one or more ships in the playerShips list.
     * @param playerShips
     * @return 
     */
    public static boolean[][] generateRadarVisibility(Ship[] playerShips) {
        boolean[][] array = new boolean[WIDTH][HEIGHT];
        
        for(Ship s: playerShips) {
            ArrayList<Vector2> visibleCells = s.getRadarPositions();
            for(Vector2 point: visibleCells) {
                try {
                    array[point.x][point.y] = true;
                } catch(ArrayIndexOutOfBoundsException ignore) {}
            }
            for(ShipUnit su: s.getShipUnits()) {
                try {
                    array[su.getPosition().x][su.getPosition().y] = true;
                } catch(ArrayIndexOutOfBoundsException ignore) {}
            }
        }
        return array;
    }

    /**
     * A small helper method which sets to true all flags inside and around
     * the given position.
     * @param pos
     * @param player0Visibility 
     */
    private static void makeVisibleAllNeighbours(Vector2 pos, boolean[][] player0Visibility) {
        try {
            player0Visibility[pos.x][pos.y] = true;
        } catch(IndexOutOfBoundsException ignore){}
        try {
            player0Visibility[pos.x - 1][pos.y] = true;
        } catch(IndexOutOfBoundsException ignore){}
        try {
            player0Visibility[pos.x + 1][pos.y] = true;
        } catch(IndexOutOfBoundsException ignore){}
        try {
            player0Visibility[pos.x][pos.y - 1] = true;
        } catch(IndexOutOfBoundsException ignore){}
        try {
            player0Visibility[pos.x][pos.y + 1] = true;
        } catch(IndexOutOfBoundsException ignore){}
    }

    /**
     * Returns the coordinates of the specified object if it is found in
     * the grid.
     * @param targetHit
     * @return 
     */
    public Vector2 objectCoordinates(GameObject object) {
        boolean found = false;
        for(int x = 0; x < WIDTH && ! found; x++) {
            for(int y = 0; y < HEIGHT && !found; y++) {
                if(grid[x][y] == object) {
                    return new Vector2(x, y);
                }
            }
        }
        return null;
    }

    /**
     * Copy the GameObjects from the provided map to this map within the specified
     * region between minX,minY and maxX,maxY.
     * @param map Map whose grid will be copied.
     * @param minX 
     * @param minY
     * @param maxX
     * @param maxY 
     */
    void setGrid(Map map, int minX, int minY, int maxX, int maxY) {
        for(int x = minX; x <= maxX; x++) {
            for(int y = minY; y <= maxY; y++) {
                this.grid[x][y] = map.grid[x][y];
            }
        }
    }

    public boolean isCoral(Vector2 position) {
        GameObject o = grid[position.x][position.y];
        if(o == null) {
            return false;
        } else {
            return o.getObjectType().equals(GameObject.GameObjectType.CoralReef);
        }
    }
}
