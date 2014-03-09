/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my_game.models.game_components;
import java.util.ArrayList;
import my_game.models.player_components.ChatLog;
import my_game.models.player_components.Player;
import my_game.util.Vector2;
import my_game.models.ships_impl.*;
/**
 * The state of a game describes a game fully.
 */
public class GameState implements java.io.Serializable {
    
    public enum GamePhase {
        New,    // game has just been created, players have not interacted yet
        ShipPositioning, // after creating the game, players position their ships on the map
        PlayerTurns,    // the main part of the game where players take turns 
        GameOver    //the game is over, one player is the winner
    };
    
    protected GamePhase phase;
    
    /** An array of two players who are playing the game. */
    private Player[] player;
    
    /** Index of the players array pointing to the player whose turn it currently is. */
    private int playerTurn;
    
    /** The name of the game given to it when it is created for the first time. */
    public final String name;
    
    /** Identifier for the game. */
    //public final int gameID;    // TODO Is this necessary? Please clarify why. Also it will be unnecessarily comples to generate unique IDs
    
    protected ChatLog chatLog;
    protected Map map;
    
    
    //TODO accessors and mutators for chat log and map

    public GameState(Player[] player, CoralReef reef, int firstPlayer, String name) {
        //init game phase
        this.phase = GamePhase.New;
        //init the players array
        this.player = new Player[2];
        this.player[0] = player[0];
        this.player[1] = player[1]; 
        //init player turn
        this.playerTurn = firstPlayer;
        //set game name
        this.name = name;
        //init each player's ships
        Ship[] player0Ships = generatePlayerShips(player[0].id);
        Ship[] player1Ships = generateOpponentShips(player[1].id);
        Base player0Base = new Base(player[0].getID());
        Base player1Base = new Base(player[1].getID());
        //init map
        map = new Map(reef, player0Ships, player1Ships, player0Base, player1Base);
        //init chat
        chatLog = new ChatLog();
    }
    
    /**
     * Access the map object of this game state.
     * @return 
     */
    public Map getMap() {
        return this.map;
    }
    
    public GameState(GameState copyState) {
        this.phase = copyState.phase;
        //shallow copy players array
        this.player = new Player[copyState.player.length];
        System.arraycopy(copyState.player, 0, this.player, 0, copyState.player.length);
        this.playerTurn = copyState.playerTurn;
        this.name = copyState.name;
        //use copy constructor to create a chatlog copy
        this.chatLog = new ChatLog(copyState.chatLog);
        //use copy constructor to create a map copy
        this.map = new Map(copyState.map);
        //TODO implement map copy constructor
    }
    public void setGamePhase(GamePhase p){
        this.phase = p;
    }
    
    private Ship[] generatePlayerShips(int pid) {
        ShipDirection d = ShipDirection.East;
        ArrayList<Vector2> position = new ArrayList<Vector2>();
        int y = 10;
        for (int x = 5; x > 0; x--){
            Vector2 v = new Vector2(x,y);
            position.add(v);
        }
        Cruiser c1 = new Cruiser(pid, position, d);
        position.clear();
        y++;
        for (int x = 5; x > 0; x--){
            Vector2 v = new Vector2(x,y);
            position.add(v);
        }
        Cruiser c2 = new Cruiser(pid, position, d);   
        position.clear();
        y++;
        for (int x = 4; x > 0; x--){
            Vector2 v = new Vector2(x,y);
            position.add(v);
        }        
        Destroyer d1 = new Destroyer(pid, position, d);
        position.clear();
        y++;
        for (int x = 4; x > 0; x--){
            Vector2 v = new Vector2(x,y);
            position.add(v);
        }        
        Destroyer d2 = new Destroyer(pid, position, d);        
        position.clear();
        y++;
        for (int x = 4; x > 0; x--){
            Vector2 v = new Vector2(x,y);
            position.add(v);
        }        
        Destroyer d3 = new Destroyer(pid, position, d);        
        position.clear();
        y++;
        for (int x = 3; x > 0; x--){
            Vector2 v = new Vector2(x,y);
            position.add(v);
        }        
        TorpedoBoat t1 = new TorpedoBoat(pid, position, d);   
        position.clear();
        y++;
        for (int x = 3; x > 0; x--){
            Vector2 v = new Vector2(x,y);
            position.add(v);
        }        
        TorpedoBoat t2 = new TorpedoBoat(pid, position, d);     
        position.clear();
        y++;
        for (int x = 2; x > 0; x--){
            Vector2 v = new Vector2(x,y);
            position.add(v);
        }        
        MineLayer m1 = new MineLayer(pid, position, d);    
        position.clear();
        y++;
        for (int x = 2; x > 0; x--){
            Vector2 v = new Vector2(x,y);
            position.add(v);
        }        
        MineLayer m2 = new MineLayer(pid, position, d);     
        position.clear();
        y++;
        for (int x = 3; x > 0; x--){
            Vector2 v = new Vector2(x,y);
            position.add(v);
        }        
        RadarBoat r = new RadarBoat(pid, position, d);   
        Ship[] ships = new Ship[10];
        ships[0]= c1;
        ships[1]= c2;
        ships[2]= d1;
        ships[3]= d2;
        ships[4]= d3;
        ships[5]= t1;
        ships[6]= t2;
        ships[7]= m1;
        ships[8]= m2;
        ships[9]= r;
                       
        return ships;
    }
    
