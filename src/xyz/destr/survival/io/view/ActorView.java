package xyz.destr.survival.io.view;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.rmi.server.UID;

import xyz.destr.survival.io.Synchronizable;
import xyz.destr.survival.io.SynchronizableArrayList;

public class ActorView implements Synchronizable {

	public UID uid;
	public boolean isDead;
	public int x;
	public int y;
	public float energy;
	
	public final TileRegionView region = new TileRegionView();
	public final SynchronizableArrayList<OtherActorView> otherActorList = new SynchronizableArrayList<>(OtherActorView.class);
		
	@Override
	public void clear() {
		uid = null;
		region.clear();
		otherActorList.clear();
	}

	@Override
	public void read(DataInput in) throws IOException {
		uid = UID.read(in);
		isDead = in.readBoolean();
		x = in.readInt();
		y = in.readInt();
		energy = in.readFloat();
		region.read(in);
		otherActorList.read(in);
	}

	@Override
	public void write(DataOutput out) throws IOException {
		uid.write(out);
		out.writeBoolean(isDead);
		out.writeInt(x);
		out.writeInt(y);
		out.writeFloat(energy);
		region.write(out);
		otherActorList.write(out);
	}

}
