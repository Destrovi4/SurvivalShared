package xyz.destr.rpg;

import java.io.Serializable;
import java.util.ArrayList;

import xyz.destr.rpg.entity.EntityOutput;

public class UserOutput implements Serializable {
	
	private static final long serialVersionUID = 6099377942302426588L;

	public int tick;	
	
	public ArrayList<ServerMessage> messageList = new ArrayList<>();
	public ArrayList<EntityOutput> entityOutputList = new ArrayList<>();
	
	public void clear() {
		tick = 0;
		messageList.clear();
		entityOutputList.clear();
	}
	
	public void copyOf(UserOutput other) {
		this.tick = other.tick;
		ServerMessage.copy(messageList, other.messageList);
		EntityOutput.copy(entityOutputList, other.entityOutputList);
	}
	
	public void addEntityOutput(EntityOutput entityOutput) {
		entityOutputList.add(entityOutput);
	}
	
	public void sendMessage(ServerMessage message) {
		messageList.add(message);
	}

	public void sendMessage(String text) {
		sendMessage(ServerMessage.info(text));
	}
}