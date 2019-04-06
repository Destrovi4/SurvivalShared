package xyz.destr.rpg.client;

import java.util.UUID;

import xyz.destr.rpg.entity.EntityBehavior;
import xyz.destr.rpg.entity.EntityInput;
import xyz.destr.rpg.entity.EntityOutput;
import xyz.destr.rpg.world.MaterialType;
import xyz.destr.rpg.world.WorldDisplay;
import xyz.destr.rpg.world.cell.CellDisplay;

public class WaxworkClient implements Runnable {
	
	protected EntityBehavior behavior;
	
	public WaxworkClient(EntityBehavior behavior) {
		this.behavior = behavior;
	}

	protected WorldDisplay generateWorldDisplay() {
		WorldDisplay display = new WorldDisplay();
		int ex = display.x = 2;
		int ey = display.y = 2;
		int ez = display.z = 1;
		int visionRangeX = 3;
		int visionRangeY = 3;
		int visionRangeZ = 1;
		int sizeX = display.sizeX = 1 + 2 * visionRangeX;
		int sizeY = display.sizeY = 1 + 2 * visionRangeY;
		int sizeZ = display.sizeZ = 1 + 2 * visionRangeZ;
		display.cell = new CellDisplay[sizeX * sizeY * sizeZ];
		
		final int sizeXY = sizeX * sizeY;
		for(int dz = 0; dz < sizeZ; dz++) {
			final int offsetZ = dz * sizeXY;
			for(int dy = 0; dy < sizeY; dy++) {
				final int offsetZY = offsetZ + dy * sizeX;
				for(int dx = 0; dx < sizeX; dx++) {
					final int index = dx + offsetZY;
					final CellDisplay cellDisplay = new CellDisplay();
					display.cell[index] = cellDisplay;
					final int x = ex + dx - visionRangeX;
					final int y = ey + dy - visionRangeY;
					final int z = ez + dz - visionRangeZ;					
					
					MaterialType material;
					
					if(z <= 0) {
						material = MaterialType.SOIL;
					} else {
						if(x > 2 && x < 10 && y > 2 && y < 10) {
							material = MaterialType.SOIL;
						} else {
							material = MaterialType.EMPTY;
						}
					}
					
					cellDisplay.fillMaterial	= material;
					cellDisplay.eastSurface		= material;
					cellDisplay.southSurface	= material;
					cellDisplay.westSurface		= material;
					cellDisplay.northSurface	= material;
					cellDisplay.topSurface		= material;
					cellDisplay.bottomSurface	= material;
				}
			}
		}
		
		return display;
	}
	
	@Override
	public void run() {
		UUID entityUUID = new UUID(0L,0L);
		EntityInput input = new EntityInput();
		EntityOutput output = new EntityOutput();
		output.worldOutputList.add(generateWorldDisplay());
		
		while(!Thread.interrupted()) {
			input.uuid = entityUUID;
			behavior.onInput(input);
			if(!entityUUID.equals(input.uuid)) {
				throw new RuntimeException("Missing uuid in input");
			}
			input.clear();
			output.uuid = entityUUID;
			behavior.onOutput(output);
			if(!entityUUID.equals(output.uuid)) {
				throw new RuntimeException("Missing uuid in output");
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}
