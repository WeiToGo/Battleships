/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my_game.models.game_components;
import java.util.ArrayList;

import my_game.util.Range;
import my_game.util.Vector2;
import my_game.util.Positions;
import my_game.util.TurnPositions;

/**
 *
 */
public abstract class Ship implements java.io.Serializable {
    
    public enum ShipType {
        Cruiser, Destroyer, TorpedoBoat, MineLayer, RadarBoat
    };
    
   
    /** The attributes of Ship class. */
    private ShipType shipType;
    private ShipUnit[] shipUnits;
    private final int playerID;
    private int size;
    private int speed;
    private int currentSize;
    private int currentSpeed;
    private int cannonDamage;
	private int armour;
    private ShipDirection direction; 
    private int destoryedUnit;
    protected ArrayList<Vector2> visiblePositions;    
    protected ArrayList<String> weapons = new ArrayList<String>();
    /** The cannon range for a ship facing East as default. */
    protected Range cannonRange;
    /** The torpedo range for a ship facing East as default. */
    protected Range torpedoRange;
    /** The radar range for a ship facing East as default. */
    protected Range radarRange;
    
    
    /** Constructs a ship given a player ID. */
    public Ship(int pid){
        this.playerID = pid;
    }
    
    public int getCannonDamage() {
		return cannonDamage;
	}

	public void setCannonDamage(int cannonDamage) {
		this.cannonDamage = cannonDamage;
	}
    
    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getCurrentSize() {
	return currentSize;
    }

    public void setCurrentSize(int currentSize) {
        this.currentSize = currentSize;
    }
    
      
    /**
     * This method is called so Map can calculate all the legal moves for this 
     * ship.
     * @return 
     */
    public int getCurrentSpeed(){
        return currentSpeed;
    }
    
    public void setCurrentSpeed(int speed){
        currentSpeed = speed;
    }
   
    
    public ShipDirection getDirection(){
        return this.direction;
    }
    
    /**
     * This method is called after every turn to reflect the current direction
     * the ship is facing.
     * @param d The new direction. 
     */
    public void setDirection(ShipDirection d){
        this.direction = d;
    }

    public int getArmour() {
	return armour;
    }

    public void setArmour(int armour) {
	this.armour = armour;
    }

    public ArrayList<String> getWeapons() {
        return weapons;
    }

    public void setWeapons(ArrayList<String> weapons) {
	this.weapons = weapons;
    }
    
    public int getDestoryedUnit() {
		return destoryedUnit;
	}

	public void setDestoryedUnit(int destoryedUnit) {
		this.destoryedUnit = destoryedUnit;
	}

	public int getPlayerID() {
	return playerID;
    }

    /**
    * @return The type of this ship.
    */
    public ShipType getShipType() {
        return this.shipType;
    }
	
    public void setShipType(ShipType shipType) {
	this.shipType = shipType;
    }
	
    public  ShipUnit[] getShipUnits(){
        return this.shipUnits;
    }

    public void setShipUnits(ShipUnit[] shipUnits) {
    	this.shipUnits = shipUnits;
    }
    public Range getCannonRange() {
    	return cannonRange;
    }

    public Range getTorpedoRange() {
    	return torpedoRange;
    } 
    
    public Range getRadarRange() {
    	return radarRange;
    }    

    public void setCannonRange(Range r){
        this.cannonRange = r;
    }
    
    public void setTorpedoRange(Range r){
        this.torpedoRange = r;
    }
    
    public void setRadarRange(Range r){
        this.radarRange = r;
    }

    /**
     * This method should be called after each attack on the ship.
     */
    public void hitUpdate(){
        currentSize--;    
        setCurrentSpeed(currentSpeed - speed/size);
    }
    public ArrayList<Vector2> getRadarPositions(){
        Range r = this.getRadarRange();
        ArrayList<Vector2> visible = getRangePositions(r);
        return visible;
    }
    public ArrayList<Vector2> getCannonPositions(){
        Range r = this.getCannonRange();
        ArrayList<Vector2> visible = getRangePositions(r);
        return visible;
    }    
    
