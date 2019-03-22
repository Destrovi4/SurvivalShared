package xyz.destr.io.serealizer;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.lang.reflect.Array;
import java.rmi.server.UID;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;

public interface Serializer {
	
	public static void registerDeafultSerializers(SerializerManager manager) {
		manager.registerSerializerClass(Object.class, DefaultObjectSerializer.class);
		manager.registerSerializerClass(Boolean.class, BooleanObjectSerializer.class);
		manager.registerSerializerClass(Character.class, CharacterObjectSerializer.class);
		manager.registerSerializerClass(Short.class, ShortObjectSerializer.class);
		manager.registerSerializerClass(Integer.class, IntegerObjectSerializer.class);
		manager.registerSerializerClass(Long.class, LongObjectSerializer.class);
		manager.registerSerializerClass(Float.class, FloatObjectSerializer.class);
		manager.registerSerializerClass(Double.class, DoubleObjectSerializer.class);
		
		manager.registerSerializerClass(boolean[].class, BooleanArraySerializer.class);
		manager.registerSerializerClass(char[].class, CharArraySerializer.class);
		manager.registerSerializerClass(byte[].class, ByteArraySerializer.class);
		manager.registerSerializerClass(short[].class, ShortArraySerializer.class);
		manager.registerSerializerClass(int[].class, IntArraySerializer.class);
		manager.registerSerializerClass(long[].class, LongArraySerializer.class);
		manager.registerSerializerClass(float[].class, FloatArraySerializer.class);
		manager.registerSerializerClass(double[].class, DoubleArraySerializer.class);
	}
	
	public static void registerAdditionalSerializers(SerializerManager manager) {
		manager.registerSerializerClass(String.class, StringSerializeer.class);
		manager.registerSerializerClass(ArrayList.class, ArrayLsitSerializeer.class);
		manager.registerSerializerClass(UID.class, UidSerializeer.class);
		manager.registerSerializerClass(UUID.class, UuidSerializeer.class);
	}
		
	public static Serializer get(Class<?> clazz) {
		return SerializerManager.getInstance().get(clazz);
	}
	
	public static Serializer get(SerializerOptions options) {
		return SerializerManager.getInstance().get(options);
	}
		
	public void checkProtocol(DataInput in) throws IOException;
	
	public void writeProtocol(DataOutput out) throws IOException;
	
	public Object read(DataInput in) throws IOException;
	
	public void reliase(Object obj);
	
	public Object read(Object obj, DataInput in) throws IOException;
	
	public void write(DataOutput out, Object obj) throws IOException;
	
	static abstract class BasicSerializer implements Serializer {

		protected final Class<?> clazz;
		
		protected BasicSerializer(Class<?> clazz){
			this.clazz = clazz;
		}
		
		protected void checkType(DataInput in) throws IOException {
			if(!clazz.getTypeName().equals(in.readUTF())) {
				throw new IOException();
			}
		}
		
		protected void writeType(DataOutput out) throws IOException {
			out.writeUTF(clazz.getTypeName());
		}
		
		@Override
		public void checkProtocol(DataInput in) throws IOException {
			checkType(in);
		}

		@Override
		public void writeProtocol(DataOutput out) throws IOException {
			writeType(out);
		}		
	}
	
	static abstract class WithInnerSerializer extends BasicSerializer implements HaveInnerSerializer {
		
		protected final Serializer serializer;
		
		protected WithInnerSerializer(Class<?> clazz, Serializer serializer) {
			super(clazz);
			this.serializer = serializer;
		}
		
		@Override
		public void checkProtocol(DataInput in) throws IOException {
			checkProtocol(in, new HashSet<Object>());
		}

		@Override
		public void writeProtocol(DataOutput out) throws IOException {
			writeProtocol(out, new HashSet<Object>());
		}
		
		@Override
		public void checkProtocol(DataInput in, HashSet<Object> checked) throws IOException {
			checkType(in);
			if(serializer instanceof HaveInnerSerializer) {
				((HaveInnerSerializer)serializer).checkProtocol(in, checked);
			} else {
				serializer.checkProtocol(in);
			}
		}

		@Override
		public void writeProtocol(DataOutput out, HashSet<Object> checked) throws IOException {
			writeType(out);
			if(serializer instanceof HaveInnerSerializer) {
				((HaveInnerSerializer)serializer).writeProtocol(out, checked);
			} else {
				serializer.writeProtocol(out);
			}
		}
		
	}
	
	static class ObjectArraySerializer extends WithInnerSerializer {
		