    private Ship[] generateOpponentShips(int pid) {
        ShipDirection d = ShipDirection.West;
        ArrayList<Vector2> position = new ArrayList<Vector2>();
        int y = 10;
        int x;
        for (x = 29-5; x < 30; x++){
            Vector2 v = new Vector2(x,y);
            position.add(v);
        }
        Cruiser c1 = new Cruiser(pid, position, d);
        position.clear();
        y++;
        for (x = 29-5; x < 30; x++){
            Vector2 v = new Vector2(x,y);
            position.add(v);
        }
        Cruiser c2 = new Cruiser(pid, position, d);   
        position.clear();
        y++;
        for (x = 29-4; x < 30; x++){
            Vector2 v = new Vector2(x,y);
            position.add(v);
        }        
        Destroyer d1 = new Destroyer(pid, position, d);
        position.clear();
        y++;
        for (x = 29-4; x < 30; x++){
            Vector2 v = new Vector2(x,y);
            position.add(v);
        }        
        Destroyer d2 = new Destroyer(pid, position, d);        
        position.clear();
        y++;
        for (x = 29-4; x < 30; x++){
            Vector2 v = new Vector2(x,y);
            position.add(v);
        }        
        Destroyer d3 = new Destroyer(pid, position, d);        
        position.clear();
        y++;
        for (x = 29-3; x < 30; x++){
            Vector2 v = new Vector2(x,y);
            position.add(v);
        }        
        TorpedoBoat t1 = new TorpedoBoat(pid, position, d);   
        position.clear();
        y++;
        for (x = 29-3; x < 30; x++){
            Vector2 v = new Vector2(x,y);
            position.add(v);
        }        
        TorpedoBoat t2 = new TorpedoBoat(pid, position, d);     
        position.clear();
        y++;
        for (x = 29-2; x < 30; x++){
            Vector2 v = new Vector2(x,y);
            position.add(v);
        }        
        MineLayer m1 = new MineLayer(pid, position, d);    
        position.clear();
        y++;
        for (x = 29-2; x < 30; x++){
            Vector2 v = new Vector2(x,y);
            position.add(v);
        }        
        MineLayer m2 = new MineLayer(pid, position, d);     
        position.clear();
        y++;
        for (x = 29-3; x < 30; x++){
            Vector2 v = new Vector2(x,y);
            position.add(v);
        }        
        RadarBoat r = new RadarBoat(pid, position, d);   
        Ship[] ships = new Ship[10];
        ships[0]= c1;
        ships[1]= c2;
        ships[2]= d1;
        ships[3]= d2;
        ships[4]= d3;
        ships[5]= t1;
        ships[6]= t2;
        ships[7]= m1;
        ships[8]= m2;
        ships[9]= r;
                       
        return ships;
    }  

    /**
     * This method is called when the players rearrange their ships around the base.
     * @param s The ship they want to move
     * @param p The position they want to move to. (Does not need to be the
     * position of the bow.
     */
    private void positionShip(Ship s, Vector2 p) {
   /*     try {
            gameState.getMap().moveShip(s,position);
        } catch (GameException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
    */
        int shipSize = s.getSize();
        ArrayList<Vector2> positions = new ArrayList<Vector2>();
        int i;
        if(p.x == 0){
            if (p.y <= 9 && p.y >= 5){
                for (i = 10-shipSize; i < 10; i++){
                    Vector2 v = new Vector2(p.x,i);
                    positions.add(v);
                }              
            }else if (p.y >= 20 && p.y <= 24){
                for (i = 19+shipSize; i > 19; i--){
                    Vector2 v = new Vector2(p.x,i);
                    positions.add(v);                
                }
            }
        }else if (p.x == 29){
            if (p.y <= 9 && p.y >= 5){
                for (i = 10-shipSize; i < 10; i++){
                    Vector2 v = new Vector2(p.x,i);
                    positions.add(v);
                }              
            }else if (p.y >= 20 && p.y <= 24){
                for (i = 19+shipSize; i > 19; i--){
                    Vector2 v = new Vector2(p.x,i);
                    positions.add(v);                
                }
            }
        }else if(p.y > 9 && p.y < 20 && p.x <= 5){
            for (i = 1; i <= shipSize; i++){
                Vector2 v = new Vector2(i,p.y);
                positions.add(v);
            }             
        }else if(p.y > 9 && p.y < 20 && p.x >= 24){
            for (i = 29-shipSize; i < 29; i++){
                Vector2 v = new Vector2(i,p.y);
                positions.add(v);            
            }
        }
        
        // commented out this part so I can un the tests.
        
/*      boolean canMove = true;
        for (Vector2 v: positions){
            if (map.getObjectAt(v).getObjectType() != null){
                canMove = false;
            }
        }
        
        if (canMove){
            s.moveTo(positions);
            map.updateShipPositions(s);
        }        
  
  */
            for (Vector2 v: positions){
                System.out.println(v.x + " " + v.y);
            }         
    }    
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        sb.append("phase: " + this.phase + "\n");
        sb.append("Player0: " + this.player[0] + "\n");  //requires Player.toString();
        sb.append("Player1: " + this.player[1] + "\n");
        sb.append("Player turn: " + this.playerTurn + "\n");        
        sb.append("Server name: " + this.name + "\n");
        sb.append("ChatLog: " + this.chatLog + "\n");    //requires ChatLog.toString();
        sb.append("Map: \n" + this.map);                   //requires Map.toString();
        
        return sb.toString();
    }
}
