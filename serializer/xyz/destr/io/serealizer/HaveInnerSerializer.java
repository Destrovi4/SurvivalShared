package xyz.destr.io.serealizer;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.HashSet;

interface HaveInnerSerializer {
	public void checkProtocol(DataInput in, HashSet<Object> checked) throws IOException;
	public void writeProtocol(DataOutput out, HashSet<Object> checked) throws IOException;
}
