package xyz.destr.rpg.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import xyz.destr.rpg.entity.command.EntityCommand;
import xyz.destr.rpg.entity.command.EntityCommandMove;
import xyz.destr.rpg.entity.skill.SkillInput;
import xyz.destr.rpg.entity.skill.StaticSkill;
import xyz.destr.rpg.world.WorldDirection;

public class EntityInput implements Serializable {

	private static final long serialVersionUID = 3080082052617896796L;

	public UUID uuid;
	
	public ArrayList<EntityCommand> inputList = new ArrayList<>();
	public ArrayList<SkillInput> skillInputList = new ArrayList<>();
	
	public void clear() {
		this.uuid = null;
		this.inputList.clear();
		this.skillInputList.clear();
	}
	
	public void copyOf(EntityInput other) {
		this.uuid = other.uuid;
		EntityCommand.copy(inputList, other.inputList);
		SkillInput.copy(skillInputList, other.skillInputList);
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
		return inputList.isEmpty() && skillInputList.isEmpty();
	}
	
	public void useSkill(UUID uuid, Object... args) {
		SkillInput skillInput = new SkillInput(uuid, SkillInput.USE);
		for(Object arg: args) {
			skillInput.addArgument(arg);
		}
		skillInputList.add(skillInput);
	}
	
	public void move(int dx, int dy, int dz) {
		EntityCommandMove move = new EntityCommandMove();
		move.dx = dx;
		move.dy = dy;
		move.dz = dz;
		inputList.add(move);
	}
	
	public void move(WorldDirection direction) {
		useSkill(StaticSkill.MOVE, direction);
	}
	
}