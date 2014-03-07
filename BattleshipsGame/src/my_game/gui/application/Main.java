package my_game.gui.application;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import my_game.models.player_components.Player;
import my_game.networking.server.Constants;

public class Main extends Application {
	
	private static Stage stage;
        
        /** The player running this instance of the game. */
        private Player player;

    /**
     * @param args the command line arguments
     */
	
	
	
    public static void main(String[] args) {
        Application.launch(Main.class, (java.lang.String[])null);
    }

    @Override
    public void start(Stage primaryStage) {
        initPlayer();
        
        try {
            
            AnchorPane page = null;       	
            page = (AnchorPane) FXMLLoader.load(Main.class.getResource("UI.fxml"));
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

    private void initPlayer() {
        try {
            //initialize the player to some default player
            //TODO modify this later to allow for player login
            this.player = new Player("DefaultPlayer:" + InetAddress.getLocalHost().getHostName(), "", InetAddress.getLocalHost(), Constants.SERVER_PORT, 1, 0);
        } catch (UnknownHostException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}