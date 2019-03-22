package xyz.destr.io.serealizer;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class ArrayLsitSerializeer implements Serializer, InitializableSerializer, HaveInnerSerializer {

	protected SerializerOptions options;
	protected Serializer serializer;
		
	@Override
	public void init(SerializerOptions options, SerializerManager manager) {
		this.options = options;
		this.serializer = manager.get(new SerializerOptions(options.innerClasses, new Class<?>[0]));
	}

	@Override
	public Object read(DataInput in) throws IOException {
		return read(new ArrayList<Object>(), in);
	}

	@Override
	public void reliase(Object obj) {
	}

	@Override
	public Object read(Object obj, DataInput in) throws IOException {
		@SuppressWarnings("unchecked")
		final ArrayList<Object> arrayList = (ArrayList<Object>)obj;
		for(int i = 0, size = arrayList.size(); i < size; i++) {
			final Object data = arrayList.get(i);
			if(data == null) {
				serializer.reliase(data);
			}
		}
		arrayList.clear();
		final int remouteSize = in.readInt();
		for(int i = 0; i < remouteSize; i++) {
			if(in.readBoolean()) {
				arrayList.add(serializer.read(in));
			} else {
				arrayList.add(null);
			}
		}	
		return arrayList;
	}

	@Override
	public void write(DataOutput out, Object obj) throws IOException {
		final ArrayList<?> arrayList = (ArrayList<?>)obj;
		out.writeInt(arrayList.size());
		for(int i = 0, size = arrayList.size(); i < size; i++) {
			final Object data = arrayList.get(i);
			if(data == null) {
				out.writeBoolean(false);
			} else {
				out.writeBoolean(true);
				serializer.write(out, data);
			}
		}
	}

	@Override
	public void checkProtocol(DataInput in) throws IOException {
		if(serializer instanceof InitializableSerializer) {
			checkProtocol(in, new HashSet<Object>());
		} else {
			if(!ArrayList.class.getTypeName().equals(in.readUTF())) throw new IOException();
		}
	}

	@Override
	public void writeProtocol(DataOutput out) throws IOException {
		if(serializer instanceof InitializableSerializer) {
			writeProtocol(out, new HashSet<Object>());
		} else {
			out.writeUTF(ArrayList.class.getTypeName());
		}
	}

	@Override
	public void checkProtocol(DataInput in, HashSet<Object> checked) throws IOException {
		if(!ArrayList.class.getTypeName().equals(in.readUTF())) throw new IOException();
		if(serializer instanceof InitializableSerializer) {
			checkProtocol(in, checked);
		} else {
			if(!ArrayList.class.getTypeName().equals(in.readUTF())) throw new IOException();
		}
	}

	@Override
	public void writeProtocol(DataOutput out, HashSet<Object> checked) throws IOException {
		out.writeUTF(ArrayList.class.getTypeName());
		if(!checked.add(options)) return;
		if(serializer instanceof InitializableSerializer) {
			writeProtocol(out, checked);
		} else {
			out.writeUTF(ArrayList.class.getTypeName());
		}
	}

}
