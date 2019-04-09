package xyz.destr.rpg.entity.skill;

import xyz.destr.rpg.world.WorldDirection;

public class SkillArgumentDirection implements SkillArgument {

	private static final long serialVersionUID = 5303843981493570068L;
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
