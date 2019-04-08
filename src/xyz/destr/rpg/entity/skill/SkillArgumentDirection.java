package xyz.destr.rpg.entity.skill;

import xyz.destr.rpg.world.WorldDirection;

public class SkillArgumentDirection implements SkillArgument {

	public final WorldDirection direction;
	
	public SkillArgumentDirection() {
		direction = WorldDirection.NOWHERE;
	}
	
	public SkillArgumentDirection(WorldDirection direction) {
		this.direction = direction;
	}
	
	@Override
	public SkillArgumentType getType() {
		return SkillArgumentType.DIRECTION;
	}	
	
	@Override
	public SkillArgumentDirection clone() {
		return new SkillArgumentDirection(direction);
	}
	
}
