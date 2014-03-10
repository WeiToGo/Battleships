package my_game.gui.application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import my_game.models.game_components.CoralReef;
import my_game.networking.NetEntityListener;
import my_game.networking.packets.impl.CoralReefPacket;
import my_game.networking.server.GameServer;
import my_game.util.GameException;


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
    
    /** The coral reef used for generating the playing map. */
    private CoralReef reef;
    
    private final ServerListener serverListener = new ServerListener();
    private final ClientListener clientListener = new ClientListener();
    
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
                Stage primaryStage = new Stage();
                AnchorPane page = null;
                try {
                    page = (AnchorPane) FXMLLoader.load(Main.class.getResource("CreateGame.fxml"));
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                Stage previousStage = Main.getStage();
                previousStage.close();
                Scene scene = new Scene(page);
                primaryStage.setScene(scene);
                primaryStage.setTitle("Battleship");
                primaryStage.show();
                Main.setStage(primaryStage);
            }
        });

        if(Main.isServer) {
            //Start a server
            GameServer s = new GameServer(Main.getPlayer(), "DefaultServerName");  //TODO allow to choose server name
            Main.setServer(s);

            //Create the CoralReef and display it in the TextArea
            reef = new CoralReef();
            map.setText(reef.toString());
            
            Main.getServer().addNetListener(serverListener);
        } else {
            //it is a client connecting
            Main.getClient().addNetListener(clientListener);
        }
    }
    
    class ServerListener implements NetEntityListener {

        public void onConnected() {
            System.out.println("Sending coral reef to client.");
            Main.getServer().sendCoralReef(reef);
        }

        public void onReefReceive(CoralReef reef) {
            //Server is not supposed to receive this, something went wrong
            Logger.getLogger(GameConfirmation.class.getName()).log(Level.SEVERE, null, new GameException("CoralReef object received by server!"));
        }
        
    }
    
    class ClientListener implements NetEntityListener {

        public void onConnected() {
            //do nothing
        }

        public void onReefReceive(CoralReef reef) {
            //display the newly received coral reef in the text area
            map.setText(reef.toString());
        }
    }
}
