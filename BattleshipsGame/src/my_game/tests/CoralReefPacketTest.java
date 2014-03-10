/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my_game.tests;

import my_game.models.game_components.CoralReef;
import my_game.networking.packets.impl.CoralReefPacket;

/**
 *
 * @author Ivo
 */
public class CoralReefPacketTest {
    
    public static void main(String[] args) {
        CoralReef reef = new CoralReef();
        System.out.println("Generated reef: \n" + reef);
        CoralReefPacket packet = new CoralReefPacket(reef);
        byte[] data = packet.getData();
        //now generate a new packet out of the data and check if the obtained reef matches the initial one
        packet = new CoralReefPacket(data);
        reef.setReef(packet.reef);
        System.out.println("Received reef: \n" + reef);
    }
}
