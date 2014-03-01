/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my_game.models.game_components;
import java.util.ArrayList;

import my_game.util.Range;
import my_game.util.Vector2;
import my_game.util.Positions;
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
     * This method return true if the ship can turn 180 degree in one turn
     * (Torpedo and radar boat)
     * 
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
     *@return array of positions on the map that this ship can move to.
     */
    public Positions availableMoves(){
    
        ShipUnit[] shipUnits = this.getShipUnits();
        ShipDirection direction = this.getDirection();
        int speed = this.getCurrentSpeed();
        int size = this.getCurrentSize();
        Vector2 shipBow = shipUnits[0].getPosition();
        // I get an error if I don't add this part.
        ArrayList<Vector2> back = new ArrayList<Vector2>();
        ArrayList<Vector2> forward = new ArrayList<Vector2>();
        ArrayList<Vector2> left = new ArrayList<Vector2>();
        ArrayList<Vector2> right = new ArrayList<Vector2>();
        Positions availableMoves = new Positions(back, forward, left, right);
        switch (direction){
            case North: availableMoves = this.availableNorth(shipBow, size, speed);
                break;
            case South: availableMoves = this.availableSouth(shipBow, size, speed);
                break;
            case East: availableMoves = this.availableEast(shipBow, size, speed);
                break;
            case West: availableMoves = this.availableWest(shipBow, size, speed);
                break;
        }
  
        return availableMoves;
    }
    
    private Positions availableNorth(Vector2 bow, int size, int speed){
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
        
    private Positions availableSouth(Vector2 bow, int size, int speed){
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
    
    private Positions availableEast(Vector2 bow, int size, int speed){
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
    
    private  Positions availableWest(Vector2 bow, int size, int speed){

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
     *@return array of positons on the map that this ship can turn to.
     */
    public Vector2[] availableTurns() {
        throw new UnsupportedOperationException("Not yet implemented");
        
    }
          
}
