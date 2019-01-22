package xyz.destr.survival.io;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public interface Synchronizable {
	public void clear();
	public void read(DataInput in) throws IOException;
	public void write(DataOutput out) throws IOException;
}
