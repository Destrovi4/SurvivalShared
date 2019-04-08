package xyz.destr.rpg.entity.command;

public class EntityCommandMove implements EntityCommand {

	public int dx;
	public int dy;
	public int dz;
	
	@Override
	public EntityCommandType getCommandType() {
		return EntityCommandType.MOVE;
	}

	@Override
	public EntityCommand clone() {
		final EntityCommandMove clone = new EntityCommandMove();
		clone.copyOf(this);
		return clone;
	}

	public void copyOf(EntityCommandMove other) {
		this.dx = other.dx;
		this.dy = other.dy;
		this.dz = other.dz;
	}

}