    /**
     * 
     * @param target = the target GameObject 
     */
    public GameObject fireCannon(GameObject target) {
        GameObject objectHit = null;
        if(target == null) {
            return null;
        }
    	if (target.getClass() == new ShipUnit().getClass()){
			ShipUnit su = ((ShipUnit)target);
                        su.setDamage(getCannonDamage());
                        objectHit = su;
			if (((ShipUnit)target).isDestoryed()){
				((ShipUnit)target).getShip().hitUpdate();
			}
		}
		
		if (target.getClass() == new BaseUnit().getClass()){
                    BaseUnit bu = ((BaseUnit)target);
			bu.setDamage();
                        objectHit = bu;
		}
		
		if (target.getClass() == new Mine().getClass()){
                    Mine m = ((Mine)target);
			m.setDestoryed(true);
                        objectHit = m;
		}
                return objectHit;
    }
    
    /**
     * This method gets an arraylist of positions that is visible within the 
     * ship's radar or canon range. (excluding the positions of the ship itself).
     * @return 
     */
    private ArrayList<Vector2> getRangePositions(Range r){
        Vector2 tl = r.getTopLeft();
        Vector2 tr = r.getTopRight();
        Vector2 br = r.getBottomRight();
        Vector2 bl = r.getBottomLeft();
        Vector2 newtl, newtr, newbr, newbl;
        Range newRange;
        ShipDirection d = this.getDirection();
        Vector2 shipPosition = this.getShipUnits()[0].getPosition();
        ArrayList<Vector2> visible = new ArrayList<Vector2>();   
        ArrayList<Vector2> filtered = new ArrayList<Vector2>();
        switch (d){
            case East:
                visible = this.generateRangePositions(r,shipPosition);
                break;
            //counter-clock
            case North: 
                newtl = new Vector2(-(bl.y), bl.x);
                newtr = new Vector2(-(tl.y), tl.x);
                newbr = new Vector2(-(tr.y), tr.x);
                newbl = new Vector2(-(br.y), br.x);
                newRange = new Range(newtl,newtr,newbr,newbl);                
                visible = this.generateRangePositions(newRange,shipPosition);
                break;
            //clock-wise    
            case South: 
                newtl = new Vector2(tr.y, -(tr.x));
                newtr = new Vector2(br.y, -(br.x));
                newbr = new Vector2(bl.y, -(bl.x));
                newbl = new Vector2(tl.y, -(tl.x));
                newRange = new Range(newtl,newtr,newbr,newbl);
                visible = this.generateRangePositions(newRange,shipPosition);
                break;
            //180 deg.
            case West: 
                newtl = new Vector2(-(br.x), -(br.y));
                newtr = new Vector2(-(bl.x), -(bl.y));
                newbr = new Vector2(-(tl.x), -(tl.y));
                newbl = new Vector2(-(tr.x), -(tr.y));
                newRange = new Range(newtl,newtr,newbr,newbl);                
                visible = this.generateRangePositions(newRange,shipPosition);
                break;
        } 
        filtered = filterRangePositions(visible);
        return visible;

    }
    /**
     * This method generates an arraylist of visible positions for this ship
     * (including the ship itself) given its radarRange.
     * @param r The visibility range
     * @param bowPosition
     * @return 
     */
    private ArrayList<Vector2> generateRangePositions(Range r, Vector2 bowPosition){
        Vector2 tl = r.getTopLeft();
        Vector2 tr = r.getTopRight();
        Vector2 bl = r.getBottomLeft();
        Vector2 sp = bowPosition;
       int xStart, xEnd, yStart, yEnd, i,j;
        ArrayList<Vector2> positions = new ArrayList<Vector2>();
        xStart = bowPosition.x + tl.x;
        xEnd = bowPosition.x + tr.x;
        yStart = bowPosition.y + tl.y;
        yEnd = bowPosition.y + bl.y;
/*        System.out.println("xStart " + xStart);
        System.out.println("xEnd" + xEnd);
        System.out.println("yStart " + yStart);
        System.out.println("yStart " + yEnd);
  */      
        for (i = xStart; i <= xEnd; i++){
            for (j = yStart; j <= yEnd; j++){
                Vector2 p = new Vector2(i,j);
                positions.add(p);
            }
        }
        
        return positions;
    }     
    /**
     * This method filters out the positions of the ship itself within the 
     * ship's radar range.
     * @param p Arraylist of all positions within the ship's radar range.
     * @return Filtered list.
     */
    private ArrayList<Vector2> filterRangePositions(ArrayList<Vector2> p){
        ArrayList<Vector2> filteredPositions = new ArrayList<Vector2>();
        ShipUnit[] shipUnits = this.getShipUnits();
        boolean isSelf;
        // maybe filter the position that are outside of the map?
        for (Vector2 v: p){
            isSelf = false;
            for (ShipUnit su: shipUnits){
                if (su.getPosition().equals(p)){
                    isSelf = true;
                }
            }
            if (!isSelf){
                filteredPositions.add(v);
            }
        }
        return filteredPositions;
    }
    /**
     * This method updates each ShipUnit to the new position, and it's called for
     * both move and turn actions.
     * @param An array of Vector2 indicating the new potions starting at the bow
     * of the ship.
     */
    public void moveTo(ArrayList<Vector2> newPosition){
        // assert newPosition.length == shipUnits.length
        // and assuming it's the right order.
        ShipUnit[] shipUnits = this.getShipUnits();
        int i = 0;
        int j = 0;
        while (i < newPosition.size() && j < shipUnits.length){
            ShipUnit su = shipUnits[i];
            newPosition.get(j);
            su.setPosition(newPosition.get(j));
     //       System.out.println("move to " +newPosition.get(j).x + " " + newPosition.get(j).y);
            i++;
            j++;
        }
    }
    
