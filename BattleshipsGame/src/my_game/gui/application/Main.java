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

public class Main extends Application {
	
	private static Stage stage;
	private static Player player;

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
    
}