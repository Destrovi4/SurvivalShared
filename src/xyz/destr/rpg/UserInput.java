package xyz.destr.rpg;

import java.io.Serializable;
import java.util.ArrayList;

import xyz.destr.rpg.entity.EntityInput;

public class UserInput implements Serializable {

	private static final long serialVersionUID = -7390866463013948876L;

	public ArrayList<EntityInput> entityInputList = new ArrayList<>();
	
	public void clear() {
		entityInputList.clear();
	}
	
	public void copyOf(UserInput other) {
		entityInputList.clear();
		EntityInput.copy(this.entityInputList, other.entityInputList);
	}

	public void addEntityInput(EntityInput input) {
		entityInputList.add(input);
	}
	
}
