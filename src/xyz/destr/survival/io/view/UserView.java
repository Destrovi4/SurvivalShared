package xyz.destr.survival.io.view;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import xyz.destr.survival.io.Synchronizable;
import xyz.destr.survival.io.SynchronizableArrayList;

public class UserView implements Synchronizable {

	public final SynchronizableArrayList<UserMessage> messageList = new SynchronizableArrayList<>(UserMessage.class);
	public final SynchronizableArrayList<ActorTypeView> actorTypeList = new SynchronizableArrayList<>(ActorTypeView.class);
	
	public void clear() {
		messageList.clear();
	}
	
	@Override
	public void read(DataInput in) throws IOException {
		messageList.read(in);
		actorTypeList.read(in);
	}

	@Override
	public void write(DataOutput out) throws IOException {
		messageList.write(out);
		actorTypeList.write(out);
	}

}
