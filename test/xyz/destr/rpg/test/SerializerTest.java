package xyz.destr.rpg.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Random;

import org.junit.Test;

import xyz.destr.io.serealizer.Serializer;
import xyz.destr.io.serealizer.SerializerManager;
import xyz.destr.rpg.world.MaterialType;
import xyz.destr.rpg.world.WorldDisplay;
import xyz.destr.rpg.world.cell.CellDisplay;

public class SerializerTest {
	
	private static Random random = new Random(0);
	
	public static Object writeRead(Object object) throws IOException {
		final Serializer serialiser = SerializerManager.getInstance().get(object.getClass());
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		serialiser.write(new DataOutputStream(baos), object);
		return serialiser.read(new DataInputStream(new ByteArrayInputStream(baos.toByteArray())));
	}
	
	private static MaterialType randomMaterial() {
		return MaterialType.VALUES[random.nextInt(MaterialType.VALUES.length)];
	}
	
	private static CellDisplay randomCellDisplay() {
		final CellDisplay cell = new CellDisplay();
		cell.fillMaterial = randomMaterial();
		cell.eastSurface = randomMaterial();
		cell.southSurface = randomMaterial();
		cell.westSurface = randomMaterial();
		cell.northSurface = randomMaterial();
		cell.topSurface = randomMaterial();
		cell.bottomSurface = randomMaterial();
		return cell;
	}
	
	private static CellDisplay[] randomCellDisplayArray(int length) {
		final CellDisplay[] array = new CellDisplay[length];
		for(int i = 0; i < length; i++) {
			array[i] = randomCellDisplay();
		}
		return array;
	}
	
	private static void _assertEquals(CellDisplay expected, CellDisplay actual) {
		assertEquals(expected.fillMaterial, actual.fillMaterial);
		assertEquals(expected.eastSurface, actual.eastSurface);
		assertEquals(expected.southSurface, actual.southSurface);
		assertEquals(expected.westSurface, actual.westSurface);
		assertEquals(expected.northSurface, actual.northSurface);
		assertEquals(expected.topSurface, actual.topSurface);
		assertEquals(expected.bottomSurface, actual.bottomSurface);
	}
	
	private static void _assertEquals(CellDisplay[] expected, CellDisplay[] actual) {
		assertEquals(expected.length, actual.length);
		for(int i = 0, length = expected.length; i < length; i++) {
			_assertEquals(expected[i], actual[i]);
		}
	}
	
	@Test
	public void testCellDisplay() throws IOException {
		final CellDisplay a = randomCellDisplay();		
		final CellDisplay b = (CellDisplay)writeRead(a);
		_assertEquals(a, b);
	}
	
	@Test
	public void testCellDisplayArray() throws IOException {
		final CellDisplay[] a = randomCellDisplayArray(10);		
		final CellDisplay[] b = (CellDisplay[])writeRead(a);
		_assertEquals(a, b);
	}
	
	@Test
	public void testWorldOutput() throws IOException {
		final WorldDisplay a = new WorldDisplay();
		a.x = 1;
		a.y = 2;
		a.z = 3;
		a.sizeX = 4;
		a.sizeY = 5;
		a.sizeZ = 6;
		final WorldDisplay b = (WorldDisplay)writeRead(a);
		assertEquals(a.x, b.x);
		assertEquals(a.y, b.y);
		assertEquals(a.z, b.z);
		assertEquals(a.sizeX, b.sizeX);
		assertEquals(a.sizeY, b.sizeY);
		assertEquals(a.sizeZ, b.sizeZ);
	}
	
	//WorldOutput
}
