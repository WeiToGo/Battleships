package my_game.networking.packets.impl;

import java.awt.Color;

import my_game.networking.packets.Packet;

public class GameStatePacket extends Packet {

	public float x, y, radius;
	public Color color;
	
	
	public GameStatePacket(byte[] data) {
		super(PacketTypes.GAMESTATE.getId());
		
		String message = readData(data);
		String[] args = message.split("~");
		//get all data in order
		x = Float.parseFloat(args[0]);
		y = Float.parseFloat(args[1]);
		radius = Float.parseFloat(args[2]);
		float r = Float.parseFloat(args[3]);
		float g = Float.parseFloat(args[4]);
		float b = Float.parseFloat(args[5]);
		
		color = new Color(r, g, b, 1);
	}
	
	public GameStatePacket(float x, float y, float radius, Color color) {
		super(PacketTypes.GAMESTATE.getId());
		
		this.x = x;
		this.y = y;
		this.radius = radius;
		this.color = color;
	}

	@Override
	public byte[] getData() {
		return ("03" + x + "~" + y + "~" + radius + "~" + getColorString()).getBytes();
	}

	/**
	 * Converts the colour saved in this game state
	 * packet into the string "R~G~B" where R, G and B
	 * are the red, green and blue values of the colour in
	 * the range 0.0f - 1.0f
	 * @return
	 */
	private String getColorString() {
		StringBuilder str = new StringBuilder();
		str.append(color.getRed() / 255f);
		str.append("~");
		str.append(color.getGreen() / 255f);
		str.append("~");
		str.append(color.getBlue() / 255f);
		str.append("~");
		return str.toString();
	}

}