		private static Serializer getArrayComponentSerializer(SerializerManager manager, Class<?> clazz) {
			if(!clazz.isArray()) throw new AssertionError();
			return manager.get(clazz.getComponentType());
		}
		
		public ObjectArraySerializer(SerializerManager manager, Class<?> clazz) {
			super(clazz, getArrayComponentSerializer(manager, clazz));
		}
		
		protected Object[] allocateArray(int length) {
			return (Object[])Array.newInstance(clazz.getComponentType(), length);
		}
		
		protected void reliaseArray(Object[] array) {
			
		}

		@Override
		public Object read(DataInput in) throws IOException {
			final int length = in.readInt();
			final Object[] array = allocateArray(length);
			for(int i = 0; i < length; i++) {
				if(in.readBoolean()) {
					array[i] = serializer.read(in);
				}
			}
			return array;
		}

		@Override
		public void reliase(Object obj) {
			reliaseArray((Object[])obj);
		}

		@Override
		public Object read(Object obj, DataInput in) throws IOException {
			final Object[] array = (Object[])obj;
			final int length = in.readInt();
			final Serializer serializer = this.serializer;
			if(array.length != length) {
				reliaseArray(array);
				final Object[] newArray = allocateArray(length);
				for(int i = 0; i < length; i++) {
					if(in.readBoolean()) {
						newArray[i] = serializer.read(in);
					}
				}
				return newArray;
			} else {
				for(int i = 0; i < length; i++) {
					final Object data = array[i];
					if(in.readBoolean()) {
						if(data == null) {
							array[i] = serializer.read(in);
						} else {
							array[i] = serializer.read(obj, in);
						}
					} else {
						if(data != null) {
							array[i] = null;
							serializer.reliase(data);
						}
					}
				}
			}
			return array;
		}

		@Override
		public void write(DataOutput out, Object obj) throws IOException {
			final Object[] array = (Object[])obj;
			final int length = array.length;
			out.writeInt(length);
			final Serializer serializer = this.serializer;
			for(int i = 0; i < length; i++) {
				final Object data = array[i];
				if(data == null) {
					out.writeBoolean(false);
				} else {
					out.writeBoolean(true);
					serializer.write(out, data);
				}
			}
		}
			
	}
	
	static abstract class Unreusable extends BasicSerializer {
		protected Unreusable(Class<?> clazz) {
			super(clazz);
		}
		@Override
		public void reliase(Object obj) {
		}
		@Override
		public Object read(Object obj, DataInput in) throws IOException {
			reliase(obj);
			return read(in);
		}	
	}
	
	static class DefaultObjectSerializer extends Unreusable {
		protected DefaultObjectSerializer() {
			super(Object.class);
		}
		@Override
		public Object read(DataInput in) throws IOException {
			return new Object();
		}
		@Override
		public void write(DataOutput out, Object obj) throws IOException {
		}
	}
	
	static class BooleanObjectSerializer extends Unreusable {
		protected BooleanObjectSerializer() {
			super(Boolean.class);
		}
		@Override
		public Object read(DataInput in) throws IOException {
			return (boolean)in.readBoolean();
		}
		@Override
		public void write(DataOutput out, Object obj) throws IOException {
			out.writeBoolean((boolean)obj);
		}
	}
	
	static class CharacterObjectSerializer extends Unreusable {
		protected CharacterObjectSerializer() {
			super(Character.class);
		}
		@Override
		public Object read(DataInput in) throws IOException {
			return (char)in.readChar();
		}
		@Override
		public void write(DataOutput out, Object obj) throws IOException {
			out.writeChar((char)obj);
		}
	}
	
	static class ShortObjectSerializer extends Unreusable {
		protected ShortObjectSerializer() {
			super(Short.class);
		}
		@Override
		public Object read(DataInput in) throws IOException {
			return (short)in.readShort();
		}
		@Override
		public void write(DataOutput out, Object obj) throws IOException {
			out.writeShort((short)obj);
		}
	}
	
	static class IntegerObjectSerializer extends Unreusable {
		protected IntegerObjectSerializer() {
			super(Integer.class);
		}
		@Override
		public Object read(DataInput in) throws IOException {
			return (int)in.readInt();
		}
		@Override
		public void write(DataOutput out, Object obj) throws IOException {
			out.writeInt((int)obj);
		}
	}
	
