package xyz.destr.rpg;

import java.util.ArrayList;

import xyz.destr.io.serealizer.SerialazeInnerTypes;
import xyz.destr.rpg.entity.EntityOutput;

public class UserOutput {
	public int tick;	
	
	@SerialazeInnerTypes(types = {ServerMessage.class})
	public ArrayList<ServerMessage> messageList = new ArrayList<>();
	
	@SerialazeInnerTypes(types = {EntityOutput.class})
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