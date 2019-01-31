package xyz.destr.survival.io.view;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.UUID;

import xyz.destr.survival.game.actor.ActorProperties;
import xyz.destr.survival.io.Synchronizable;
import xyz.destr.survival.io.SynchronizableArrayList;

public class ActorTypeView implements Synchronizable {

	public UUID uuid;
	public final ActorProperties properties = new ActorProperties();
	public final SynchronizableArrayList<ActorView> actorList = new SynchronizableArrayList<>(ActorView.class);
		
	@Override
	public void clear() {
		uuid = null;
		actorList.clear();
	}
	
	@Override
	public void read(DataInput in) throws IOException {
		uuid = new UUID(in.readLong(), in.readLong());
		actorList.read(in);
	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeLong(uuid.getMostSignificantBits());
		out.writeLong(uuid.getLeastSignificantBits());
		actorList.write(out);
	}

}