	static class LongObjectSerializer extends Unreusable {
		protected LongObjectSerializer() {
			super(Long.class);
		}
		@Override
		public Object read(DataInput in) throws IOException {
			return (long)in.readLong();
		}
		@Override
		public void write(DataOutput out, Object obj) throws IOException {
			out.writeLong((long)obj);
		}
	}
	
	static class FloatObjectSerializer extends Unreusable {
		protected FloatObjectSerializer() {
			super(Float.class);
		}
		@Override
		public Object read(DataInput in) throws IOException {
			return (float)in.readFloat();
		}
		@Override
		public void write(DataOutput out, Object obj) throws IOException {
			out.writeFloat((float)obj);
		}
	}
	
	static class DoubleObjectSerializer extends Unreusable {
		protected DoubleObjectSerializer() {
			super(Double.class);
		}
		@Override
		public Object read(DataInput in) throws IOException {
			return (double)in.readDouble();
		}
		@Override
		public void write(DataOutput out, Object obj) throws IOException {
			out.writeDouble((double)obj);
		}
	}
	
	static class EnumSerializer extends Unreusable {
		
		public Enum<?>[] values;
		
		protected EnumSerializer(Class<?> clazz) {
			super(clazz);
			if(!clazz.isEnum()) new AssertionError();
			try {
				values = (Enum<?>[])clazz.getMethod("values").invoke(clazz);
			} catch (Exception e) {
				throw new AssertionError(e);
			}
		}

		@Override
		public Object read(DataInput in) throws IOException {
			return values[in.readInt()];
		}

		@Override
		public void write(DataOutput out, Object obj) throws IOException {
			out.writeInt(((Enum<?>)obj).ordinal());
		}
		
		@Override
		public void checkProtocol(DataInput in) throws IOException {
			super.checkProtocol(in);
			if(values.length != in.readInt()) throw new IOException();
			for(Enum<?> e: values) {
				if(!e.name().equals(in.readUTF())) {
					throw new IOException();
				}
			}
		}

		@Override
		public void writeProtocol(DataOutput out) throws IOException {
			super.writeProtocol(out);
			out.writeInt(values.length);
			for(Enum<?> e: values) {
				out.writeUTF(e.name());
			}			
		}
		
	}
	
	static abstract class PimitiveArraySerializer extends BasicSerializer {

		protected PimitiveArraySerializer(Class<?> clazz) {
			super(clazz);
			if(!clazz.isArray()) throw new AssertionError();
			if(!clazz.getComponentType().isPrimitive()) throw new AssertionError();
		}

		@Override
		public Object read(DataInput in) throws IOException {
			final Object array = allocateArray(in.readInt());
			readArray(array, in);
			return array;
		}

		@Override
		public void reliase(Object obj) {
		}
		
		@Override
		public void write(DataOutput out, Object obj) throws IOException {
			out.writeInt(Array.getLength(obj));
			writeArray(out, obj);
		}

		@Override
		public Object read(Object obj, DataInput in) throws IOException {
			final int length = in.readInt();
			if(Array.getLength(obj) != length) {
				reliase(obj);
				final Object array = allocateArray(length);
				readArray(array, in);
				return array;
			} else {
				readArray(obj, in);
				return obj;
			}
		}
		
		abstract protected Object allocateArray(int length);				
		abstract protected void writeArray(DataOutput out, Object obj) throws IOException;
		abstract protected void readArray(Object obj, DataInput in) throws IOException;
	}
	
	static class BooleanArraySerializer extends PimitiveArraySerializer {
		protected BooleanArraySerializer() {
			super(boolean[].class);
		}
		@Override
		protected Object allocateArray(int length) {
			return new boolean[length];
		}
		@Override
		public void writeArray(DataOutput out, Object obj) throws IOException {
			final boolean[] array = (boolean[])obj;
			final int length = array.length;
			for(int i = 0; i < length; i++) {
				out.writeBoolean(array[i]);
			}
		}
		@Override
		protected void readArray(Object obj, DataInput in) throws IOException {
			final boolean[] array = (boolean[])obj;
			final int length = array.length;
			for(int i = 0; i < length; i++) {
				array[i] = in.readBoolean();
			}
		}
	}
	
	static class CharArraySerializer extends PimitiveArraySerializer {
		protected CharArraySerializer() {
			super(char[].class);
		}
		@Override
		protected Object allocateArray(int length) {
			return new char[length];
		}
		@Override
		public void writeArray(DataOutput out, Object obj) throws IOException {
			final char[] array = (char[])obj;
			final int length = array.length;
			for(int i = 0; i < length; i++) {
				out.writeChar(array[i]);
			}
		}
		@Override
		protected void readArray(Object obj, DataInput in) throws IOException {
			final char[] array = (char[])obj;
			final int length = array.length;
			for(int i = 0; i < length; i++) {
				array[i] = in.readChar();
			}
		}
	}
	
