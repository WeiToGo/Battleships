package my_game.gui.application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class CreateGame
    implements Initializable {

    @FXML //  fx:id="myButton"
    private Button newGameButton; // Value injected by FXMLLoader
    
    @FXML //  fx:id="myButton"
    private Button loadGameButton; // Value injected by FXMLLoader
    
    @FXML //  fx:id="myButton"
    private Button returnGameButton; // Value injected by FXMLLoader
    
    @FXML //  fx:id="myButton"
    private ImageView background; // Value injected by FXMLLoader
    
    @FXML //  fx:id="myButton"
    private AnchorPane pane; // Value injected by FXMLLoader


    @Override // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        assert newGameButton != null : "fx:id=\"myButton\" was not injected: check your FXML file 'simple.fxml'.";

        // initialize your logic here: all @FXML variables will have been injected

    background.fitHeightProperty().bind(pane.heightProperty());
    background.fitWidthProperty().bind(pane.widthProperty());
    
    newGameButton.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
        	Stage primaryStage=new Stage();
        	AnchorPane page=null;
        	try {
        		page = (AnchorPane) FXMLLoader.load(Main.class.getResource("GameConfirmation.fxml"));
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
    
    loadGameButton.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
        	Stage primaryStage=new Stage();
        	AnchorPane page=null;
        	try {
        		page = (AnchorPane) FXMLLoader.load(Main.class.getResource("GameConfirmation.fxml"));
        	} catch (IOException e) {
        		// TODO Auto-generated catch block
        		e.printStackTrace();
        	}
        	
        	final JFileChooser chooser = new JFileChooser();
        	FileNameExtensionFilter filter = new FileNameExtensionFilter(
        			"JPG & GIF Images", "jpg", "gif");
        	chooser.setFileFilter(filter);
        	int returnVal = chooser.showOpenDialog(null);
        	if(returnVal == JFileChooser.APPROVE_OPTION) {
            	Stage previousStage=Main.getStage();
            	previousStage.close();
            	Scene scene = new Scene(page);
            	primaryStage.setScene(scene);
            	primaryStage.setTitle("Battleship");
            	primaryStage.show();
            	Main.setStage(primaryStage);
        	}

        }
    });
    
    returnGameButton.setOnAction(new EventHandler<ActionEvent>() {
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
