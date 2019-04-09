package xyz.destr.rpg.world;

import java.io.Serializable;
import java.util.Collection;

import xyz.destr.rpg.world.cell.CellDisplay;

public class WorldDisplay implements Serializable {

	private static final long serialVersionUID = -2837211145423871009L;
	
	public int x;
	public int y;
	public int z;
	public int sizeX;
	public int sizeY;
	public int sizeZ;
	public CellDisplay[] cell;
	
	public int toIndex(int x, int y, int z) {
		return (z - this.z) * sizeX * sizeY + (y - this.y) * sizeX + (x - this.x);
	}
	
	public boolean inBounds(int x, int y, int z) {
		return !(x < 0 || y < 0 || z < 0 || x >= sizeX || y >= sizeY || z >= sizeZ);
	}
	
	public CellDisplay getCell(int x, int y, int z) {
		if(inBounds(x, y, z)) {
			return cell[toIndex(x, y, z)];
		} else {
			throw new OutOfMemoryError(String.format("(x:%d; y:%d; z:%d) out of (x:%d; y:%d; z:%d; sizeX:%d; sizeY:%d; sizeZ:%d)", x, y, z, this.x, this.y, this.z, sizeX, sizeY, sizeZ));
		}
	}
	
	public void clear() {
		this.x = 0;
		this.y = 0;
		this.z = 0;
		this.sizeX = 0;
		this.sizeY = 0;
		this.sizeZ = 0;
		this.cell = null;
	}
	
	public void copyOf(WorldDisplay other) {
		this.x = other.x;
		this.y = other.y;
		this.z = other.z;
		this.sizeX = other.sizeX;
		this.sizeY = other.sizeY;
		this.sizeZ = other.sizeZ;
		if(other.cell == null) {
			this.cell = null;
		} else if(this.cell == null || this.cell.length != other.cell.length) {
			this.cell = new CellDisplay[other.cell.length];
			CellDisplay.copy(this.cell, other.cell);
		}
	}
	
	@Override
	public WorldDisplay clone() {
		final WorldDisplay clone = new WorldDisplay();
		clone.copyOf(this);
		return clone;
	}
	
	public static void copy(Collection<WorldDisplay> to, Collection<WorldDisplay> from) {
		to.clear();
		for(WorldDisplay world: from) {
			to.add(world.clone());
		}
	}
		
}
