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
public abstract class Ship {
    
    public enum ShipType {
        Cruiser, Destroyer, TorpedoBoat, MineLayer, RadarBoat
    };
    
    public enum ShipDirection {
        North, South, East, West
    };
    
    /** The attributes of Ship class. */
    private ShipType shipType;
    private ShipUnit[] shipUnits;
    private final int playerID;
    private int size;
    private int speed;
    private int currentSize;
    private int currentSpeed;
    private int armour;
    private ShipDirection direction; 
    protected ArrayList<String> weapons;

    
    /** Constructs a ship given a player ID. */
    public Ship(int pid){
        this.playerID = pid;
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
	
    public ShipUnit[] getShipUnits(){
        return this.shipUnits;
    }

    public void setShipUnits(ShipUnit[] shipUnits) {
	this.shipUnits = shipUnits;
    }


    /**
     * This method should be called after each attack on the ship.
     */
    public void reduceCurentSize(){
        currentSize--;    
        //TO DO : calculate the reduce speed accordingly.
        int reducedSpeed = 0;
        setCurrentSpeed(reducedSpeed);
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
        for (ShipUnit s: shipUnits){
            for (Vector2 v: newPosition){
                s.setPosition(v);
            }
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
            Vector2 backPosition = new Vector2(x,y+size);
            back.add(backPosition);
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
            for (i = y-1; i >= 0; i--){
                Vector2 p = new Vector2(x,i);
       		forward.add(p);
            }
        }else{
            for (i = y-1; i >= y-speed; i--){
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
            Vector2 backPosition = new Vector2(x,y-size);
            back.add(backPosition);
        }
        
        // move sideways.
        if (x-1 >= 0){
            int leftX = x-1;
            for (i = y; i > y-size; i--){
                Vector2 p = new Vector2(leftX,i);
                left.add(p);
            }
        }
        if (x+1 < 30){
            int rightX = x+1;
            for (i = y; i > y-size; i--){
                Vector2 p = new Vector2(rightX,i);
       		right.add(p);
            }
        }        
        // move forward.
        if (y + speed > 29){
            for (i = y+1; i < 30; i++){
        	Vector2 p = new Vector2(x,i);
                forward.add(p);
            }
            
        }else{
            for (i = y+1; i <= y+speed; i++){
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
            Vector2 backPosition = new Vector2(x-size, y);
            back.add(backPosition);
        }
        
        // move sideways.
        if (y-1 >= 0){
            int leftY = y-1;
            for (i = x; i > x-size; i--){
                Vector2 p = new Vector2(i,leftY);
    		left.add(p);
            }
        }
        if (y+1 < 30){
            int rightY = y+1;
            for (i = x; i > x-size; i--){
       		Vector2 p = new Vector2(i,rightY);
       		right.add(p);
            }
        }    
        
        // move forward.
        if (x + speed > 29){
            for (i = x+1; i < 30; i++){
                Vector2 p = new Vector2(i,y);
                forward.add(p);
            }
        }else{
            for (i = x+1; i <= x+speed; i++){
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
            Vector2 backPosition = new Vector2(x+size,y);
            back.add(backPosition);
        }
        // move sideways.
        if (y-1 >= 0){
            int leftY = y-1;
            for (i = x; i < x+size; i++){
    		Vector2 p = new Vector2(i,leftY);
    		left.add(p);
            }
        }
        if (y+1 < 30){
            int rightY = y+1;
            for (i = x; i < x+size; i++){
       		Vector2 p = new Vector2(i,rightY);
       		right.add(p);
            }
        }  
        // move forward.
        if (x < speed){
            for (i = x-1; i >= 0; i--){
                Vector2 p = new Vector2(i,y);
                forward.add(p);
            }
        }else{
            for (i = x-1; i >= x-speed; i--){
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
    public void turnTo(Vector2[] newPosition, ShipDirection d){
        // assert newPosition.length == shipUnits.length
        // and assuming it's the right order.
        ShipUnit[] shipUnits = this.getShipUnits();
        for (ShipUnit s: shipUnits){
            for (Vector2 v: newPosition){
                s.setPosition(v);
            }
        }
        
        this.setDirection(d);
    }
    /**
     * This method return true if the ship can turn 180 degree in one turn.
     * It's called by prepareTurn()
     * (Torpedo and radar boat)
     * @return 
     */
    private boolean hasFlexibleTurn(){
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
                positions.setLeft(left);
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
                positions.setRight(right);
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
                positions.setLeft(left);
                //leftpath
                k = 0;
                for (i = yPivot+1; i <= yPivot+size-1; i++){
                    for (j = xPivot+1; j <= xPivot+size-1-k; j++){
                        Vector2 p = new Vector2(j,i);
                        System.out.print("j " + j);
                        System.out.print( "  ");
                        System.out.println(i);     
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
    // Added main() to test. 
/*    public static void main(String[] args) {
        ShipUnit su = new ShipUnit();
        ShipUnit su2 = new ShipUnit();
        ShipUnit su3 = new ShipUnit();
        ShipUnit su4 = new ShipUnit();        
       
        su.setPosition(new Vector2(0,3));        
        su2.setPosition(new Vector2(1,3));
        su3.setPosition(new Vector2(2,3)); 
        su4.setPosition(new Vector2(3,3));         
        ShipUnit[] units = new ShipUnit[4];
        units[0] = su;
        units[1] = su2;
        units[2] = su3;
        units[3] = su4;
        TurnPositions allTurns = availableTurnWest(units, 4);
        ArrayList<Vector2> left = allTurns.getLeft();
        ArrayList<Vector2> lp = allTurns.getLeftPath(); 
        ArrayList<Vector2> r = allTurns.getRight();   
        ArrayList<Vector2> rp = allTurns.getRightPath();    
        ArrayList<Vector2> b = allTurns.getBackward();    
        if (left != null){
            for (int i = 0; i < left.size(); i++){
                System.out.print("left" + left.get(i).x);
                System.out.print( "  ");
                System.out.println(left.get(i).y);        
            }
        }
        if (lp != null){
        for (int i = 0; i < lp.size(); i++){
            System.out.print("lp" + lp.get(i).x);
            System.out.print( "  ");
            System.out.println(lp.get(i).y);        
        }      
        }
        if (r != null){
        for (int i = 0; i < r.size(); i++){
            System.out.print("right" + r.get(i).x);
            System.out.print( "  ");
            System.out.println(r.get(i).y);        
        }
        }
        if (rp != null){
        for (int i = 0; i < rp.size(); i++){
            System.out.print("rp" + rp.get(i).x);
            System.out.print( "  ");
            System.out.println(rp.get(i).y);        
        }   
        }
        if (b != null) {
        for (int i = 0; i < b.size(); i++){
            System.out.print("back" + b.get(i).x);
            System.out.print( "  ");
            System.out.println(b.get(i).y);        
        }        
        }
    }

*/
}
