package xyz.destr.io.serealizer;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class SwitchSerializer implements Serializer, InitializableSerializer, HaveInnerSerializer {
	protected Serializer[] serialiser;
	protected SerializerOptions options;
	
	@Override
	public void init(SerializerOptions options, SerializerManager manager) {
		this.options = options;
		final ArrayList<Serializer> serializerList = new ArrayList<>();
		for(Class<?> clazz: options.classes) {
			final Serializer serializer = manager.get(clazz);
			if(serializer == null) throw new RuntimeException("Can't get serializer for class " + clazz.getTypeName());
			serializerList.add(serializer);
		}
		serialiser = serializerList.toArray(new Serializer[serializerList.size()]);
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
		if("Switch".equals(in.readUTF())) throw new IOException();
		if(!checked.add(options)) return;
		if(serialiser.length != in.readInt()) throw new IOException();
		for(Serializer s: serialiser) {
			if(s instanceof HaveInnerSerializer) {
				((HaveInnerSerializer)s).checkProtocol(in, checked);
			} else {
				s.checkProtocol(in);
			}
		}
	}

	@Override
	public void writeProtocol(DataOutput out, HashSet<Object> checked) throws IOException {
		out.writeUTF("Switch");
		if(!checked.add(options)) return;
		out.writeInt(serialiser.length);
		for(Serializer s: serialiser) {
			if(s instanceof HaveInnerSerializer) {
				((HaveInnerSerializer)s).writeProtocol(out, checked);
			} else {
				s.writeProtocol(out);
			}
		}
	}

	@Override
	public Object read(DataInput in) throws IOException {
		final int index = in.readInt();
		if(index < 0 || index > serialiser.length) throw new IOException();
		return serialiser[index].read(in);
	}

	@Override
	public void reliase(Object obj) {
	}

	@Override
	public Object read(Object obj, DataInput in) throws IOException {
		final int index = in.readInt();
		if(index < 0 || index > serialiser.length) throw new IOException();
		return serialiser[index].read(obj, in);
	}

	@Override
	public void write(DataOutput out, Object obj) throws IOException {
		final Class<?>[] classes = options.classes;
		for(int i = 0; i < classes.length; i++) {
			if(classes[i] == obj.getClass()) {
				out.writeInt(i);
				serialiser[i].write(out, obj);
				return;
			}
		}
	}	
}
