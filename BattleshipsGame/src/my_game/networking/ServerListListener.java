/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my_game.networking;

/**
 *
 * @author Ivo
 */
public interface ServerListListener {
    
    /**
     * A method which is called every time a server has been found and its
     * description information retrieved.
     * @param si The information describing the server.
     */
    public void addServerInfo(ServerInfo si);
}
