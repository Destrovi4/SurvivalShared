package xyz.destr.survival.game.actor;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import xyz.destr.survival.io.CopyableTo;
import xyz.destr.survival.io.Synchronizable;

final public class ActorProperties implements Synchronizable, CopyableTo<ActorProperties> {
	
	public final ActorActionPropertiesMove actionMove = new ActorActionPropertiesMove();
	public final ActorActionPropertyesEatTile actionEatTile = new ActorActionPropertyesEatTile();
	public final ActorActionPropertysBreed actionBreed = new ActorActionPropertysBreed();
	public final ActorActionPropertiesAttack actionAttack = new ActorActionPropertiesAttack();
	
	@Override
	public void clear() {
		actionMove.clear();
		actionEatTile.clear();
		actionBreed.clear();
		actionAttack.clear();
	}

	@Override
	public void read(DataInput in) throws IOException {
		actionMove.read(in);
		actionEatTile.read(in);
		actionBreed.read(in);
		actionAttack.read(in);
	}

	@Override
	public void write(DataOutput out) throws IOException {
		actionMove.write(out);
		actionEatTile.write(out);
		actionBreed.write(out);
		actionAttack.write(out);
	}

	@Override
	public void copyTo(ActorProperties other) {
		this.actionMove.copyTo(other.actionMove);
		this.actionEatTile.copyTo(other.actionEatTile);
		this.actionBreed.copyTo(other.actionBreed);
		this.actionAttack.copyTo(other.actionAttack);
	}
}
