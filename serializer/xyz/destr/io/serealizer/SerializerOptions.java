package xyz.destr.io.serealizer;

import java.util.Arrays;
import java.util.Comparator;

public class SerializerOptions {
	static Comparator<Object> HASH_COMPARATOR = (a, b)->{
		final int ha = a.hashCode();
		final int hb = b.hashCode();
		if(ha == hb) return 0;
		return ha > hb ? 1 : -1;
	};
	
	protected Class<?>[] classes;
	protected Class<?>[] innerClasses;
	
	protected final int hash;
	
	public SerializerOptions(
		Class<?>[] classes,
		Class<?>[] innerClasses
	) {
		int hash;
		this.classes = Arrays.copyOf(classes, classes.length);
		Arrays.sort(this.classes, HASH_COMPARATOR);
		hash = Arrays.hashCode(this.classes);
		this.innerClasses = Arrays.copyOf(innerClasses, innerClasses.length);
		Arrays.sort(this.innerClasses, HASH_COMPARATOR);
		hash = hash * 31 + Arrays.hashCode(this.innerClasses);
		this.hash = hash;
	}
	
	@Override
	public int hashCode() {
		return hash;
	}
	
	@Override
	public boolean equals(Object other) {
		if(this == other) return true;
		if(other instanceof SerializerOptions) {
			SerializerOptions otherOptions = (SerializerOptions)other;
			if(classes.length != otherOptions.classes.length) return false;
			if(innerClasses.length != otherOptions.innerClasses.length) return false;
			if(!Arrays.equals(classes, otherOptions.classes)) return false;
			if(!Arrays.equals(innerClasses, otherOptions.innerClasses)) return false;
			
			return true;
		}		
		return false;
	}
	
	@Override
	public String toString() {
		return "[" + getClass().getName() + "0x" + Integer.toHexString(hash)
		+ "; types: " + Arrays.toString(classes)
		+ "; innerClasses: " + Arrays.toString(innerClasses);
	}
}
