package xyz.destr.survival.io.view;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.UUID;

import xyz.destr.survival.io.Synchronizable;

public class OtherActorView implements Synchronizable {

	public UUID uuid;
	public int x;
	public int y;
	
	@Override
	public void clear() {
		x = 0;
		y = 0;
	}

	@Override
	public void read(DataInput dis) throws IOException {
		uuid = new UUID(dis.readLong(), dis.readLong());
		x = dis.readInt();
		y = dis.readInt();
	}

	@Override
	public void write(DataOutput dos) throws IOException {
		dos.writeLong(uuid.getMostSignificantBits());
		dos.writeLong(uuid.getLeastSignificantBits());
		dos.writeInt(x);
		dos.writeInt(y);
	}

}
