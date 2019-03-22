package xyz.destr.rpg.client;

import xyz.destr.rpg.entity.EntityBehavior;
import xyz.destr.rpg.entity.EntityInput;
import xyz.destr.rpg.entity.EntityOutput;
import xyz.destr.rpg.world.WorldDisplay;
import xyz.destr.rpg.world.cell.CellDisplay;

public class TestEntityBehavior implements EntityBehavior {

	@Override
	public void onOutput(EntityOutput output) {
		System.out.println("Entity: " + output.uuid);
		for(WorldDisplay world: output.worldOutputList){
			final int sizeX = world.sizeX;
			final int sizeY = world.sizeY;
			//final int sizeZ = world.sizeZ;
			final int sizeXY = sizeX * sizeY;
			//for(int z = 0; z < sizeZ; z++) {
			int z = 1;
				System.out.print("-----");
				System.out.print(z);
				System.out.println("-----");
				final int offsetZ = z * sizeXY;
				for(int y = 0; y < sizeY; y++) {
					final int offsetY = y * sizeX + offsetZ;
					for(int x = 0; x < sizeX; x++) {
						int index = x + offsetY;
						CellDisplay sell = world.cell[index];
						System.out.print(sell.fillMaterial.name().charAt(0));
						System.out.print(',');
					}
					System.out.println();
				}
			//}
		}
	}

	@Override
	public void onInput(EntityInput input) {
		input.move(1, 0, 0);
	}

}
