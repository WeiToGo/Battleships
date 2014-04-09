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
import my_game.controller.Game;
import my_game.models.game_components.CoralReef;
import my_game.models.game_components.GameState;
import my_game.networking.NetEntityListener;
import my_game.networking.server.GameServer;
import my_game.util.GameException;


public class GameConfirm
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

    @FXML //  fx:id="myButton"
    private TextArea playerStatus; // Value injected by FXMLLoader
    
    /** The coral reef used for generating the playing map. */
    private CoralReef reef;
    
    private final ServerListener serverListener = new ServerListener();
    private final ClientListener clientListener = new ClientListener();
    
    private boolean otherPlayerVote = false, otherPlayerHasVoted = false;
    
    @Override // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        
        background.fitHeightProperty().bind(pane.heightProperty());
        background.fitWidthProperty().bind(pane.widthProperty());

        acceptGameButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //TODO if both players accept launch game and close window, else generate new map
                if(Main.isServer) {
                    Main.getServer().sendVote(true);
                } else {
                    Main.getClient().sendVote(true);
                }
                
                while(!otherPlayerHasVoted) {
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(GameConfirm.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                if(otherPlayerVote) {
                    //both players voted yes, start game
                    if(Main.isServer) {
                        System.out.println("Server will now start game.");
                        //first remove the listener from the server
                        Main.getServer().removeNetListener(serverListener);
                        //now create a new Game
                        Game g = new Game(Main.getPlayer(), Main.getServer().getConnectedPlayer(), 
                                reef, Main.getServer(), Game.PlayerType.Host, Main.getServer().getName(), 0);
                        //TODO Close this window.
                            Stage previousStage=Main.getStage();
                            previousStage.close();
                    } else {
                        System.out.println("Client will now start game.");
                        Main.getClient().removeNetListener(clientListener);
                        //now create a new Game
                        Game g = new Game(Main.getPlayer(), Main.getClient().getConnectedPlayer(), 
                                reef, Main.getClient(), Game.PlayerType.Client, "", 0);
                        //TODO Close this window.
                            Stage previousStage=Main.getStage();
                            previousStage.close();
                    }
                } else {
                    if(Main.isServer) {
                        sendNewReef();
                    } else {
                        //prepare for yet another vote as a client
                        otherPlayerHasVoted = false;
                    }
                }
            }
        });

        declineGameButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(Main.isServer) {
                    sendNewReef();
                } else {
                    //send decline to server
                    Main.getClient().sendVote(false);
                    otherPlayerHasVoted = false;
                }
            }
        });

        returnGameButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //non javafx stuff
                //close server/client on return
                if(Main.isServer) {
                    Main.getServer().stopServer();
                    Main.setServer(null);
                } else {
                    Main.getClient().stopClient();
                    Main.setClient(null);
                }
                //****************
                
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
            GameServer s = new GameServer(Main.getPlayer(), "DefaultServerName", false); 
            //TODO allow to choose server name
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

    /**
     * Generates a new reef on the server side and sends it to the clients
     * through the server. This method should not be called if the system is
     * running as a client.
     */
    private void sendNewReef() {
        reef.generateNewReef();
        map.setText(reef.toString());
        Main.getServer().sendVote(false);
        Main.getServer().sendCoralReefToListeners(reef);
        otherPlayerHasVoted = false;
    }
    
    
    class ServerListener implements NetEntityListener {

        public void onConnected() {
            System.out.println("Sending coral reef to client.");
            Main.getServer().sendCoralReefToListeners(reef);
            playerStatus.setText("A player has connected");
        }

        public void onReefReceive(CoralReef reef) {
            //Server is not supposed to receive this, something went wrong
            Logger.getLogger(GameConfirm.class.getName()).log(Level.SEVERE, null, new GameException("CoralReef object received by server!"));
        }

        public void onVoteReceive(boolean vote) {
            otherPlayerVote = vote;
            otherPlayerHasVoted = true;
            if(vote==true){
                playerStatus.setText("Player 2 has accepted the map");
            }
            
            else{
                playerStatus.setText("Player 2 has declined the map");
            }
        }

        public void onGameStateReceive(GameState gs) {
            //this method shouldn't get called while here
            Logger.getLogger(GameConfirm.class.getName()).log(Level.SEVERE, null, new GameException("GameState received in GameConfirmation.java"));
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

        public void onVoteReceive(boolean vote) {
            otherPlayerVote = vote;
            otherPlayerHasVoted = true;
            if(vote==true){
                playerStatus.setText("Player 1 has accepted the map");
            }
            
            else{
                playerStatus.setText("Player 1 has declined the map");
            }
        }

        public void onGameStateReceive(GameState gs) {
            //this method shouldn't get called while here
            Logger.getLogger(GameConfirm.class.getName()).log(Level.SEVERE, null, new GameException("GameState received in GameConfirmation.java"));
        }
    }
}