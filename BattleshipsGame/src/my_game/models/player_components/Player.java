/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my_game.models.player_components;
import java.net.InetAddress;

/**
 * Contains all information necessary to represent a player.
 */
public class Player implements java.io.Serializable {

	
    /* Player's identification. */
    private String username, password;	//password is not used at the moment
	       
    /* Address information of player. */
    private InetAddress ipAddress;
    private int playerPort;

    /* The unique identifier of this player. */
    public final int id;    
    
    private int playerStatus;
	
    public Player(String username, String password, InetAddress ipAddress, 
            int port, int pid, int status) {
		this.username = username;
		this.password = password;
		this.ipAddress = ipAddress;
		this.playerPort = port;
                this.id = pid;
                this.playerStatus = 0; // TO be changed?
	}
    
    //TODO Security?
    public String getUsername() {
        return username;
    }
	
    public String getPassword() {
        return password;
    }
    public InetAddress getIP() {
        return ipAddress;
    }
	
    public int getPort() {
        return playerPort;
    }
	
    public int getID() {
        return this.id;
    }
    
    public int getStatus() {
        return this.playerStatus;
    }

    @Override
    public int hashCode() {
        return (7 * this.id * this.username.hashCode());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Player)) {
            return false;
        }

        Player p = (Player) obj;
        return (this.id == p.id && this.username == p.username);
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(username + " ");
        sb.append("id:" + id + " ");
        sb.append(ipAddress.toString() + ":");
        sb.append(playerPort);
        return sb.toString();
    }
}
