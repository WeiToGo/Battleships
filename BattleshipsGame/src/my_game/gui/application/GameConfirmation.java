package my_game.gui.application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import my_game.controller.Game;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class GameConfirmation
    implements Initializable {

    @FXML //  fx:id="myButton"
    private Button acceptGameButton; // Value injected by FXMLLoader
    
    @FXML //  fx:id="myButton"
    private Button declineGameButton; // Value injected by FXMLLoader
    
    @FXML //  fx:id="myButton"
    private Button returnGameButton; // Value injected by FXMLLoader
    
    @FXML //  fx:id="myButton"
    private ImageView background; // Value injected by FXMLLoader
    
    @FXML //  fx:id="myButton"
    private TextArea map; // Value injected by FXMLLoader
    
    @FXML //  fx:id="myButton"
    private AnchorPane pane; // Value injected by FXMLLoader


    @Override // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {

    background.fitHeightProperty().bind(pane.heightProperty());
    background.fitWidthProperty().bind(pane.widthProperty());
    
    acceptGameButton.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
        	//TODO if both players accept launch game and close window, else generate new map
        }
    });
    
    declineGameButton.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {

        }
    });
    
    returnGameButton.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
        	Stage primaryStage=new Stage();
        	AnchorPane page=null;
        	try {
        		page = (AnchorPane) FXMLLoader.load(Main.class.getResource("CreateGame.fxml"));
        	} catch (IOException e) {
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
        }
    });

    }
}
