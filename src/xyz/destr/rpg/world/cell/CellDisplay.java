package xyz.destr.rpg.world.cell;

import xyz.destr.rpg.world.MaterialType;
import xyz.destr.rpg.world.WorldDirection;

public class CellDisplay {
	public MaterialType fillMaterial;
	public MaterialType eastSurface;
	public MaterialType southSurface;
	public MaterialType westSurface;
	public MaterialType northSurface;
	public MaterialType topSurface;
	public MaterialType bottomSurface;
	
	public void clear() {
		fillMaterial = MaterialType.EMPTY;
		eastSurface = MaterialType.EMPTY;
		southSurface = MaterialType.EMPTY;
		westSurface = MaterialType.EMPTY;
		northSurface = MaterialType.EMPTY;
		topSurface = MaterialType.EMPTY;
		bottomSurface = MaterialType.EMPTY;
	}
	
	public void copyOf(CellDisplay other) {
		this.fillMaterial	= other.fillMaterial;
		this.eastSurface	= other.eastSurface;
		this.southSurface	= other.southSurface;
		this.westSurface	= other.westSurface;
		this.northSurface	= other.northSurface;
		this.topSurface		= other.topSurface;
		this.bottomSurface	= other.bottomSurface;
	}
	
	@Override
	public CellDisplay clone() {
		final CellDisplay clone = new CellDisplay();
		clone.copyOf(this);
		return clone;
	}
	
	public MaterialType getSurfaceMaterial(WorldDirection direction) {
		switch(direction) {
		case EAST: return eastSurface;
		case SOUTH: return southSurface;
		case WEST: return westSurface;
		case NORTH: return northSurface;
		case TOP: return topSurface;
		case BOTTOM: return bottomSurface;
		default:
			throw new RuntimeException();
		}
	}

	public void setSurfaceMaterial(WorldDirection direction, MaterialType sideMaterial) {
		switch(direction) {
		case EAST:
			eastSurface = sideMaterial;
		break;
		case SOUTH:
			southSurface = sideMaterial;
		break;
		case WEST:
			westSurface = sideMaterial;
		break;
		case NORTH:
			northSurface = sideMaterial;
		break;
		case TOP:
			topSurface = sideMaterial;
		break;
		case BOTTOM:
			bottomSurface = sideMaterial;
		break;
		default:
			throw new RuntimeException();
		}
	}
	
	public static void copy(CellDisplay[] to, CellDisplay[] from) {
		if(to.length != from.length) throw new RuntimeException();
		for(int i = 0, length = to.length; i < length; i++) {
			final CellDisplay cellFrom = from[i];
			if(cellFrom == null) {
				to[i] = null;
			} else {
				final CellDisplay cellTo = to[i];
				if(cellTo == null) {
					to[i] = cellFrom.clone();
				} else {
					cellTo.copyOf(cellFrom);
				}
			}			
		}
	}
	
}
