package xyz.destr.io.serealizer;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class StringSerializeer implements Serializer {

	@Override
	public void checkProtocol(DataInput in) throws IOException {
		if(String.class.getTypeName().equals(in.readUTF())) throw new IOException();
	}

	@Override
	public void writeProtocol(DataOutput out) throws IOException {
		out.writeUTF(String.class.getTypeName());
	}

	@Override
	public Object read(DataInput in) throws IOException {
		return in.readUTF();
	}

	@Override
	public void reliase(Object obj) {		
	}

	@Override
	public Object read(Object obj, DataInput in) throws IOException {
		return read(in);
	}

	@Override
	public void write(DataOutput out, Object obj) throws IOException {
		out.writeUTF((String)obj);
	}

}
