package xyz.destr.io.serealizer;

public interface InitializableSerializer {
	public void init(SerializerOptions options, SerializerManager manager);
}
