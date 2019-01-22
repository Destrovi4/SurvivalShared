package xyz.destr.survival.io.action;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.UUID;

import xyz.destr.survival.io.Synchronizable;
import xyz.destr.survival.io.SynchronizableArrayList;

public class ActorTypeAction implements Synchronizable {

	public UUID typeUUID;
	
	public final SynchronizableArrayList<ActorAction> actorList = new SynchronizableArrayList<>(ActorAction.class);
	
	@Override
	public void clear() {
		typeUUID = null;
		actorList.clear();
	}

	@Override
	public void read(DataInput in) throws IOException {
		typeUUID = new UUID(in.readLong(), in.readLong());
		actorList.read(in);
	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeLong(typeUUID.getMostSignificantBits());
		out.writeLong(typeUUID.getLeastSignificantBits());
		actorList.write(out);
	}

}
