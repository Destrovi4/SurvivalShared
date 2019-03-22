package xyz.destr.io.serealizer;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class ObjectSerializer implements Serializer, HaveInnerSerializer {
	
	protected Class<?> clazz;
	protected Field[] charFields;
	protected Field[] byteFields;
	protected Field[] shortFields;
	protected Field[] intFields;
	protected Field[] longFields;
	protected Field[] floatFields;
	protected Field[] doubleFields;
	protected Field[] booleanFields;
	
	protected Serializer[] objectFieldSerializer;
	protected Field[][] objectFields;
	
	protected void init(Class<?> clazz, SerializerManager manager) {
		if(clazz == null) throw new NullPointerException();
		this.clazz = clazz;
		
		ArrayList<Field> booleanFieldList = new ArrayList<>();
		ArrayList<Field> charFieldList = new ArrayList<>();
		ArrayList<Field> byteFieldList = new ArrayList<>();
		ArrayList<Field> shortFieldList = new ArrayList<>();
		ArrayList<Field> intFieldList = new ArrayList<>();
		ArrayList<Field> longFieldList = new ArrayList<>();
		ArrayList<Field> floatFieldList = new ArrayList<>();
		ArrayList<Field> doubleFieldList = new ArrayList<>();
				
		HashMap<Class<?>, ArrayList<Field>> primitiveFieldListByClass = new HashMap<>();
		primitiveFieldListByClass.put(boolean.class, booleanFieldList);
		primitiveFieldListByClass.put(char.class, charFieldList);
		primitiveFieldListByClass.put(byte.class, byteFieldList);
		primitiveFieldListByClass.put(short.class, shortFieldList);
		primitiveFieldListByClass.put(int.class, intFieldList);
		primitiveFieldListByClass.put(long.class, longFieldList);
		primitiveFieldListByClass.put(float.class, floatFieldList);
		primitiveFieldListByClass.put(double.class, doubleFieldList);
		
		ArrayList<Serializer> serializerList = new ArrayList<>();
		HashMap<Serializer, ArrayList<Field>> fieldListBySerializer = new HashMap<>();
				
		for(Field field: clazz.getDeclaredFields()) {
			final int modifiers = field.getModifiers();
			if(Modifier.isStatic(modifiers)) continue;			
			if(field.isSynthetic()) continue;
			
			final Class<?> fieldType = field.getType();
			ArrayList<Class<?>> typeList = null;
			ArrayList<Class<?>> innerTypeList = null;
			if(fieldType.isPrimitive()) {
				primitiveFieldListByClass.get(fieldType).add(field);
			} else {
				if(typeList == null) typeList = new ArrayList<>();
				typeList.clear();
				if(innerTypeList == null) innerTypeList = new ArrayList<>();
				innerTypeList.clear();
				for(Annotation annotation: field.getAnnotations()) {
					if(annotation instanceof SerialazeTypes) {
						for(Class<?> type: ((SerialazeTypes)annotation).types()) {
							typeList.add(type);
						}
					} else if(annotation instanceof SerialazeInnerTypes) {
						for(Class<?> type: ((SerialazeInnerTypes)annotation).types()) {
							innerTypeList.add(type);
						}
					}
				}
				
				final Serializer serializer;
				if(innerTypeList.size() == 0 && typeList.size() == 0) {
					serializer = manager.get(fieldType);
				} else {
					serializer = manager.get(new SerializerOptions(
						typeList.size() == 0 
						? new Class<?>[] {fieldType}
						: typeList.toArray(new Class<?>[typeList.size()]),
						innerTypeList.toArray(new Class<?>[innerTypeList.size()])
					));
				}
				
				if(serializer == null) throw new AssertionError("Can't get serealizer for field " + field);
				
				ArrayList<Field> fieldList = fieldListBySerializer.get(serializer);
				if(fieldList == null) {
					fieldList = new ArrayList<>();
					fieldListBySerializer.put(serializer, fieldList);
				}
				fieldList.add(field);
				serializerList.add(serializer);
			}
		}
		
		booleanFields	= booleanFieldList.toArray(new Field[booleanFieldList.size()]);
		charFields		= charFieldList.toArray(new Field[charFieldList.size()]);
		byteFields		= byteFieldList.toArray(new Field[byteFieldList.size()]);
		shortFields		= shortFieldList.toArray(new Field[shortFieldList.size()]);
		intFields		= intFieldList.toArray(new Field[intFieldList.size()]);
		longFields		= longFieldList.toArray(new Field[longFieldList.size()]);
		floatFields		= floatFieldList.toArray(new Field[floatFieldList.size()]);
		doubleFields	= doubleFieldList.toArray(new Field[doubleFieldList.size()]);
		
		final int serialiserCount = serializerList.size();
		objectFieldSerializer = new Serializer[serialiserCount];
		objectFields = new Field[serialiserCount][];
		for(int i = 0; i < serialiserCount; i++) {
			Serializer serializer = objectFieldSerializer[i] = serializerList.get(i);
			ArrayList<Field> fieldList = fieldListBySerializer.get(serializer);
			objectFields[i] = fieldList.toArray(new Field[fieldList.size()]);
		}
	}
	
	@Override
	public Object read(DataInput in) throws IOException {
		try {
			Object object = clazz.newInstance();
			read(object, in);
			return object;
		} catch (Exception e) {
			throw new IOException("Serializer[" + clazz + "]", e);
		}
	
	}
	
	@Override
	public void reliase(Object obj) {
		
	}
	
	@Override
	public Object read(Object obj, DataInput in) throws IOException {
		try {
			for(Field field: charFields) field.setChar(obj, in.readChar());
			for(Field field: byteFields) field.setByte(obj, in.readByte());
			for(Field field: shortFields) field.setShort(obj, in.readShort());
			for(Field field: intFields) field.setInt(obj, in.readInt());
			for(Field field: longFields) field.setLong(obj, in.readLong());
			for(Field field: floatFields) field.setFloat(obj, in.readFloat());
			for(Field field: doubleFields) field.setDouble(obj, in.readDouble());
			for(Field field: booleanFields) field.setBoolean(obj, in.readBoolean());
		} catch(Exception e) {
			throw new IOException(e);
		}
		
		for(int i = 0, count = objectFieldSerializer.length; i < count; i++) {
			Serializer serializer = objectFieldSerializer[i];
			for(Field field: objectFields[i]) {
				try {
					final Object data = field.get(obj);
					if(in.readBoolean()) {
						if(data == null) {
							field.set(obj, serializer.read(in));
						} else {
							final Object result = serializer.read(data, in);
							if(result != data) {
								field.set(obj, result);
							}
						}
					} else {
						if(data != null) {
							field.set(obj, null);
							serializer.reliase(data);
						}
					}
				} catch (IllegalArgumentException | IllegalAccessException e) {
					throw new IOException(e);
				}
			}
		}
		
		return obj;
	}
	
	@Override
	public void write(DataOutput out, Object obj) throws IOException {
		if(!clazz.equals(obj.getClass())) throw new IOException("Try to write " + obj.getClass() + " to serializer " + clazz);
		try {
			for(Field field: charFields) out.writeChar(field.getChar(obj));
			for(Field field: byteFields) out.writeByte(field.getByte(obj));
			for(Field field: shortFields) out.writeShort(field.getShort(obj));
			for(Field field: intFields) out.writeInt(field.getInt(obj));
			for(Field field: longFields) out.writeLong(field.getLong(obj));
			for(Field field: floatFields) out.writeFloat(field.getFloat(obj));
			for(Field field: doubleFields) out.writeDouble(field.getDouble(obj));
			for(Field field: booleanFields) out.writeBoolean(field.getBoolean(obj));
		} catch(Exception e) {
			throw new IOException("Serealizer[" + clazz + "]", e);
		}
		
		for(int i = 0, count = objectFieldSerializer.length; i < count; i++) {
			Serializer serializer = objectFieldSerializer[i];
			for(Field field: objectFields[i]) {
				try {
					Object data = field.get(obj);
					if(data == null) {
						out.writeBoolean(false);
					} else {
						out.writeBoolean(true);
						serializer.write(out, data);
					}
				} catch (IllegalArgumentException | IllegalAccessException e) {
					throw new IOException(e);
				}
			}
		}
	}
	
	@Override
	public void checkProtocol(DataInput in) throws IOException {
		checkProtocol(in, new HashSet<Object>());
	}
	
	@Override
	public void checkProtocol(DataInput in, HashSet<Object> checked) throws IOException {
		if(!clazz.getTypeName().equals(in.readUTF())) {
			throw new IOException();
		}
		
		if(!checked.add(clazz)) return;
		
		checkFieldArray(boolean.class, booleanFields, in);
		checkFieldArray(char.class, charFields, in);
		checkFieldArray(byte.class, byteFields, in);
		checkFieldArray(short.class, shortFields, in);
		checkFieldArray(int.class, intFields, in);
		checkFieldArray(long.class, longFields, in);
		checkFieldArray(float.class, floatFields, in);
		checkFieldArray(double.class, doubleFields, in);
		
		final int objectTypeCount = objectFieldSerializer.length;
		if(objectTypeCount != in.readInt()) throw new IOException();
		
		for(int i = 0; i < objectTypeCount; i++) {
			final Serializer serializer = objectFieldSerializer[i];
			if(serializer instanceof HaveInnerSerializer) {
				((HaveInnerSerializer)serializer).checkProtocol(in, checked);
			} else {
				serializer.checkProtocol(in);
			}
			final Field[] fields = objectFields[i];
			if(fields.length != in.readInt()) throw new IOException();
			for(Field field: fields) {
				if(!field.getName().equals(in.readUTF()));
			}
		}
		
		
	}
		
	private void checkFieldArray(Class<?> type, Field[] fields, DataInput in) throws IOException {
		if(!type.getTypeName().equals(in.readUTF()))
			throw new IOException();
		if(in.readInt() != fields.length)
			throw new IOException();
		for(Field filed: fields) {
			if(!filed.getName().equals(in.readUTF())) {
				throw new IOException();
			}
		}
	}
	
	@Override
	public void writeProtocol(DataOutput out) throws IOException {
		writeProtocol(out, new HashSet<Object>());
	}
	
	@Override
	public void writeProtocol(DataOutput out, HashSet<Object> checked) throws IOException {
		out.writeUTF(clazz.getTypeName());
		
		if(checked.contains(clazz)) return;
		checked.add(clazz);
		
		writeFieldArray(out, boolean.class, booleanFields);
		writeFieldArray(out, char.class, charFields);
		writeFieldArray(out, byte.class, byteFields);
		writeFieldArray(out, short.class, shortFields);
		writeFieldArray(out, int.class, intFields);
		writeFieldArray(out, long.class, longFields);
		writeFieldArray(out, float.class, floatFields);
		writeFieldArray(out, double.class, doubleFields);
		
		final int objectTypeCount = objectFieldSerializer.length;
		out.writeInt(objectTypeCount);
		for(int i = 0; i < objectTypeCount; i++) {
			final Serializer serializer = objectFieldSerializer[i];
			if(serializer instanceof HaveInnerSerializer) {
				((HaveInnerSerializer)serializer).writeProtocol(out, checked);
			} else {
				serializer.writeProtocol(out);
			}
			final Field[] fields = objectFields[i];
			out.writeInt(fields.length);
			for(Field field: fields) {
				out.writeUTF(field.getName());
			}
		}
	}
	
	private void writeFieldArray(DataOutput out, Class<?> clazz, Field[] fields) throws IOException {
		out.writeUTF(clazz.getTypeName());
		out.writeInt(fields.length);
		for(Field filed: fields) {
			out.writeUTF(filed.getName());
		}
	}
}
