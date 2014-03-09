package my_game.gui.application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import my_game.networking.ServerInfo;
import my_game.networking.ServerListListener;
import my_game.networking.client.GameClient;


public class JoinGame
    implements Initializable {

    @FXML //  fx:id="myButton"
    private Button joinGameButton; // Value injected by FXMLLoader
    
    @FXML //  fx:id="myButton"
    private Button returnGameButton; // Value injected by FXMLLoader
    
    @FXML //  fx:id="myButton"
    private ImageView background; // Value injected by FXMLLoader
    
    @FXML //  fx:id="myButton"
    private AnchorPane pane; // Value injected by FXMLLoader
    
    @FXML //  fx:id="myButton"
    private TableView<ServerInfo> taview; // Value injected by FXMLLoader


    @Override // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        
        Main.isServer = false;
        //Create a client
        GameClient c = new GameClient(Main.getPlayer());
        Main.setClient(c);
        
        //Get the list of servers
        
        final ObservableList<ServerInfo> list = FXCollections.observableArrayList();
        TableColumn serverNameCol = new TableColumn("Server Name");
        serverNameCol.setPrefWidth(168);
        serverNameCol.setCellValueFactory(new PropertyValueFactory<ServerInfo,String>("serverNameString"));

        TableColumn playerNameCol = new TableColumn("Player Name");
        playerNameCol.setPrefWidth(194);
        playerNameCol.setCellValueFactory(new PropertyValueFactory<ServerInfo,String>("playerNameString"));

        TableColumn ipAddressCol = new TableColumn("IP Address");
        ipAddressCol.setPrefWidth(251);
        ipAddressCol.setCellValueFactory(new PropertyValueFactory<ServerInfo,String>("ipAddressString"));
        taview.getColumns().addAll(serverNameCol, playerNameCol, ipAddressCol);
        taview.setItems(list);
        c.getLANServersList(new ServerListListener() {

            public void addServerInfo(ServerInfo si) {
                list.add(si);
            }
        });
        
        background.fitHeightProperty().bind(pane.heightProperty());
        background.fitWidthProperty().bind(pane.widthProperty());

        joinGameButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ServerInfo server = taview.getSelectionModel().getSelectedItem();
                Main.getClient().connect(server.ipAddress);
                
                Stage primaryStage = new Stage();
                AnchorPane page = null;
                try {
                    page = (AnchorPane) FXMLLoader.load(Main.class.getResource("GameConfirmation.fxml"));
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                
                if (server != null) {
                    Stage previousStage = Main.getStage();
                    previousStage.close();
                    Scene scene = new Scene(page);
                    primaryStage.setScene(scene);
                    primaryStage.setTitle("Battleship");
                    primaryStage.show();
                    Main.setStage(primaryStage);
                } else {
                    JOptionPane.showMessageDialog(null, "Please select a game", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        returnGameButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage primaryStage = new Stage();
                AnchorPane page = null;
                try {
                    page = (AnchorPane) FXMLLoader.load(Main.class.getResource("UI.fxml"));
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

    }
}
