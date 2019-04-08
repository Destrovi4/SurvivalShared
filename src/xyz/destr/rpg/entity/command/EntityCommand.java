package xyz.destr.rpg.entity.command;

import java.util.Collection;

public interface EntityCommand {
	
	public EntityCommandType getCommandType();
	
	public EntityCommand clone();
	
	public static void copy(Collection<EntityCommand> to, Collection<EntityCommand> from) {
		to.clear();
		for(EntityCommand entityCommand: from) {
			to.add(entityCommand.clone());
		}
	}
	
}