    /**
     *@return Positions which contains 5 arraylists of Vector2 indicating 
     * the positions this ship can move to in each direction (forward,back,left,
     * right).
     */
    public Positions availableMoves(){
        int speed = this.getCurrentSpeed();
        int size = this.getSize(); //need original size?
        Vector2 shipBow = this.getShipUnits()[0].getPosition();
        System.out.println("current dir " + this.getDirection().toString());
        // I get an error if I don't add this part.
        ArrayList<Vector2> back = new ArrayList<Vector2>();
        ArrayList<Vector2> forward = new ArrayList<Vector2>();
        ArrayList<Vector2> left = new ArrayList<Vector2>();
        ArrayList<Vector2> right = new ArrayList<Vector2>();
        Positions availableMoves = new Positions(back, forward, left, right);
        switch (this.getDirection()){
            case North: availableMoves = this.availableMoveNorth(shipBow, size, speed);
                break;
            case South: availableMoves = this.availableMoveSouth(shipBow, size, speed);
                break;
            case East: availableMoves = this.availableMoveEast(shipBow, size, speed);
                break;
            case West: availableMoves = this.availableMoveWest(shipBow, size, speed);
                break;
            default: // return a null arraylist of available moves?
        }
        return availableMoves;
    }
    /**
     * This method return 4 arraylists of positions a ship can move to if the ship
     * is originally facing north.
     * @param su Array of shipUnits of this ship.
     * @param size The size of the ship
     * @return 
     */    
    private Positions availableMoveNorth(Vector2 bow, int size, int speed){
    	int x = bow.x;
        int y = bow.y;
        int i, j = 0;
        ArrayList<Vector2> back = new ArrayList<Vector2>();
        ArrayList<Vector2> forward = new ArrayList<Vector2>();
        ArrayList<Vector2> left = new ArrayList<Vector2>();
        ArrayList<Vector2> right = new ArrayList<Vector2>();
        //move backward.
        if (y+size < 30){
            for (i = y+1; i <= y+size; i++){
                Vector2 p = new Vector2(x,i);
    		back.add(p);     
            }
        }
        
        //move sideways.
        if (x-1 >= 0){
            int leftX = x-1;
            for (i = y; i < y+size; i++){
                Vector2 p = new Vector2(leftX,i);
    		left.add(p);
            }
        }
        if (x+1 < 30){
            int rightX = x+1;
            for (i = y; i < y+size; i++){
                Vector2 p = new Vector2(rightX,i);
       		right.add(p);
            }
        }        
        
        // move forward.
        if (y < speed){
            for (i = y+size-1; i >= 0; i--){
                Vector2 p = new Vector2(x,i);
       		forward.add(p);
            }
        }else{
            for (i = y+size-1; i >= y-speed; i--){
        	Vector2 p = new Vector2(x,i);
                forward.add(p);
            }
        }
        
        Positions positions = new Positions(back, forward, left, right);
        return positions; 
    }     
    /**
     * This method return 4 arraylists of positions a ship can move to if the ship
     * is originally facing south.
     * @param su Array of shipUnits of this ship.
     * @param size The size of the ship
     * @return 
     */         
    private Positions availableMoveSouth(Vector2 bow, int size, int speed){
        int x = bow.x;
        int y = bow.y;
        int i, j = 0;
        ArrayList<Vector2> back = new ArrayList<Vector2>();
        ArrayList<Vector2> forward = new ArrayList<Vector2>();
        ArrayList<Vector2> left = new ArrayList<Vector2>();
        ArrayList<Vector2> right = new ArrayList<Vector2>();
        // move backward.
        if (y-size >= 0){
            for (i = y-1; i <= y-size; i--){
                Vector2 p = new Vector2(x,i);
    		back.add(p);     
            }
        for (int k = 0; k < back.size(); i++){
            System.out.println("back   == " + back.get(k).x + " " + back.get(k).y);
            }            
        }   
        
        // move sideways.
        if (x-1 >= 0){
            int rightX = x-1;
            for (i = y; i > y-size; i--){
                Vector2 p = new Vector2(rightX,i);
                right.add(p);
            }
        }
        if (x+1 < 30){
            int leftX = x+1;
            for (i = y; i > y-size; i--){
                Vector2 p = new Vector2(leftX,i);
       		left.add(p);
            }
        }        
        // move forward.
        if (y + speed > 29){
            for (i = y-size+1; i < 30; i++){
        	Vector2 p = new Vector2(x,i);
                forward.add(p);
            }
            
        }else{
            for (i = y-size+1; i <= y+speed; i++){
                Vector2 p = new Vector2(x,i);
                forward.add(p);
            }
        }
        Positions positions = new Positions(back, forward, left, right);
        return positions;

    }
    
