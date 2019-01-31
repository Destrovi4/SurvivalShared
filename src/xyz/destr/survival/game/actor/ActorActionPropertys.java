package xyz.destr.survival.game.actor;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import xyz.destr.survival.io.CopyableTo;
import xyz.destr.survival.io.Synchronizable;

public abstract class ActorActionPropertys<T extends ActorActionPropertys<?>> implements Synchronizable, CopyableTo<T> {
	
	public float energyCost;
	public float actionPointsCost;
	
	@Override
	public void clear() {
		energyCost = 0;
		actionPointsCost = 0;
	}
	
	@Override
	public void read(DataInput in) throws IOException {
		energyCost = in.readFloat();
		actionPointsCost = in.readFloat();
	}
	
	@Override
	public void write(DataOutput out) throws IOException {
		out.writeFloat(energyCost);
		out.writeFloat(actionPointsCost);
	}

	@Override
	public void copyTo(T other) {
		other.energyCost = this.energyCost;
		other.actionPointsCost = this.actionPointsCost;
	}
}
