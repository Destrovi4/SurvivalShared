package xyz.destr.rpg.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import xyz.destr.io.serealizer.SerialazeInnerTypes;

public class EntityInput {

	public UUID uuid;
	
	@SerialazeInnerTypes(types = {EntityCommandMove.class})
	public ArrayList<EntityCommand> inputList = new ArrayList<>();
	
	public void clear() {
		this.uuid = null;
		this.inputList.clear();
	}
	
	public void copyOf(EntityInput other) {
		this.uuid = other.uuid;
		EntityCommand.copy(inputList, other.inputList);
	}
	
	public EntityInput clone() {
		final EntityInput clone = new EntityInput();
		clone.copyOf(this);
		return clone;
	}
	
	public static void copy(Collection<EntityInput> to, Collection<EntityInput> from) {
		to.clear();
		for(EntityInput worldInput: from) {
			to.add(worldInput.clone());
		}
	}
	
	public boolean isEmpty() {
		return inputList.isEmpty();
	}
	
	public void move(int dx, int dy, int dz) {
		EntityCommandMove move = new EntityCommandMove();
		move.dx = dx;
		move.dy = dy;
		move.dz = dz;
		inputList.add(move);
	}
	
}