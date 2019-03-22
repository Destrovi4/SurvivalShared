package xyz.destr.io.serealizer.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Random;

import org.junit.Test;

import xyz.destr.io.serealizer.SerializerManager;
import xyz.destr.io.serealizer.Serializer.ObjectArraySerializer;

public class ObjectArrayTest {

	static final Random random = new Random(0);
	
	static class Data {
		int i;
	}
	
	@Test
	public void test() throws IOException {
		final Data[] a = new Data[10];
		for(int i = 0; i < a.length; i++) {
			final Data data = new Data();
			data.i = random.nextInt();
			a[i] = data;
		}
		final SerializerManager manager = new SerializerManager();
		manager.addDefaultSerializers();
		final ObjectArraySerializer serializer = new ObjectArraySerializer(manager, Data[].class);
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		serializer.write(new DataOutputStream(baos), a);
		final Data[] b = (Data[])serializer.read(new DataInputStream(new ByteArrayInputStream(baos.toByteArray())));
		assertNotNull(b);
		assertEquals(a.length, b.length);
		for(int i = 0; i < a.length; i++) {
			if(a[i] == null) {
				assertNull(b[i]);
			} else {
				assertEquals(a[i].i, b[i].i);
			}
		}
	}
	
}