    private Positions availableMoveEast(Vector2 bow, int size, int speed){
        int x = bow.x;
        int y = bow.y;
        int i, j = 0;
        ArrayList<Vector2> back = new ArrayList<Vector2>();
        ArrayList<Vector2> forward = new ArrayList<Vector2>();
        ArrayList<Vector2> left = new ArrayList<Vector2>();
        ArrayList<Vector2> right = new ArrayList<Vector2>();
        // move backward.
        if (x-size >= 0){
            for (i = x-1; i >= x-size; i--){
                Vector2 p = new Vector2(i,y);
    		back.add(p);     
            }
        }
        // move sideways.           
        if (y-1 >= 0){   
            int leftY = y-1;
            for (i = x; i > x-size; i--){
                Vector2 p = new Vector2(i,leftY);
    		left.add(p);
            }
        }
        // move sideways.            
        if (y+1 < 30){
            int rightY = y+1;
            for (i = x; i > x-size; i--){
       		Vector2 p = new Vector2(i,rightY);
       		right.add(p);
            }
        }
        // move forward.        
        if (x + speed > 29){
            for (i = x-size+1; i < 30; i++){
                Vector2 p = new Vector2(i,y);
                forward.add(p);
            }
        }else{
            for (i = x-size+1; i <= x+speed; i++){
                Vector2 p = new Vector2(i,y);
                forward.add(p);
            }
        }

        Positions positions = new Positions(back, forward, left, right);   
        return positions;
    }
    
    private Positions availableMoveWest(Vector2 bow, int size, int speed){

    	int x = bow.x;
        int y = bow.y;
        int i, j = 0;
        ArrayList<Vector2> back = new ArrayList<Vector2>();
        ArrayList<Vector2> forward = new ArrayList<Vector2>();
        ArrayList<Vector2> left = new ArrayList<Vector2>();
        ArrayList<Vector2> right = new ArrayList<Vector2>();

        // move backward.
        if (x+size < 30){
            for (i = x+1; i <= x+size; i++){
                Vector2 p = new Vector2(i,y);
    		back.add(p);     
            }
        }
        // move sideways.
        if (y-1 >= 0){
            int rightY = y-1;
            for (i = x; i < x+size; i++){
    		Vector2 p = new Vector2(i,rightY);
    		right.add(p);
            }
        }
        if (y+1 < 30){
            int leftY = y+1;
            for (i = x; i < x+size; i++){
       		Vector2 p = new Vector2(i,leftY);
       		left.add(p);
            }
        }  
        // move forward.
        if (x < speed){
            for (i = x+size-1; i >= 0; i--){
                Vector2 p = new Vector2(i,y);
                forward.add(p);
            }
        }else{
            for (i = x+size-1; i >= x-speed; i--){
                Vector2 p = new Vector2(i,y);
                forward.add(p);              
            }
        }
        
        Positions positions = new Positions(back, forward, left, right);
        return positions; 
    }
    /**
     * This method updates each ShipUnit to the new position.
     * @param An array of Vector2 indicating the new potions starting at the bow
     * of the ship.
     * @param newPosition
     * @param d The new direction should be passed from the GUI.
     */
    public void turnTo(ArrayList<Vector2> newPosition, ShipDirection d){
        // assert newPosition.length == shipUnits.length
        // and assuming it's the right order.
        ShipUnit[] shipUnits = this.getShipUnits();
        int i = 0;
        int j = 0;
        while (i < newPosition.size() && j < shipUnits.length){
            ShipUnit s = shipUnits[i];
            newPosition.get(j);
            s.setPosition(newPosition.get(j));
            i++;
            j++;
        }        
        this.setDirection(d);
    }
    /**
     * This method return true if the ship can turn 180 degree in one turn.
     * (Torpedo and radar boat)
     * @return 
     */
    public boolean hasFlexibleTurn(){
        if (this.shipType.equals(ShipType.TorpedoBoat)){
            return true;
        }else if (this.shipType.equals(ShipType.RadarBoat)){
            return true; 
        }else{
            return false;
        }
    }
            
