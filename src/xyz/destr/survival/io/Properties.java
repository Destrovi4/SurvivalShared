package xyz.destr.survival.io;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

public class Properties implements Synchronizable {
	
	protected HashMap<String, Boolean> booleanByName = new HashMap<>();
	protected HashMap<String, Integer> integerByName = new HashMap<>();
	protected HashMap<String, Float> floatByName = new HashMap<>();
	protected HashMap<String, String> stringByName = new HashMap<>();
	
	@Override
	public void clear() {
		booleanByName.clear();
		integerByName.clear();
		floatByName.clear();
		stringByName.clear();
	}
	
	@Override
	public void read(DataInput in) throws IOException {
		for(int i = 0, count = in.readInt(); i < count; i++) {
			booleanByName.put(in.readUTF(), in.readBoolean());
		}
		
		for(int i = 0, count = in.readInt(); i < count; i++) {
			integerByName.put(in.readUTF(), in.readInt());
		}
		
		for(int i = 0, count = in.readInt(); i < count; i++) {
			floatByName.put(in.readUTF(), in.readFloat());
		}
		
		for(int i = 0, count = in.readInt(); i < count; i++) {
			stringByName.put(in.readUTF(), in.readUTF());
		}
	}
	
	@Override
	public void write(DataOutput out) throws IOException {
		out.writeInt(booleanByName.size());
		for(Entry<String, Boolean> entry: booleanByName.entrySet()) {
			out.writeUTF(entry.getKey());
			out.writeBoolean(entry.getValue());
		}
		
		out.writeInt(integerByName.size());
		for(Entry<String, Integer> entry: integerByName.entrySet()) {
			out.writeUTF(entry.getKey());
			out.writeInt(entry.getValue());
		}
		
		out.writeInt(floatByName.size());
		for(Entry<String, Float> entry: floatByName.entrySet()) {
			out.writeUTF(entry.getKey());
			out.writeFloat(entry.getValue());
		}
		
		out.writeInt(stringByName.size());
		for(Entry<String, String> entry: stringByName.entrySet()) {
			out.writeUTF(entry.getKey());
			out.writeUTF(entry.getValue());
		}
	}
	
	public boolean setBoolean(String key, boolean value) {
		return nullAsFalse(booleanByName.put(key, value));
	}
	
	public int setInteger(String key, int value) {
		return nullAsZero(integerByName.put(key, value));
	}
	
	public float setFloat(String key, float value) {
		return nullAsZero(floatByName.put(key, value));
	}
	
	public String setString(String key, String value) {
		return stringByName.put(key, value);
	}
	
	public boolean getBoolean(String key) {
		return nullAsFalse(booleanByName.get(key));
	}
	
	public int getInteger(String key) {
		return nullAsZero(integerByName.get(key));
	}
	
	public float getFloat(String key) {
		return nullAsZero(floatByName.get(key));
	}
	
	public String getString(String key) {
		return stringByName.get(key);
	}
	
	public void putAll(Properties other) {
		booleanByName.putAll(other.booleanByName);
		integerByName.putAll(other.integerByName);
		floatByName.putAll(other.floatByName);
		stringByName.putAll(other.stringByName);
	}
	
	protected static boolean nullAsFalse(Boolean value) {
		if(value == null) return false;
		return value;
	}
	
	protected static int nullAsZero(Integer value) {
		if(value == null) return 0;
		return value;
	}
	
	protected static float nullAsZero(Float value) {
		if(value == null) return 0;
		return value;
	}
}
