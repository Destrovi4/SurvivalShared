package xyz.destr.survival.io.action;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.rmi.server.UID;

import xyz.destr.survival.io.Synchronizable;
import xyz.destr.survival.io.SynchronizableArrayList;

public class ActorAction implements Synchronizable {

	public UID uid;
	public final SynchronizableArrayList<ActorCommand> commandList = new SynchronizableArrayList<>(ActorCommand.class);
		
	@Override
	public void clear() {
		uid = null;
		commandList.clear();
	}

	@Override
	public void read(DataInput in) throws IOException {
		uid = UID.read(in);
		commandList.read(in);
	}

	@Override
	public void write(DataOutput out) throws IOException {
		uid.write(out);
		commandList.write(out);
	}

}
