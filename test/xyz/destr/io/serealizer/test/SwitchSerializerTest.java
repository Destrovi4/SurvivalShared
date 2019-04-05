package xyz.destr.io.serealizer.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.junit.Test;

import xyz.destr.io.serealizer.SerialazeTypes;
import xyz.destr.io.serealizer.Serializer;
import xyz.destr.io.serealizer.SerializerManager;
import xyz.destr.io.serealizer.SerializerOptions;
import xyz.destr.io.serealizer.SwitchSerializer;

public class SwitchSerializerTest {
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
		@SerialazeTypes(types = {DataB.class, DataC.class})
		DataA data0;
		@SerialazeTypes(types = {DataB.class, DataC.class})
		DataA data1;
		@SerialazeTypes(types = {DataB.class, DataC.class})
		DataA data2;
	}
	SerializerManager manager = new SerializerManager();
	{
		manager.addAdditionalSerializers();
		manager.addDefaultSerializers();
	}
	
	static void readWrite(Serializer serializer, Object in, Object out) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		serializer.write(new DataOutputStream(baos), in);
		serializer.read(out, new DataInputStream(new ByteArrayInputStream(baos.toByteArray())));
	}
	
	
	@Test
	public void test0() throws IOException {
		SwitchSerializer serializer = new SwitchSerializer();
		serializer.init(
			new SerializerOptions(
				new Class<?>[] {DataB.class, DataC.class},
				new Class<?>[0]
			),
			SerializerManager.getInstance()
		);
		
		DataB inDataB = new DataB(123456);		
		DataB outDataB = new DataB();
		readWrite(serializer, inDataB, outDataB);
		assertEquals(inDataB.data, outDataB.data);
		
		DataC inDataC = new DataC(123.456f);
		DataC outDataC = new DataC();
		readWrite(serializer, inDataC, outDataC);
		assertEquals(inDataC.data, outDataC.data);
	}
	
	@Test
	public void test1() throws IOException {
		Serializer serializer = manager.get(TestData.class);
		TestData dataIn = new TestData();
		dataIn.data0 = null;
		dataIn.data1 = new DataB(123456);
		dataIn.data2 = new DataC(123.456f);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		serializer.write(new DataOutputStream(baos), dataIn);
		
		TestData dataOut = new TestData();
		serializer.read(dataOut, new DataInputStream(new ByteArrayInputStream(baos.toByteArray())));
		assertEquals(((DataB)dataIn.data1).data, ((DataB)dataOut.data1).data);
		assertEquals(((DataC)dataIn.data2).data, ((DataC)dataOut.data2).data);		
	}
}
