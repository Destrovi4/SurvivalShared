package xyz.destr.rpg;

import java.util.Collection;

public class ServerMessage {
	public ServerMessageType type;
	public String text;
	
	public void copyOf(ServerMessage other) {
		this.type = other.type;
		this.text = other.text;
	}
	
	public static void copy(Collection<ServerMessage> to, Collection<ServerMessage> from) {
		to.clear();
		for(ServerMessage message: from) {
			to.add(clone(message));
		}
	}
	
	public static ServerMessage clone(ServerMessage message) {
		final ServerMessage clone = new ServerMessage();
		clone.copyOf(message);
		return clone;
	}
	
	public static ServerMessage info(String text) {
		final ServerMessage message = new ServerMessage();
		message.type = ServerMessageType.INFO;
		message.text = text;
		return message;
	}
}