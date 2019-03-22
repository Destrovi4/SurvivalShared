package xyz.destr.io.serealizer.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.Test;

import xyz.destr.io.serealizer.SerialazeInnerTypes;
import xyz.destr.io.serealizer.Serializer;
import xyz.destr.io.serealizer.SerializerManager;

public class ArrayListSerealizerTest {

	public static class DataA {}
	public static class DataB extends DataA {
		public int data;
		public DataB() {}
		public DataB(int data) {
			this.data = data;
		}
	}
	public static class DataC extends DataA {
		public float data;
		public DataC() {}
		public DataC(float data) {
			this.data = data;
		}
	}
	
	public static class TestData {
		@SerialazeInnerTypes(types = {DataB.class, DataC.class})
		public ArrayList<DataA> list = new ArrayList<>();
	}
	
	Serializer serializer = SerializerManager.getInstance().get(TestData.class);
	
	@Test
	public void test() throws IOException {
		TestData dataIn = new TestData();
		dataIn.list.add(new DataB(123));
		dataIn.list.add(new DataB(234));
		dataIn.list.add(new DataC(34.5f));
		dataIn.list.add(null);
		dataIn.list.add(new DataB(456));
		dataIn.list.add(new DataC(78.9f));
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		serializer.write(new DataOutputStream(baos), dataIn);
		
		TestData dataOut = new TestData();
		serializer.read(dataOut, new DataInputStream(new ByteArrayInputStream(baos.toByteArray())));
		
		final ArrayList<?> listIn = dataIn.list;
		final ArrayList<?> listOut = dataOut.list;
		final int size = listIn.size();
		assertEquals(size, listOut.size());
		for(int i = 0; i < size; i++) {
			final Object objectIn = listIn.get(i);
			final Object objectOut = listOut.get(i);
			if(objectIn == null) {
				assertNull(objectOut);
			} else {
				assertEquals(objectIn.getClass(), objectOut.getClass());
				if(objectIn instanceof DataB) {
					assertEquals(((DataB)objectIn).data, ((DataB)objectOut).data);
				} else if(objectIn instanceof DataC) {
					assertEquals(((DataC)objectIn).data, ((DataC)objectOut).data);
				} else {
					throw new RuntimeException();
				}
			}
		}
	}
	
}
