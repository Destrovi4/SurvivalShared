package xyz.destr.survival.io.view;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import xyz.destr.survival.game.world.GroundType;
import xyz.destr.survival.game.world.TileObjectType;
import xyz.destr.survival.io.Synchronizable;

public class TileRegionView implements Synchronizable {
	
	public int x;
	public int y;
	public int width;
	public int height;
	
	public GroundType[] goundData;
	public TileObjectType[] objectData;
	
	public boolean inBounds(int tileX, int tileY) {
		return !(tileX < x || tileY < y || x + width >= tileX || y + height >= tileY);
	}
	
	public int toIndex(int tileX, int tileY) {
		return (tileX - x) + (tileY - y) * width;
	}
	
	public void setRect(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		setSize(width * height);
	}
	
	public void setSize(int size) {
		goundData = goundData == null || goundData.length < size ? new GroundType[size] : goundData;
		objectData = objectData == null || objectData.length < size ? new TileObjectType[size] : objectData;
	}
	
	@Override
	public void clear() {
		x = 0;
		y = 0;
		width = 0;
		height = 0;
	}
	
	@Override
	public void read(DataInput in) throws IOException {
		this.x = in.readInt();
		this.y = in.readInt();
		this.width = in.readInt();
		this.height = in.readInt();
		final int size = width * height;
		setSize(size);
		GroundType[] groundTypeValues = GroundType.values();
		TileObjectType[] objectTypeValues = TileObjectType.values();
		
		for(int i = 0; i < size; i++) {
			goundData[i] = groundTypeValues[Math.max(0, in.readByte())];
			objectData[i] = objectTypeValues[Math.max(0, in.readByte())];
		}
	}
	
	@Override
	public void write(DataOutput out) throws IOException {
		out.writeInt(x);
		out.writeInt(y);
		out.writeInt(width);
		out.writeInt(height);
		
		for(int i = 0, count = goundData.length; i < count; i++) {
			GroundType groundType = goundData[i];
			out.writeByte(groundType == null ? 0 : groundType.ordinal());
			TileObjectType objectType = objectData[i];
			out.writeByte(objectType == null ? 0 : objectType.ordinal());
		}
	}
}
