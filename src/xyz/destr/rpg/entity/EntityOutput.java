package xyz.destr.rpg.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import xyz.destr.io.serealizer.SerialazeInnerTypes;
import xyz.destr.rpg.world.WorldDisplay;

public class EntityOutput {
	
	public UUID uuid;
	
	@SerialazeInnerTypes(types = {WorldDisplay.class})
	public final ArrayList<WorldDisplay> worldOutputList = new ArrayList<>();
	
	public void clear() {
		uuid = null;
		worldOutputList.clear();
	}
	
	public void copyOf(EntityOutput other) {
		this.uuid = other.uuid;
		WorldDisplay.copy(this.worldOutputList, other.worldOutputList);
	}
	
	@Override
	public EntityOutput clone() {
		final EntityOutput clone = new EntityOutput();
		clone.copyOf(this);
		return clone;
	}
	
	public void merge(EntityOutput other) {
		if(!other.uuid.equals(uuid)) {
			throw new RuntimeException("Wrong merging " + other.uuid + " to " + uuid);
		}
		for(WorldDisplay worldDisplay: other.worldOutputList) {
			worldOutputList.add(worldDisplay.clone());
		}
	}
	
	public static void copy(Collection<EntityOutput> to, Collection<EntityOutput> from) {
		to.clear();
		for(EntityOutput output: from) {
			to.add(output.clone());
		}
	}
	
}