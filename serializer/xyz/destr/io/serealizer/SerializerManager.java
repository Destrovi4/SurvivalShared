package xyz.destr.io.serealizer;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import xyz.destr.io.serealizer.Serializer.EnumSerializer;
import xyz.destr.io.serealizer.Serializer.ObjectArraySerializer;

public class SerializerManager {
			
	private static SerializerManager defaultManager = null;
	
	public static SerializerManager getInstance() {
		if(defaultManager == null) {
			defaultManager = new SerializerManager();
			defaultManager.addDefaultSerializers();
			defaultManager.addAdditionalSerializers();
		}
		return defaultManager;
	}

	final HashMap<Class<?>, Class<? extends Serializer>> serializerClassByClass = new HashMap<>();
	
	final HashMap<Class<?>, Serializer> serialiserByClass = new HashMap<>();
	final HashMap<SerializerOptions, Serializer> serialiserByOptions = new HashMap<>();
	
	public Serializer get(Class<?> clazz) {
		final Serializer serializer = serialiserByClass.get(clazz);
		if(serializer == null) {
			if(clazz.isEnum()) {
				final EnumSerializer newSerializer = new EnumSerializer(clazz);
				serialiserByClass.put(clazz, newSerializer);
				return newSerializer;
			}
			final Class<? extends Serializer> serializerClass = serializerClassByClass.get(clazz);
			if(serializerClass == null) {
				if(clazz.isArray()) {
					final ObjectArraySerializer newSerializer = new ObjectArraySerializer(this, clazz);
					serialiserByClass.put(clazz, newSerializer);
					return newSerializer;
				} else {
					final ObjectSerializer newSerializer = new ObjectSerializer();
					serialiserByClass.put(clazz, newSerializer);
					newSerializer.init(clazz, this);
					return newSerializer;
				}
			} else {
				try {
					final Serializer newSerializer = serializerClass.newInstance();
					serialiserByClass.put(clazz, newSerializer);
					return newSerializer;
				} catch (InstantiationException | IllegalAccessException e) {
					serialiserByClass.remove(clazz);
					throw new AssertionError(e);
				}					
			}
		}
		return serializer;
	}
	
	public Serializer get(SerializerOptions options) {
		final Serializer serializer = serialiserByOptions.get(options);
		if(serializer == null) {
			switch(options.classes.length) {
			case 0: throw new RuntimeException();
			case 1:
				try {
					final Class<?> clazz = options.classes[0];
					final Class<? extends Serializer> serializerClass = serializerClassByClass.get(clazz);
					final Serializer newSerializer;
					if(serializerClass == null) {
						if(clazz.isArray()) {
							final ObjectArraySerializer arraySerializer = new ObjectArraySerializer(this, clazz);
							newSerializer = arraySerializer;
							serialiserByOptions.put(options, newSerializer);
						} else {
							final ObjectSerializer objectSerializer = new ObjectSerializer();
							newSerializer = objectSerializer;
							serialiserByOptions.put(options, newSerializer);
							objectSerializer.init(clazz, this);
						}
					} else {
						final Constructor<?> constructor;
						serchForConstructor:{
							for(Constructor<?> c: serializerClass.getConstructors()) {
								final Class<?>[] parameterTypes = c.getParameterTypes();
								if(parameterTypes.length == 1 && parameterTypes[0] == SerializerOptions.class) {
									constructor = c;
									break serchForConstructor;
								}
							}
							constructor = null;
						}
												
						if(constructor != null) {
							newSerializer = (Serializer)constructor.newInstance(options);
						} else {
							newSerializer = serializerClass.newInstance();
						}
						serialiserByOptions.put(options, newSerializer);
					}
					if(newSerializer instanceof InitializableSerializer) {
						((InitializableSerializer)newSerializer).init(options, this);
					}
					return newSerializer;
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | SecurityException e) {
					throw new AssertionError(e);
				}
				default:{
					final SwitchSerializer newSerializer = new SwitchSerializer();
					serialiserByOptions.put(options, newSerializer);
					newSerializer.init(options, this);
					return newSerializer;
				}
					
			}
		} else {
			return serializer;
		}
		
		/*
		if(serializer == null) {
			final Class<?> clazz = options.getSerializedClass();
			final Class<? extends Serializer> serializerClass = serializerClassByClass.get(clazz);
			try {
				Constructor<? extends Serializer> constructor = serializerClass.getDeclaredConstructor(SerializerOptions.class);
				final Serializer newSerializer = constructor.newInstance(options);
				serialiserByOptions.put(options, newSerializer);
				if(newSerializer instanceof InitializableSerializer) {
					((InitializableSerializer)newSerializer).init(options, this);
				}
				return newSerializer;
			} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				serialiserByOptions.remove(options);
				e.printStackTrace();
			}
		}*/
		//return serializer;
	}
	
	public void addDefaultSerializers() {
		Serializer.registerDeafultSerializers(this);
	}
	
	public void addAdditionalSerializers() {
		Serializer.registerAdditionalSerializers(this);
	}
	
	public void registerSerializerClass(Class<?> serializedClass, Class<? extends Serializer> serialiserClass) {
		serializerClassByClass.put(serializedClass, serialiserClass);
	}
	
}
