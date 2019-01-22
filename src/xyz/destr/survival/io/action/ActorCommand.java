package xyz.destr.survival.io.action;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import xyz.destr.survival.io.Properties;
import xyz.destr.survival.io.Synchronizable;

public class ActorCommand implements Synchronizable  {

	public static enum Type {
		IDLE,
		MOVE,
		EAT_TILE_OBJECT,
		BREED
	}
	
	public Type type;
	public final Properties properties = new Properties();
	
	@Override
	public void clear() {
		type = Type.IDLE;
		properties.clear();
	}

	@Override
	public void read(DataInput in) throws IOException {
		type = Type.values()[in.readByte()];
		properties.read(in);
	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeByte(type.ordinal());
		properties.write(out);
	}

}
