package xyz.destr.survival.io.action;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import xyz.destr.survival.io.Synchronizable;
import xyz.destr.survival.io.SynchronizableArrayList;

public class UserAction implements Synchronizable {

	public final SynchronizableArrayList<UserCommand> commandList = new SynchronizableArrayList<>(UserCommand.class);
	public final SynchronizableArrayList<ActorTypeAction> actorTypeList = new SynchronizableArrayList<>(ActorTypeAction.class);
		
	@Override
	public void clear() {
		commandList.clear();
		actorTypeList.clear();
	}

	@Override
	public void read(DataInput in) throws IOException {
		commandList.read(in);
		actorTypeList.read(in);
	}

	@Override
	public void write(DataOutput out) throws IOException {
		commandList.write(out);
		actorTypeList.write(out);
	}

}
