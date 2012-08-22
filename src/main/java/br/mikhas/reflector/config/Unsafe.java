package br.mikhas.reflector.config;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * Provide access to low-level memory operations.
 * 
 * @author Mikhail Domanoski
 * 
 */
@SuppressWarnings("restriction")
public class Unsafe {

	/**
	 * Single instance of it
	 */
	private static Unsafe instance;

	/**
	 * The unsafe object
	 */
	private final sun.misc.Unsafe unsafe;

	// Prevent external instantiation
	private Unsafe() {
		try {
			Field usafeField = getTheUnsafeField();

			usafeField.setAccessible(true);
			unsafe = (sun.misc.Unsafe) usafeField.get(null);
		} catch (Exception e) {
			throw new Error("Could not get sun.misc.Unsafe object.", e);
		}
	}

	/**
	 * Use different ways to get the unsafe object
	 * 
	 * @return The field which holds the unsafe object
	 */
	private Field getTheUnsafeField() {
		String[] possibleNames = { "theUnsafe", "UNSAFE", "unsafe", "instance" };

		Class<sun.misc.Unsafe> unsafeClass = sun.misc.Unsafe.class;

		for (String name : possibleNames) {
			try {
				return unsafeClass.getDeclaredField(name);
			} catch (Exception e) {

			}
		}

		Field[] fields = unsafeClass.getDeclaredFields();

		for (Field field : fields) {
			try {
				if (field.getType() == unsafeClass)
					return field;
			} catch (Exception e) {
			}
		}

		return null;
	}

	/**
	 * Gets the single instance of the Unsafe wrapper
	 * 
	 * @return The {@link UnsafeWrapper}
	 */
	static Unsafe getInstance() {
		return (instance != null ? instance : (instance = new Unsafe()));
	}

	/**
	 * Gets the real unsafe object instance.
	 * <p>
	 * Beware on using it!
	 * 
	 * @return
	 */
	static sun.misc.Unsafe realUnsafe() {
		Unsafe unsafe = getInstance();
		return unsafe.unsafe;
	}

	/**
	 * <p>
	 * Retrieves the offset value of the {@link Field} for use by other methods
	 * in this class.
	 * </p>
	 * 
	 * @param field
	 *            The {@link Field} to retrieve the offset for.
	 * @return The offset value.
	 */
	public long objectFieldOffset(Field field) {
		return unsafe.objectFieldOffset(field);
	}

	/**
	 * Sets the value of the int field at the specified offset in the supplied
	 * object to the given value.
	 * 
	 * @param target
	 *            the object containing the field to modify.
	 * @param fieldOffset
	 *            the offset of the int field within <code>obj</code>.
	 * @param intValue
	 *            the new value of the field.
	 */
	public void putInt(Object target, long fieldOffset, int intValue) {
		unsafe.putInt(target, fieldOffset, intValue);
	}

	/**
	 * Sets the value of the short field at the specified offset in the supplied
	 * object to the given value.
	 * 
	 * @param target
	 *            the object containing the field to modify.
	 * @param fieldOffset
	 *            the offset of the short field within <code>obj</code>.
	 * @param shortValue
	 *            the new value of the field.
	 */
	public void putShort(Object target, long fieldOffset, short shortValue) {
		unsafe.putShort(target, fieldOffset, shortValue);
	}

	/**
	 * Sets the value of the long field at the specified offset in the supplied
	 * object to the given value.
	 * 
	 * @param target
	 *            the object containing the field to modify.
	 * @param fieldOffset
	 *            the offset of the long field within <code>obj</code>.
	 * @param longValue
	 *            the new value of the field.
	 */
	public void putLong(Object target, long fieldOffset, long longValue) {
		unsafe.putLong(target, fieldOffset, longValue);
	}

	/**
	 * Sets the value of the byte field at the specified offset in the supplied
	 * object to the given value.
	 * 
	 * @param target
	 *            the object containing the field to modify.
	 * @param fieldOffset
	 *            the offset of the byte field within <code>obj</code>.
	 * @param byteValue
	 *            the new value of the field.
	 */
	public void putByte(Object target, long fieldOffset, byte byteValue) {
		unsafe.putByte(target, fieldOffset, byteValue);
	}

