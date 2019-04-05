package xyz.destr.io.serealizer.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;

import org.junit.Test;

import xyz.destr.io.serealizer.Serializer;

public class PrimitiveTest {
	
	protected static <T> T transfer(Class<T> clazz, T data) throws IOException {
		Serializer serializer = Serializer.get(clazz);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutput out = new DataOutputStream(baos);
		serializer.write(out, data);
		DataInput input = new DataInputStream(new ByteArrayInputStream(baos.toByteArray()));
		Object result = serializer.read(input);
		return clazz.cast(result);
	}
	
	public static class IntData {
		public int i;
	}
	
	@Test
	public void testInt() throws IOException {
		IntData expected = new IntData();
		expected.i = 12345;
		IntData actual = transfer(IntData.class, expected);
		assertEquals(expected.i, actual.i);
	}
	
	public static class ObjectData {
		public IntData intData;
	}
	
	@Test
	public void testObject() throws IOException {
		ObjectData expected = new ObjectData();
		expected.intData = new IntData();
		expected.intData.i = 12345;
		ObjectData actual = transfer(ObjectData.class, expected);
		assertNotNull(actual.intData);
		assertEquals(expected.intData.i, actual.intData.i);
	}
	
}