    /**
     * This method return a TurnPositions which contains 5 arraylist of positions
     * this ship can turn to.
     * @return 
     */
    public TurnPositions availableTurns() {
        TurnPositions availableTurns = new TurnPositions(null,null,null,null,null);
        ShipUnit[] shipUnits = this.getShipUnits();
        ShipDirection direction = this.getDirection();
        int size = this.getSize();// need original size
        System.out.println("current direction " + direction.toString());
        switch (direction){
            case North: availableTurns = this.availableTurnNorth(shipUnits, size);
                break;
            case South: availableTurns = this.availableTurnSouth(shipUnits, size);
                break;
            case East: availableTurns = this.availableTurnEast(shipUnits, size);
                break;
            case West: availableTurns = this.availableTurnWest(shipUnits, size);
                break;
        }
        return availableTurns;
    }
    /**
     * This method return 5 arraylist of positions a ship can turn to if the ship
     * is originally facing north.
     * @param su Array of shipUnits of this ship.
     * @param size The size of the ship
     * @return 
     */
    private TurnPositions availableTurnNorth(ShipUnit[] su, int size){
        ShipUnit pivot;
        ArrayList<Vector2> left = new ArrayList<Vector2>();
        ArrayList<Vector2> lPath = new ArrayList<Vector2>();
        ArrayList<Vector2> right = new ArrayList<Vector2>();
        ArrayList<Vector2> rPath = new ArrayList<Vector2>();
        ArrayList<Vector2> back = new ArrayList<Vector2>();
        TurnPositions positions = new TurnPositions(null,null,null,null,null);
        ShipDirection d;
        int i,j,k;
        int xPivot;
        int yPivot;
        if (this.hasFlexibleTurn()){
            pivot = su[size-2];
            Vector2 pivotPosition = pivot.getPosition();
            xPivot = pivotPosition.x;
            yPivot = pivotPosition.y;
           
            if (xPivot-1 >= 0 && xPivot+1 < 30 && yPivot-1 >= 0 && yPivot+1 < 30){
                //left 90.
                for (i = xPivot-1; i <= xPivot+1; i++){
                    Vector2 p = new Vector2(i,yPivot);
                    left.add(p);
                }
                d = ShipDirection.West;
                positions.setLeft(left);
                positions.setLeftDirection(d);
                //leftpath
                Vector2 p1 = new Vector2(xPivot-1, yPivot-1);
                Vector2 p2 = new Vector2(xPivot+1, yPivot+1);
                lPath.add(p1);
                lPath.add(p2);
                positions.setLeftPath(lPath);
                //right 90.      
                for (i = xPivot+1; i >= xPivot-1; i--){
                    Vector2 p = new Vector2(i,yPivot);
                    right.add(p);
                }
                d = ShipDirection.East;
                positions.setRight(right);
                positions.setRightDirection(d);
                //rightpath
                Vector2 p3 = new Vector2(xPivot+1, yPivot-1);
                Vector2 p4 = new Vector2(xPivot-1, yPivot+1);
                rPath.add(p3);
                rPath.add(p4);
                positions.setRightPath(rPath);
                
                //180 turn should also be possible. 
                for (i = yPivot+1; i >= yPivot-1; i--){
                    Vector2 p = new Vector2(xPivot,i);
                    back.add(p);
                }
                d = ShipDirection.South;
                positions.setBackDirection(d);
                positions.setBack(back);
            }                            
        }else{
            pivot = su[size-1];
            Vector2 pivotPosition = pivot.getPosition();
            xPivot = pivotPosition.x;
            yPivot = pivotPosition.y;

            if (xPivot-size+1 >= 0){
                //left 90.
                for (i = xPivot-size+1; i <= xPivot; i++){
                    Vector2 p = new Vector2(i,yPivot);
                    left.add(p);
                }
                d = ShipDirection.West;
                positions.setLeftDirection(d);                
                positions.setLeft(left);
                //leftpath
                k = 0;
                for (i = yPivot-1; i >= yPivot-size+1; i--){
                    for (j = xPivot-size+1+k; j <= xPivot-1; j++){           
                        Vector2 p = new Vector2(j,i);
                        lPath.add(p);  
                    }
                    k++;        
                }
                positions.setLeftPath(lPath);             
            }
            if (xPivot+size-1 < 30){
                //right 90;
                for (i = xPivot+size-1; i >= xPivot; i--){
                    Vector2 p = new Vector2(i,yPivot);
                    right.add(p);
                }
                d = ShipDirection.East;
                positions.setRightDirection(d);                
                positions.setRight(right);
                //rightpath
                k = 0;
                for (i = yPivot-1; i >= yPivot-size+1; i--){                    
                    for (j = xPivot+1; j <= xPivot+size-1-k; j++){
                        Vector2 p = new Vector2(j,i);
                        rPath.add(p);                        
                    }
                    k++;
                }
                positions.setRightPath(rPath);
            }
        }
        return positions;    
    }
    /**
     * This method return 5 arraylist of positions a ship can turn to if the ship
     * is originally facing south.
     * @param su Array of shipUnits of this ship.
     * @param size The size of the ship
     * @return 
     */
    private TurnPositions availableTurnSouth(ShipUnit[] su, int size){
        ShipUnit pivot;
        ArrayList<Vector2> left = new ArrayList<Vector2>();
        ArrayList<Vector2> lPath = new ArrayList<Vector2>();
        ArrayList<Vector2> right = new ArrayList<Vector2>();
        ArrayList<Vector2> rPath = new ArrayList<Vector2>();
        ArrayList<Vector2> back = new ArrayList<Vector2>();
        TurnPositions positions = new TurnPositions(null,null,null,null,null);
        ShipDirection d;
        int i,j,k;
        int xPivot;
        int yPivot;
        if (this.hasFlexibleTurn()){
            pivot = su[size-2];
            Vector2 pivotPosition = pivot.getPosition();
            xPivot = pivotPosition.x;
            yPivot = pivotPosition.y;

            if (xPivot-1 >= 0 && xPivot+1 < 30 && yPivot-1 >= 0 && yPivot+1 < 30){
                //left 90.
                for (i = xPivot+1; i >= xPivot-1; i--){
                    Vector2 p = new Vector2(i,yPivot);
                    left.add(p);
                }
                d = ShipDirection.East;
                positions.setLeftDirection(d);                
                positions.setLeft(left);
                //leftpath
                Vector2 p1 = new Vector2(xPivot-1, yPivot-1);
                Vector2 p2 = new Vector2(xPivot+1, yPivot+1);
                lPath.add(p1);
                lPath.add(p2);
                positions.setLeftPath(lPath);
                //right 90.
                for (i = xPivot-1; i <= xPivot+1; i++){
                    Vector2 p = new Vector2(i,yPivot);
                    right.add(p);
                }
                d = ShipDirection.West;
                positions.setRightDirection(d);                
                positions.setRight(right);
                //rightpath
                Vector2 p3 = new Vector2(xPivot+1, yPivot-1);
                Vector2 p4 = new Vector2(xPivot-1, yPivot+1);
                rPath.add(p3);
                rPath.add(p4);
                positions.setRightPath(rPath);
                //180 turn should also be possible. 
                for (i = yPivot-1; i <= yPivot+1; i++){
                    Vector2 p = new Vector2(xPivot,i);
                    back.add(p);
                }
                d = ShipDirection.North;
                positions.setBackDirection(d);                
                positions.setBack(back);
            }               
                       
        }else{
            pivot = su[size-1];
            Vector2 pivotPosition = pivot.getPosition();
            xPivot = pivotPosition.x;
            yPivot = pivotPosition.y;
            //right 90.
            if (xPivot-size+1 >= 0){
                for (i = xPivot-size+1; i <= xPivot; i++){
                    Vector2 p = new Vector2(i,yPivot);
                    right.add(p);
                }
                d = ShipDirection.West;
                positions.setRightDirection(d);                
                positions.setRight(right);
                //rightpath
                k = 0;
                for (i = yPivot+1; i <= yPivot+size-1; i++){
                    for (j = xPivot-size+1+k; j <= xPivot-1; j++){
                        Vector2 p = new Vector2(j,i);
                        rPath.add(p);                        
                    }
                    k++;
                }
                positions.setRightPath(rPath);                    
            }
            //left 90;
            if (xPivot+size-1 < 30){
                for (i = xPivot+size-1; i >= xPivot; i--){
                    Vector2 p = new Vector2(i,yPivot);
                    left.add(p);
                }
                d = ShipDirection.East;
                positions.setLeftDirection(d);                
                positions.setLeft(left);
                //leftpath
                k = 0;
                for (i = yPivot+1; i <= yPivot+size-1; i++){
                    for (j = xPivot+1; j <= xPivot+size-1-k; j++){
                        Vector2 p = new Vector2(j,i);    
                        lPath.add(p);                        
                    }
                    k++;
                }
                positions.setLeftPath(lPath);                
            }
        }
        return positions; 
    }
    private TurnPositions availableTurnEast(ShipUnit[] su, int size){
        ShipUnit pivot;
        ArrayList<Vector2> left = new ArrayList<Vector2>();
        ArrayList<Vector2> lPath = new ArrayList<Vector2>();
        ArrayList<Vector2> right = new ArrayList<Vector2>();
        ArrayList<Vector2> rPath = new ArrayList<Vector2>();
        ArrayList<Vector2> back = new ArrayList<Vector2>();
        TurnPositions positions = new TurnPositions(null,null,null,null,null);
        ShipDirection d;
        int i,j,k;
        int xPivot;
        int yPivot;
        if (this.hasFlexibleTurn()){
            pivot = su[size-2];
            Vector2 pivotPosition = pivot.getPosition();
            xPivot = pivotPosition.x;
            yPivot = pivotPosition.y;

            if (xPivot-1 >= 0 && xPivot+1 < 30 && yPivot-1 >= 0 && yPivot+1 < 30){
                //left 90.
                for (i = yPivot-1; i <= yPivot+1; i++){
                    Vector2 p = new Vector2(xPivot,i);
                    left.add(p);
                }
                d = ShipDirection.North;
                positions.setLeftDirection(d);                
                positions.setLeft(left);
                //lefttpath
                Vector2 p1 = new Vector2(xPivot+1, yPivot-1);
                Vector2 p2 = new Vector2(xPivot-1, yPivot+1);
                lPath.add(p1);
                lPath.add(p2);
                positions.setLeftPath(lPath);     
                //right 90.
                for (i = yPivot+1; i >= yPivot-1; i--){
                    Vector2 p = new Vector2(xPivot,i);
                    right.add(p);
                }
                d = ShipDirection.South;
                positions.setRightDirection(d);                
                positions.setRight(right);
                //rightpath
                Vector2 p3 = new Vector2(xPivot-1, yPivot-1);
                Vector2 p4 = new Vector2(xPivot+1, yPivot+1);
                rPath.add(p3);
                rPath.add(p4);
                positions.setRightPath(rPath);                
                //180 turn should also be possible. 
                for (i = xPivot-1; i <= xPivot+1; i++){
                    Vector2 p = new Vector2(i, yPivot);
                    back.add(p);
                }
                d = ShipDirection.West;
                positions.setBackDirection(d);    
                positions.setBack(back);
            }               
                       
        }else{
            pivot = su[size-1];
            Vector2 pivotPosition = pivot.getPosition();
            xPivot = pivotPosition.x;
            yPivot = pivotPosition.y;
            //left 90.
            if (yPivot-size+1 >= 0){
                for (i = yPivot-size+1; i <= yPivot; i++){
                    Vector2 p = new Vector2(xPivot, i);
                    left.add(p);
                }
                d = ShipDirection.North;
                positions.setLeftDirection(d);                
                positions.setLeft(left);
                //leftpath
                k = 0;
                for (i = yPivot-1; i >= yPivot-size+1; i--){
                    for (j = xPivot+1; j <= xPivot+size-1-k; j++){
                        Vector2 p = new Vector2(j,i);
                        lPath.add(p);                        
                    }
                    k++;
                }
                positions.setLeftPath(lPath);
            }
            //right 90;
            if (yPivot+size-1 < 30){
                for (i = yPivot+size-1; i >= yPivot; i--){
                    Vector2 p = new Vector2(xPivot, i);
                    right.add(p);
                }
                d = ShipDirection.South;
                positions.setRightDirection(d);                
                positions.setRight(right);
                //rightpath
                k = 0;
                for (i = yPivot+1; i <= yPivot+size-1; i++){
                    for (j = xPivot+1; j <= xPivot+size-1-k; j++){
                        Vector2 p = new Vector2(j,i);
                        rPath.add(p);                        
                    }
                    k++;
                }
                positions.setRightPath(rPath);                 
            }
        }
        return positions;  
    }
    