	static class ByteArraySerializer extends PimitiveArraySerializer {
		protected ByteArraySerializer() {
			super(byte[].class);
		}
		@Override
		protected Object allocateArray(int length) {
			return new byte[length];
		}
		@Override
		public void writeArray(DataOutput out, Object obj) throws IOException {
			final byte[] array = (byte[])obj;
			final int length = array.length;
			for(int i = 0; i < length; i++) {
				out.writeByte(array[i]);
			}
		}
		@Override
		protected void readArray(Object obj, DataInput in) throws IOException {
			final byte[] array = (byte[])obj;
			final int length = array.length;
			for(int i = 0; i < length; i++) {
				array[i] = in.readByte();
			}
		}
	}
	
	static class ShortArraySerializer extends PimitiveArraySerializer {
		protected ShortArraySerializer() {
			super(short[].class);
		}
		@Override
		protected Object allocateArray(int length) {
			return new short[length];
		}
		@Override
		public void writeArray(DataOutput out, Object obj) throws IOException {
			final short[] array = (short[])obj;
			final int length = array.length;
			for(int i = 0; i < length; i++) {
				out.writeShort(array[i]);
			}
		}
		@Override
		protected void readArray(Object obj, DataInput in) throws IOException {
			final short[] array = (short[])obj;
			final int length = array.length;
			for(int i = 0; i < length; i++) {
				array[i] = in.readShort();
			}
		}
	}
	
	static class IntArraySerializer extends PimitiveArraySerializer {
		protected IntArraySerializer() {
			super(int[].class);
		}
		@Override
		protected Object allocateArray(int length) {
			return new int[length];
		}
		@Override
		public void writeArray(DataOutput out, Object obj) throws IOException {
			final int[] array = (int[])obj;
			final int length = array.length;
			for(int i = 0; i < length; i++) {
				out.writeInt(array[i]);
			}
		}
		@Override
		protected void readArray(Object obj, DataInput in) throws IOException {
			final int[] array = (int[])obj;
			final int length = array.length;
			for(int i = 0; i < length; i++) {
				array[i] = in.readInt();
			}
		}
	}
	
	static class LongArraySerializer extends PimitiveArraySerializer {
		protected LongArraySerializer() {
			super(long[].class);
		}
		@Override
		protected Object allocateArray(int length) {
			return new long[length];
		}
		@Override
		public void writeArray(DataOutput out, Object obj) throws IOException {
			final long[] array = (long[])obj;
			final int length = array.length;
			for(int i = 0; i < length; i++) {
				out.writeLong(array[i]);
			}
		}
		@Override
		protected void readArray(Object obj, DataInput in) throws IOException {
			final long[] array = (long[])obj;
			final int length = array.length;
			for(int i = 0; i < length; i++) {
				array[i] = in.readLong();
			}
		}
	}
	
	static class FloatArraySerializer extends PimitiveArraySerializer {
		protected FloatArraySerializer() {
			super(float[].class);
		}
		@Override
		protected Object allocateArray(int length) {
			return new float[length];
		}
		@Override
		public void writeArray(DataOutput out, Object obj) throws IOException {
			final float[] array = (float[])obj;
			final int length = array.length;
			for(int i = 0; i < length; i++) {
				out.writeFloat(array[i]);
			}
		}
		@Override
		protected void readArray(Object obj, DataInput in) throws IOException {
			final float[] array = (float[])obj;
			final int length = array.length;
			for(int i = 0; i < length; i++) {
				array[i] = in.readFloat();
			}
		}
	}
	
	static class DoubleArraySerializer extends PimitiveArraySerializer {
		protected DoubleArraySerializer() {
			super(double[].class);
		}
		@Override
		protected Object allocateArray(int length) {
			return new double[length];
		}
		@Override
		public void writeArray(DataOutput out, Object obj) throws IOException {
			final double[] array = (double[])obj;
			final int length = array.length;
			for(int i = 0; i < length; i++) {
				out.writeDouble(array[i]);
			}
		}
		@Override
		protected void readArray(Object obj, DataInput in) throws IOException {
			final double[] array = (double[])obj;
			final int length = array.length;
			for(int i = 0; i < length; i++) {
				array[i] = in.readDouble();
			}
		}
	}	
}
