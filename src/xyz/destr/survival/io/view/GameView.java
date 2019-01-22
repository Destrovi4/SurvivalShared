package xyz.destr.survival.io.view;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import xyz.destr.survival.io.Synchronizable;

public class GameView implements Synchronizable {

	public int tick;
	public final UserView user = new UserView();
	
	@Override
	public void clear() {
		tick = 0;
		user.clear();
	}
	
	@Override
	public void read(DataInput in) throws IOException {
		tick = in.readInt();
		user.read(in);
	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeInt(tick);
		user.write(out);
	}

}