    private TurnPositions availableTurnWest(ShipUnit[] su, int size){
        ShipUnit pivot;
        ArrayList<Vector2> left = new ArrayList<Vector2>();
        ArrayList<Vector2> lPath = new ArrayList<Vector2>();
        ArrayList<Vector2> right = new ArrayList<Vector2>();
        ArrayList<Vector2> rPath = new ArrayList<Vector2>();
        ArrayList<Vector2> back = new ArrayList<Vector2>();
        TurnPositions positions = new TurnPositions(null,null,null,null,null);
        ShipDirection d;
        int i,j,k;
        int xPivot;
        int yPivot;
        if (this.hasFlexibleTurn()){
            pivot = su[size-2];
            Vector2 pivotPosition = pivot.getPosition();
            xPivot = pivotPosition.x;
            yPivot = pivotPosition.y;

            if (xPivot-1 >= 0 && xPivot+1 < 30 && yPivot-1 >= 0 && yPivot+1 < 30){
                //left 90.
                for (i = yPivot+1; i >= yPivot-1; i--){
                    Vector2 p = new Vector2(xPivot, i);
                    left.add(p);
                }
                d = ShipDirection.South;
                positions.setLeftDirection(d);                
                positions.setLeft(left);
                //lefttpath
                Vector2 p1 = new Vector2(xPivot+1, yPivot-1);
                Vector2 p2 = new Vector2(xPivot-1, yPivot+1);
                lPath.add(p1);
                lPath.add(p2);
                positions.setLeftPath(lPath); 
                //right 90.
                for (i = yPivot-1; i <= yPivot+1; i++){
                    Vector2 p = new Vector2(xPivot, i);
                    right.add(p);
                }
                d = ShipDirection.North;
                positions.setRightDirection(d);                
                positions.setRight(right);    
                //rightpath
                Vector2 p3 = new Vector2(xPivot-1, yPivot-1);
                Vector2 p4 = new Vector2(xPivot+1, yPivot+1);
                rPath.add(p3);
                rPath.add(p4);
                positions.setRightPath(rPath);                    
                //180 turn should also be possible. 
                for (i = xPivot+1; i >= xPivot-1; i--){
                    Vector2 p = new Vector2(i, yPivot);
                    back.add(p);
                }
                d = ShipDirection.East;
                positions.setBackDirection(d);                
                positions.setBack(back);
            }               
                       
        }else{
            pivot = su[size-1];
            Vector2 pivotPosition = pivot.getPosition();
            xPivot = pivotPosition.x;
            yPivot = pivotPosition.y;
            //right 90.
            if (yPivot-size+1 >= 0){
                for (i = yPivot-size+1; i <= yPivot; i++){
                    Vector2 p = new Vector2(xPivot, i);
                    right.add(p);
                }
                d = ShipDirection.North;
                positions.setRightDirection(d);                
                positions.setRight(right);
                //rightpath
                k = 0;
                for (i = yPivot-1; i >= yPivot-size+1; i--){
                     for (j = xPivot-size+1+k; j <= xPivot-1; j++){                    
                        Vector2 p = new Vector2(j,i);
                        rPath.add(p);  
                    }
                    k++;        
                }
                positions.setRightPath(rPath);
            }
            //left 90;
            if (yPivot+size-1 < 30){
                for (i = yPivot+size-1; i >= yPivot; i--){
                    Vector2 p = new Vector2(xPivot, i);
                    left.add(p);
                }
                d = ShipDirection.South;
                positions.setLeftDirection(d);                
                positions.setLeft(left);
                //leftpath
                k = 0;
                for (i = yPivot+1; i <= yPivot+size-1; i++){
                    for (j = xPivot-size+1+k; j <= xPivot-1; j++){
                        Vector2 p = new Vector2(j,i);
                        lPath.add(p);                        
                    }
                    k++;
                }
                positions.setLeftPath(lPath);                
            }
        }
        return positions; 
    }
 }
