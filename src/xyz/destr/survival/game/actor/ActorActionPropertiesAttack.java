package xyz.destr.survival.game.actor;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

final public class ActorActionPropertiesAttack extends ActorActionPropertys<ActorActionPropertiesAttack> {

	public float damage;
	
	@Override
	public void clear() {
		super.clear();
		damage = 0;
	}
	
	@Override
	public void read(DataInput in) throws IOException {
		super.read(in);
		damage = in.readFloat();
	}
	
	@Override
	public void write(DataOutput out) throws IOException {
		super.write(out);
		out.writeFloat(damage);
	}

	@Override
	public void copyTo(ActorActionPropertiesAttack other) {
		super.copyTo(other);
		other.damage = this.damage;
	}
	
}
