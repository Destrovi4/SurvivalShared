package xyz.destr.survival.io.view;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import xyz.destr.survival.io.Synchronizable;

public class UserMessage implements Synchronizable {

	public static enum Type {
		INFO,
		WARNING,
		ERROR
	}
	
	public Type type;
	public String text;
	
	@Override
	public void clear() {
		type = Type.INFO;
		text = "";
	}
	
	@Override
	public void read(DataInput in) throws IOException {
		type = Type.values()[in.readByte()];
		this.text = in.readUTF();
	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeByte(type == null ? 0 : type.ordinal());
		out.writeUTF(text);
	}
	
	
	
}
