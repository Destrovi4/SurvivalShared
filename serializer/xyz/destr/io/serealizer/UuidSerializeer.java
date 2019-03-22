package xyz.destr.io.serealizer;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.UUID;

public class UuidSerializeer implements Serializer {

	@Override
	public void checkProtocol(DataInput in) throws IOException {
		if(UUID.class.getTypeName().equals(in.readUTF())) throw new IOException();
	}

	@Override
	public void writeProtocol(DataOutput out) throws IOException {
		out.writeUTF(UUID.class.getTypeName());
	}

	@Override
	public Object read(DataInput in) throws IOException {
		return new UUID(in.readLong(), in.readLong());
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
		UUID uuid = (UUID)obj;
		out.writeLong(uuid.getMostSignificantBits());
		out.writeLong(uuid.getLeastSignificantBits());
	}

}
