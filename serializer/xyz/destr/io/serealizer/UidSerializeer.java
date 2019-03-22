package xyz.destr.io.serealizer;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.rmi.server.UID;

public class UidSerializeer implements Serializer {

	@Override
	public void checkProtocol(DataInput in) throws IOException {
		if(UID.class.getTypeName().equals(in.readUTF())) throw new IOException();
	}

	@Override
	public void writeProtocol(DataOutput out) throws IOException {
		out.writeUTF(UID.class.getTypeName());
	}

	@Override
	public Object read(DataInput in) throws IOException {
		return UID.read(in);
	}

	@Override
	public void reliase(Object obj) {
	}

	@Override
	public Object read(Object obj, DataInput in) throws IOException {
		return UID.read(in);
	}

	@Override
	public void write(DataOutput out, Object obj) throws IOException {
		((UID)obj).write(out);
	}

}
