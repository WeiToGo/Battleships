/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my_game.models.player_components;

/**
 * @author wei
 */
public class Users {
    private String name;
    private String password;
    
    public Users(String n, String p){
        this.name = n;
        this.password = p;
    }
    public String getUsername(){
        return this.name;
    }
    public String getPassword(){
        return this.password;
    }
    
}