	/**
	 * Sets the value of the float field at the specified offset in the supplied
	 * object to the given value.
	 * 
	 * @param target
	 *            the object containing the field to modify.
	 * @param fieldOffset
	 *            the offset of the float field within <code>obj</code>.
	 * @param floatValue
	 *            the new value of the field.
	 */
	public void putFloat(Object target, long fieldOffset, float floatValue) {
		unsafe.putFloat(target, fieldOffset, floatValue);
	}

	/**
	 * Sets the value of the double field at the specified offset in the
	 * supplied object to the given value.
	 * 
	 * @param target
	 *            the object containing the field to modify.
	 * @param fieldOffset
	 *            the offset of the double field within <code>obj</code>.
	 * @param doubleValue
	 *            the new value of the field.
	 */
	public void putDouble(Object target, long fieldOffset, double doubleValue) {
		unsafe.putDouble(target, fieldOffset, doubleValue);
	}

	/**
	 * Sets the value of the boolean field at the specified offset in the
	 * supplied object to the given value.
	 * 
	 * @param target
	 *            the object containing the field to modify.
	 * @param fieldOffset
	 *            the offset of the boolean field within <code>obj</code>.
	 * @param booleanValue
	 *            the new value of the field.
	 */
	public void putBoolean(Object target, long fieldOffset, boolean booleanValue) {
		unsafe.putBoolean(target, fieldOffset, booleanValue);
	}

	/**
	 * Sets the value of the char field at the specified offset in the supplied
	 * object to the given value.
	 * 
	 * @param target
	 *            the object containing the field to modify.
	 * @param fieldOffset
	 *            the offset of the char field within <code>obj</code>.
	 * @param charValue
	 *            the new value of the field.
	 */
	public void putChar(Object target, long fieldOffset, char charValue) {
		unsafe.putChar(target, fieldOffset, charValue);
	}

	/**
	 * Sets the value of the object field at the specified offset in the
	 * supplied object to the given value.
	 * 
	 * @param target
	 *            the object containing the field to modify.
	 * @param fieldOffset
	 *            the offset of the object field within <code>obj</code>.
	 * @param object
	 *            the new value of the field.
	 */
	public void putObject(Object target, long fieldOffset, Object object) {
		unsafe.putObject(target, fieldOffset, object);
	}

	/**
	 * Retrieves the value of the boolean field at the specified offset in the
	 * supplied object.
	 * 
	 * @param target
	 *            the object containing the field to read.
	 * @param fieldOffset
	 *            the offset of the boolean field within <code>obj</code>.
	 */
	public boolean getBoolean(Object target, long fieldOffset) {
		return unsafe.getBoolean(target, fieldOffset);
	}

	/**
	 * Retrieves the value of the int field at the specified offset in the
	 * supplied object.
	 * 
	 * @param target
	 *            the object containing the field to read.
	 * @param fieldOffset
	 *            the offset of the int field within <code>obj</code>.
	 */
	public int getInt(Object target, long fieldOffset) {
		return unsafe.getInt(target, fieldOffset);
	}

	/**
	 * Retrieves the value of the short field at the specified offset in the
	 * supplied object.
	 * 
	 * @param target
	 *            the object containing the field to read.
	 * @param fieldOffset
	 *            the offset of the short field within <code>obj</code>.
	 */
	public short getShort(Object target, long fieldOffset) {
		return unsafe.getShort(target, fieldOffset);
	}

	/**
	 * Retrieves the value of the long field at the specified offset in the
	 * supplied object.
	 * 
	 * @param target
	 *            the object containing the field to read.
	 * @param fieldOffset
	 *            the offset of the long field within <code>obj</code>.
	 */
	public long getLong(Object target, long fieldOffset) {
		return unsafe.getLong(target, fieldOffset);
	}

	/**
	 * Retrieves the value of the char field at the specified offset in the
	 * supplied object.
	 * 
	 * @param target
	 *            the object containing the field to read.
	 * @param fieldOffset
	 *            the offset of the char field within <code>obj</code>.
	 */
	public char getChar(Object target, long fieldOffset) {
		return unsafe.getChar(target, fieldOffset);
	}

	/**
	 * Retrieves the value of the byte field at the specified offset in the
	 * supplied object.
	 * 
	 * @param target
	 *            the object containing the field to read.
	 * @param fieldOffset
	 *            the offset of the byte field within <code>obj</code>.
	 */
	public byte getByte(Object target, long fieldOffset) {
		return unsafe.getByte(target, fieldOffset);
	}

