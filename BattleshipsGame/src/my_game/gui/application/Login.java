package my_game.gui.application;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import my_game.controller.Game;
import my_game.models.player_components.Player;
import my_game.networking.server.Constants;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class Login
    implements Initializable {

    @FXML //  fx:id="myButton"
    private Button loginGameButton; // Value injected by FXMLLoader
    
    @FXML //  fx:id="myButton"
    private ImageView background; // Value injected by FXMLLoader
    
    @FXML //  fx:id="myButton"
    private AnchorPane pane; // Value injected by FXMLLoader
    
    @FXML //  fx:id="myButton"
    private TextField username; // Value injected by FXMLLoader
    
    @FXML //  fx:id="myButton"
    private TextField password; // Value injected by FXMLLoader


    @Override // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {

    background.fitHeightProperty().bind(pane.heightProperty());
    background.fitWidthProperty().bind(pane.widthProperty());
    
    loginGameButton.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            Stage primaryStage=new Stage();
            AnchorPane page=null;
            try {
            	page = (AnchorPane) FXMLLoader.load(Main.class.getResource("UI.fxml"));
            } catch (IOException e) {
        	// TODO Auto-generated catch block
            	e.printStackTrace();
            }
            if(verifyLogin(username.toString() ,password.toString())){
                try {
                    Player player=new Player(username.getText(),password.getText(),InetAddress.getLocalHost(), Constants.SERVER_PORT, 0);
                    Main.setPlayer(player);
                } catch (UnknownHostException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
        	
            	Stage previousStage=Main.getStage();
            	previousStage.close();
            	Scene scene = new Scene(page);
            	primaryStage.setScene(scene);
            	primaryStage.setTitle("Battleship");
            	primaryStage.show();
            	Main.setStage(primaryStage);
            }else{
                JOptionPane.showMessageDialog(null, "Invalid username or password", "Error",
                JOptionPane.ERROR_MESSAGE);
            }
        }

        /**
         * Verifies the locally stored users for a user with the existing username
         * and password. If such users exists, returns true, otherwise returns false.
         * @param username The username to look for.
         * @param password The password to mach against the password stored for the given username.
         */
        private boolean verifyLogin(String username, String password) {
            return true;
            //TODO implement login verification
        }
    });

    }
}
