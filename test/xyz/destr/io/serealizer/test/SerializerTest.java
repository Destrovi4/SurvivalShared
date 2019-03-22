package xyz.destr.io.serealizer.test;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Random;

import org.junit.Test;

import xyz.destr.io.serealizer.Serializer;

public class SerializerTest {
	
	static final Random random = new Random();
	
	static void setSeed(long seed) {
		random.setSeed(seed);
	}
	static boolean randomBoolean() {
		return random.nextBoolean();
	}
	static char randomChar() {
		return (char)random.nextInt(Character.MAX_VALUE);
	}
	static char[] randomCharArray(int length) {
		char[] arr = new char[length];
		for(int i = 0; i < length; i++) arr[i] = randomChar();
		return arr;
	}
	static byte randomByte() {
		return (byte)random.nextInt(Byte.MAX_VALUE);
	}
	static byte[] randomByteArray(int length) {
		byte[] arr = new byte[length];
		for(int i = 0; i < length; i++) arr[i] = randomByte();
		return arr;
	}
	static short randomShort() {
		return (short)random.nextInt(Short.MAX_VALUE);
	}
	static int randomInt() {
		return (int)random.nextInt(Integer.MAX_VALUE);
	}
	static long randomLong() {
		return random.nextLong();
	}
	static float randomFloat() {
		return random.nextFloat();
	}
	static double randomDouble() {
		return random.nextDouble();
	}
	static TestEnum randomEnum() {
		TestEnum[] values = TestEnum.values();
		return values[random.nextInt(values.length - 1)];
	}
	static void fillRandom(SerializerTest data, long seed) {
		setSeed(seed);
		data.boolean0 = randomBoolean();
		data.enum0 = randomEnum();
		data.boolean1 = randomBoolean();
		data.char0 = randomChar();
		data.byte0 = randomByte();
		data.byteArray0 = randomByteArray(10);
		data.short0 = randomShort();
		data.int0 = randomInt();
		data.long0 = randomLong();
		data.float0 = randomFloat();
		data.double0 = randomDouble();
	}
	
	static enum TestEnum {
		A,
		B,
		C;
	}
	
	boolean boolean0;
	Boolean boolean1;
	TestEnum enum0;
	char char0;
	byte byte0;
	byte[] byteArray0;
	short short0;
	int int0;
	long long0;
	float float0;
	double double0;
	SerializerTest subData;
	//@SerialazeTypes(types = { String.class } )
	//ArrayList<Object> list;
	
	public void fillRandom() {
		setSeed(0);
		
	}
	
	@Test
	public void test() throws Exception {
		fillRandom(this, 0);
		this.subData = new SerializerTest();
		fillRandom(subData, 0);
		
		Serializer serializer = Serializer.get(SerializerTest.class);
		
		{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			serializer.writeProtocol(new DataOutputStream(baos));
			serializer.checkProtocol(new DataInputStream(new ByteArrayInputStream(baos.toByteArray())));
		}
		
		SerializerTest other = new SerializerTest();
		{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			serializer.write(new DataOutputStream(baos), this);
			serializer.read(other, new DataInputStream(new ByteArrayInputStream(baos.toByteArray())));
		}
				
		testEquals(other);
	}
	
	private void testEquals(SerializerTest other) {
		assertEquals(this.boolean0, other.boolean0);
		assertEquals(this.boolean1, other.boolean1);
		
		assertEquals(this.enum0, other.enum0);
		assertEquals(this.char0, other.char0);
		assertEquals(this.byte0, other.byte0);
		assertEquals(this.short0, other.short0);
		assertEquals(this.int0, other.int0);
		assertEquals(this.long0, other.long0);
		assertEquals(this.float0, other.float0);
		assertEquals(this.double0, other.double0);
		
		if(this.subData == null) {
			assertNull(other.subData);
		} else {		
			this.subData.testEquals(other.subData);
		}
	}
	
}
