package br.mikhas.reflector.config;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import sun.misc.Unsafe;

/**
 * The {@link UnsafeFieldAccessor} uses hidden classes and methods which
 * provides direct access to field values.
 * 
 * @author Mikhail Domanoski
 * 
 */
@SuppressWarnings("restriction")
public class UnsafeFieldAccessor implements FieldAccessor {

	private final long offset;
	private final static Unsafe unsafe;
	private final Class<?> type;
	private Object staticBase;

	static {
		unsafe = UnsafeGetter.getRealUnsafe();
	}

	public UnsafeFieldAccessor(Field field) {
		if (!UnsafeGetter.isUnsafeAvailable())
			throw new IllegalStateException(
					"The unsafe object is not available");

		this.type = field.getType();

		if (Modifier.isStatic(field.getModifiers())) {
			this.staticBase = unsafe.staticFieldBase(field);
			this.offset = unsafe.staticFieldOffset(field);
		} else {
			this.offset = unsafe.objectFieldOffset(field);
		}
	}

	public Object get(Object target) {
		if (staticBase != null)
			target = staticBase;

		if (type == int.class)
			return unsafe.getInt(target, offset);
		else if (type == long.class)
			return unsafe.getLong(target, offset);
		else if (type == short.class)
			return unsafe.getShort(target, offset);
		else if (type == boolean.class)
			return unsafe.getBoolean(target, offset);
		else if (type == char.class)
			return unsafe.getChar(target, offset);
		else if (type == double.class)
			return unsafe.getDouble(target, offset);
		else if (type == byte.class)
			return unsafe.getByte(target, offset);
		else if (type == float.class)
			return unsafe.getFloat(target, offset);
		else
			return unsafe.getObject(target, offset);
	}

	@Override
	public void set(Object target, Object value) {
		if (staticBase != null)
			target = staticBase;

		if (type == int.class)
			unsafe.putInt(target, offset, (Integer) value);
		else if (type == short.class)
			unsafe.putShort(target, offset, (Short) value);
		else if (type == long.class)
			unsafe.putLong(target, offset, (Long) value);
		else if (type == byte.class)
			unsafe.putByte(target, offset, (Byte) value);
		else if (type == float.class)
			unsafe.putFloat(target, offset, (Float) value);
		else if (type == double.class)
			unsafe.putDouble(target, offset, (Double) value);
		else if (type == boolean.class)
			unsafe.putBoolean(target, offset, (Boolean) value);
		else if (type == char.class)
			unsafe.putChar(target, offset, (Character) value);
		else
			unsafe.putObject(target, offset, value);

	}
}