	/**
	 * Retrieves the value of the float field at the specified offset in the
	 * supplied object.
	 * 
	 * @param target
	 *            the object containing the field to read.
	 * @param fieldOffset
	 *            the offset of the float field within <code>obj</code>.
	 */
	public float getFloat(Object target, long fieldOffset) {
		return unsafe.getFloat(target, fieldOffset);
	}

	/**
	 * Retrieves the value of the double field at the specified offset in the
	 * supplied object.
	 * 
	 * @param target
	 *            the object containing the field to read.
	 * @param fieldOffset
	 *            the offset of the double field within <code>obj</code>.
	 */
	public double getDouble(Object target, long fieldOffset) {
		return unsafe.getDouble(target, fieldOffset);
	}

	/**
	 * Retrieves the value of the Object reference field at the specified offset
	 * in the supplied object.
	 * 
	 * @param target
	 *            the object containing the field to read.
	 * @param fieldOffset
	 *            the offset of the Object reference field within
	 *            <code>obj</code>.
	 */
	public Object getObject(Object target, long fieldOffset) {
		return unsafe.getObject(target, fieldOffset);
	}

	/**
	 * Retrieves the base object used as reference to this field offset
	 * 
	 * @param field
	 *            The field to get the base
	 * @return An object reference which is used as base to access the field
	 */
	public Object staticFieldBase(Field field) {
		return unsafe.staticFieldBase(field);
	}

	/**
	 * Retrieves the offset of the static field
	 * 
	 * @param field
	 *            The static field to get the offset
	 * @return The offset of the field based on the object reference
	 */
	public long staticFieldOffset(Field field) {
		if (!Modifier.isStatic(field.getModifiers()))
			throw new IllegalArgumentException(
					"The providen field is not static: " + field);

		return unsafe.staticFieldOffset(field);
	}

	/**
	 * Gets the value of the field defined by the given offset according to the
	 * providen value type.
	 * 
	 * @param target
	 *            The target object which contains the field
	 * @param type
	 *            The type of the field value
	 * @param fieldOffset
	 *            The field offset according to the object reference
	 * @return The value of the field
	 */
	public Object getValue(Object target, Class<?> type, long fieldOffset) {

		if (type == int.class)
			return unsafe.getInt(target, fieldOffset);
		else if (type == long.class)
			return unsafe.getLong(target, fieldOffset);
		else if (type == short.class)
			return unsafe.getShort(target, fieldOffset);
		else if (type == boolean.class)
			return unsafe.getBoolean(target, fieldOffset);
		else if (type == char.class)
			return unsafe.getChar(target, fieldOffset);
		else if (type == double.class)
			return unsafe.getDouble(target, fieldOffset);
		else if (type == byte.class)
			return unsafe.getByte(target, fieldOffset);
		else if (type == float.class)
			return unsafe.getFloat(target, fieldOffset);
		else
			return unsafe.getObject(target, fieldOffset);

	}

	/**
	 * Sets the value of the field defined by the given offset according to the
	 * providen value type.
	 * 
	 * @param target
	 *            The target object which contains the field
	 * @param type
	 *            The type of the field value
	 * @param fieldOffset
	 *            The field offset acording to the object reference
	 * @param value
	 *            The new value to the field
	 */
	public void setValue(Object target, Class<?> type, long fieldOffset,
			Object value) {
		if (type == int.class)
			unsafe.putInt(target, fieldOffset, (Integer) value);
		else if (type == short.class)
			unsafe.putShort(target, fieldOffset, (Short) value);
		else if (type == long.class)
			unsafe.putLong(target, fieldOffset, (Long) value);
		else if (type == byte.class)
			unsafe.putByte(target, fieldOffset, (Byte) value);
		else if (type == float.class)
			unsafe.putFloat(target, fieldOffset, (Float) value);
		else if (type == double.class)
			unsafe.putDouble(target, fieldOffset, (Double) value);
		else if (type == boolean.class)
			unsafe.putBoolean(target, fieldOffset, (Boolean) value);
		else if (type == char.class)
			unsafe.putChar(target, fieldOffset, (Character) value);
		else
			unsafe.putObject(target, fieldOffset, value);
	}
}
