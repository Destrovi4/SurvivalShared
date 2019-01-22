package xyz.destr.survival.io;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;

public class SynchronizableArrayList<T extends Synchronizable> extends ArrayList<T> implements Synchronizable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8513394544437786634L;
	
	protected Class<T> clazz;
	
	public SynchronizableArrayList(Class<T> clazz) {
		this.clazz = clazz;
	}
	
	public T addInstance() {
		try {
			T instance = clazz.newInstance();
			add(instance);
			return instance;
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void read(DataInput in) throws IOException {
		for(int i = 0, count = in.readInt(); i < count; i++) {
			addInstance().read(in);
		}
	}

	@Override
	public void write(DataOutput out) throws IOException {
		final int count = size();
		out.writeInt(count);
		for(int i = 0; i < count; i++) {
			get(i).write(out);
		}
	}

}
