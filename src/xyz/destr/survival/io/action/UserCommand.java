package xyz.destr.survival.io.action;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import xyz.destr.survival.io.Properties;
import xyz.destr.survival.io.Synchronizable;

public class UserCommand implements Synchronizable {
	
	public static enum Type {
		CREATE_ACTOR_TYPE,
		EXIT;
	};

	public Type type;
	public final Properties propertys = new Properties();
	
	@Override
	public void clear() {
		propertys.clear();
	}

	@Override
	public void read(DataInput in) throws IOException {
		type = Type.values()[in.readInt()];
		propertys.read(in);
	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeInt(type.ordinal());
		propertys.write(out);
	}

}
