/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my_game.util;

/**
 * A custom exception type for game related exceptions.
 * @author Ivo
 */
public class GameException extends Exception {
  public GameException() { super(); }
  public GameException(String message) { super(message); }
  public GameException(String message, Throwable cause) { super(message, cause); }
  public GameException(Throwable cause) { super(cause); }
  
}
