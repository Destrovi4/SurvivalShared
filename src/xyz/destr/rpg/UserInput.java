package xyz.destr.rpg;

import java.util.ArrayList;

import xyz.destr.io.serealizer.SerialazeInnerTypes;
import xyz.destr.rpg.entity.EntityInput;

public class UserInput {

	@SerialazeInnerTypes(types = {EntityInput.class})
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
