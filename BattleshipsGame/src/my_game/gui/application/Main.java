package my_game.gui.application;

import java.util.logging.Level;
import java.util.logging.Logger;

import my_game.models.player_components.Player;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import my_game.networking.client.GameClient;
import my_game.networking.server.GameServer;

public class Main extends Application {
	
	private static Stage stage;
        /** The player logged into the game. */
	private static Player player;
        /** If the player hosts a game, this server object will be referenced
         * from all other classes using the server to communicate. */
        private static GameServer server;
        /** If the player joins a game, this client object will be referenced
         * from all other classes using the client to communicate. */
        private static GameClient client;
        
        public static boolean isServer;
        

    /**
     * @param args the command line arguments
     */
	
	
	
    public static void main(String[] args) {
        Application.launch(Main.class, (java.lang.String[])null);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
        	
        	AnchorPane page = null;       	
            page = (AnchorPane) FXMLLoader.load(Main.class.getResource("Login.fxml"));
            Scene scene = new Scene(page);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Battleship");
            primaryStage.show();
            stage=primaryStage;

        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static Stage getStage(){
    	return stage;
    }
    
    public static void setStage(Stage s){
    	stage=s;
    }
    
    public static Player getPlayer(){
    	return player;
    }
    
    public static void setPlayer(Player p){
    	player=p;
    }
    
    public static GameServer getServer() {
        return server;
    }
    
    
    public static void setServer(GameServer s) {
        server = s;
    }
    
    public static GameClient getClient() {
        return client;
    }
    
    public static void setClient(GameClient c) {
        client = c;
    }
}