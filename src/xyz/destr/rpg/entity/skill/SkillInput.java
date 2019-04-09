package xyz.destr.rpg.entity.skill;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

import xyz.destr.rpg.world.MaterialType;
import xyz.destr.rpg.world.WorldDirection;

public class SkillInput implements Serializable {
	
	private static final long serialVersionUID = 5197605991771593729L;
	
	public static final int USE = 1<<1;
	public static final int CANCEL = 1<<2;
	public static final int ENABLE = 1<<3;
	public static final int DISABLE = 1<<4;
	
	public UUID uuid;
	public int action;
	
	public ArrayList<SkillArgument> argumentList = new ArrayList<>();

	public SkillInput() {
	}
	
	public SkillInput(UUID uuid, int action) {
		this.uuid = uuid;
		this.action = action;
	}
	
	@Override
	public SkillInput clone() {
		SkillInput clone = new SkillInput(uuid, action);
		SkillArgument.copy(clone.argumentList, argumentList);
		return clone;
	}
	
	public void use() {
		action |= USE;
	}
	
	public void cancel() {
		action |= CANCEL;
	}
	
	public void enable() {
		action |= ENABLE;
	}
	
	public void disable() {
		action |= DISABLE;
	}
	
	public void addArgument(Object object) {
		if(object instanceof WorldDirection) {
			argumentList.add(new SkillArgumentDirection((WorldDirection)object));
		} else if(object instanceof MaterialType) {
			argumentList.add(new SkillArgumentMaterialType((MaterialType)object));
		} else {
			throw new RuntimeException("Wrong argument type " + object.getClass());
		}
	}
	
	public static void copy(ArrayList<SkillInput> to, ArrayList<SkillInput> from) {
		to.clear();
		for(SkillInput skillInput: from) {
			to.add(skillInput.clone());
		}
	}
	
}
