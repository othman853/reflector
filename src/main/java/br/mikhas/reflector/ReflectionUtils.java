package br.mikhas.reflector;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Some utility methods to refractor
 * 
 * @author Mikhail Domanoski
 * 
 */
public class ReflectionUtils {

	/** Holds all default number classes */
	private static final Set<Class<? extends Number>> NUMBERS;

	/**
	 * Holds a mapping between primitives and wrapper classes
	 */
	private static final Map<Class<?>, Class<?>> PRIMITIVE_TO_WRAPPER;
	/**
	 * Holds a mapping between wrapper and primitive classes
	 */
	private static final Map<Class<?>, Class<?>> WRAPPER_TO_PRIMITIVE;
	/**
	 * Holds wrapper classes
	 */
	private static final Set<Class<?>> WRAPPERS;
	/**
	 * Holds primitive classes
	 */
	private static final Set<Class<?>> PRIMITIVES;

	// Initialize sets and maps
	static {
		WRAPPERS = new HashSet<Class<?>>(9);
		WRAPPERS.add(Character.class);
		WRAPPERS.add(Boolean.class);
		WRAPPERS.add(Long.class);
		WRAPPERS.add(Integer.class);
		WRAPPERS.add(Short.class);
		WRAPPERS.add(Float.class);
		WRAPPERS.add(Byte.class);
		WRAPPERS.add(Double.class);
		WRAPPERS.add(Void.class);

		PRIMITIVES = new HashSet<Class<?>>(9);
		PRIMITIVES.add(char.class);
		PRIMITIVES.add(boolean.class);
		PRIMITIVES.add(long.class);
		PRIMITIVES.add(int.class);
		PRIMITIVES.add(short.class);
		PRIMITIVES.add(float.class);
		PRIMITIVES.add(byte.class);
		PRIMITIVES.add(double.class);
		PRIMITIVES.add(void.class);

		PRIMITIVE_TO_WRAPPER = new HashMap<Class<?>, Class<?>>(9);
		PRIMITIVE_TO_WRAPPER.put(char.class, Character.class);
		PRIMITIVE_TO_WRAPPER.put(boolean.class, Boolean.class);
		PRIMITIVE_TO_WRAPPER.put(long.class, Long.class);
		PRIMITIVE_TO_WRAPPER.put(int.class, Integer.class);
		PRIMITIVE_TO_WRAPPER.put(short.class, Short.class);
		PRIMITIVE_TO_WRAPPER.put(float.class, Float.class);
		PRIMITIVE_TO_WRAPPER.put(byte.class, Byte.class);
		PRIMITIVE_TO_WRAPPER.put(double.class, Double.class);
		PRIMITIVE_TO_WRAPPER.put(void.class, Void.class);

		WRAPPER_TO_PRIMITIVE = new HashMap<Class<?>, Class<?>>(9);
		WRAPPER_TO_PRIMITIVE.put(Character.class, char.class);
		WRAPPER_TO_PRIMITIVE.put(Boolean.class, boolean.class);
		WRAPPER_TO_PRIMITIVE.put(Long.class, long.class);
		WRAPPER_TO_PRIMITIVE.put(Integer.class, int.class);
		WRAPPER_TO_PRIMITIVE.put(Short.class, short.class);
		WRAPPER_TO_PRIMITIVE.put(Double.class, double.class);
		WRAPPER_TO_PRIMITIVE.put(Float.class, float.class);
		WRAPPER_TO_PRIMITIVE.put(Byte.class, byte.class);
		WRAPPER_TO_PRIMITIVE.put(Void.class, void.class);

		NUMBERS = new HashSet<Class<? extends Number>>(12);
		NUMBERS.add(short.class);
		NUMBERS.add(byte.class);
		NUMBERS.add(int.class);
		NUMBERS.add(long.class);
		NUMBERS.add(float.class);
		NUMBERS.add(double.class);
		NUMBERS.add(Short.class);
		NUMBERS.add(Integer.class);
		NUMBERS.add(Long.class);
		NUMBERS.add(Float.class);
		NUMBERS.add(Double.class);
		NUMBERS.add(Byte.class);
	}

	/**
	 * Gets the types from a list of objects
	 * 
	 * @param objs
	 *            The object list
	 * @return The class of each object on the array
	 */
	public static Class<?>[] getArgumentsTypes(Object... objs) {
		Class<?>[] types = new Class<?>[objs.length];

		for (int i = 0; i < objs.length; i++) {
			if (objs[i] == null)
				types[i] = null;
			else
				types[i] = objs[i].getClass();
		}

		return types;
	}

	/**
	 * Gets the names of the classes on the array
	 * 
	 * @param cls
	 *            an array of classes
	 * @return a String with the classes names
	 */
	public static String typeNames(Class<?>... cls) {
		if (cls == null || cls.length < 1)
			return "";

		StringBuilder builder = new StringBuilder(cls.length * 25);

		for (Class<?> c : cls) {
			builder.append(c.getName()).append(',');
		}
		builder.deleteCharAt(builder.length() - 1);

		return builder.toString();
	}

	/**
	 * Checks if an array of classes is assignable to another array of classes
	 * 
	 * @param source
	 * @param destiny
	 * @return
	 */
	static boolean assignable(Class<?>[] source, Class<?>[] destiny) {
		if (source.length != destiny.length)
			return false;

		for (int i = 0; i < source.length; i++) {

			if (destiny[i] == null) {
				if (source[i].isPrimitive())
					return false;
			} else if (!source[i].isAssignableFrom(destiny[i])) {
				return false;
			}

		}
		return true;
	}

	/**
	 * Looks for a method that matches with a collection of arguments
	 * 
	 * @param cls
	 *            The class to look up for the method
	 * @param types
	 *            The collection of argument types that the method must match
	 * @return An method that matches with the argument collection
	 */
	public static Method lookupMethod(Class<?> cls, Class<?>[] types) {
		return lookupMethod(cls, null, types);
	}

	/**
	 * Looks for a method that matches with a collection of arguments
	 * 
	 * @param cls
	 *            The class to look up for the method
	 * @param name
	 *            The possible name of the method
	 * @param types
	 *            The collection of argument types that the method must match
	 * @return An method that matches with the argument collection
	 */
	public static Method lookupMethod(Class<?> cls, String name,
			Class<?>[] types) {
		Method m;

		try {
			return cls.getDeclaredMethod(name, types);
		} catch (Throwable t) {

		}

		Method[] methods = cls.getDeclaredMethods();

		for (int i = 0; i < methods.length; i++) {
			m = methods[i];

			if (name != null && !m.getName().equals(name)) {
				continue;
			}

			Class<?>[] params = m.getParameterTypes();

			if (params.length != types.length)
				continue;

			if (assignable(params, types)) {
				return m;
			}
		}

		// Try swapping primitives to wrappers

		Class<?>[] wrappers = new Class<?>[types.length];
		for (int j = 0; j < types.length; j++)
			wrappers[j] = getWrapper(types[j]);

		for (int i = 0; i < methods.length; i++) {
			m = methods[i];

			if (name != null && !m.getName().equals(name))
				continue;

			Class<?>[] params = m.getParameterTypes();

			if (params.length != wrappers.length)
				continue;

			for (int k = 0; k < params.length; k++)
				params[k] = getWrapper(params[k]);

			if (assignable(params, wrappers)) {
				return m;
			}
		}

		return null;
	}

	/**
	 * Looks for an constructor which accepts the argument list
	 * 
	 * @param <T>
	 *            The type of object which the constructor returns
	 * @param cls
	 *            The class to look for the constructor
	 * @param types
	 *            The type of the arguments to be provided to the constructor
	 * @return The constructor
	 */
	@SuppressWarnings("unchecked")
	public static <T> Constructor<T> lookupConstructor(Class<T> cls,
			Class<?>[] types) {
		Constructor<?> con;

		try {
			return cls.getDeclaredConstructor(types);
		} catch (Throwable t) {

		}

		Constructor<?>[] cons = cls.getDeclaredConstructors();

		for (int i = 0; i < cons.length; i++) {
			con = cons[i];

			Class<?>[] params = con.getParameterTypes();

			if (params.length != types.length)
				continue;

			if (assignable(params, types)) {
				return (Constructor<T>) con;
			}
		}

		return null;
	}

	/**
	 * Converts a String to a desired java type
	 * 
	 * @param type
	 *            The type to get
	 * @param value
	 *            The string value
	 * @return The string value converted into the required type if possible
	 */
	@SuppressWarnings("unchecked")
	public static <T> T convertToType(Class<T> type, String value) {
		if (type == String.class) {
			return (T) String.valueOf(value);
		} else if (isNumber(type))
			return (T) getNumber((Class<? extends Number>) type, value);
		else if (type == Boolean.class || type == boolean.class) {
			return (T) Boolean.valueOf(value);
		} else if ((type == char.class || Character.class == type)
				&& (value != null && !value.isEmpty())) {
			return (T) Character.valueOf(value.charAt(0));
		} else {
			return null;
		}
	}

	/**
	 * Gets the primitive java type according to the given wrapper.
	 * 
	 * @param cls
	 *            The java wrapper class.
	 *            <p>
	 *            If it is not a wrapper class, the method will return the class
	 *            it self.
	 * @return The primitive class
	 */
	public static Class<?> getPrimitive(Class<?> cls) {
		if (WRAPPERS.contains(cls)) {
			return WRAPPER_TO_PRIMITIVE.get(cls);
		} else {
			return cls;
		}

	}

	/**
	 * Gets the wrapper java type according to the given primitive.
	 * 
	 * @param cls
	 *            The java primitive class.
	 *            <p>
	 *            If it is not a primitive class, the method will return the
	 *            class it self.
	 * @return The wrapper class
	 */
	public static Class<?> getWrapper(Class<?> cls) {
		if (PRIMITIVES.contains(cls)) {
			return PRIMITIVE_TO_WRAPPER.get(cls);
		} else {
			return cls;
		}

	}

	/**
	 * Checks if the given class is a java primitive wrapper
	 * 
	 * @param clazz
	 *            the class to check
	 * @return <code>true</code> if it is a java wrapper class
	 */
	public static boolean isWrapper(Class<?> clazz) {
		return WRAPPERS.contains(clazz);
	}

	/**
	 * Checks if the given class is a java primitive class
	 * 
	 * @param clazz
	 *            the class to check
	 * @return <code>true</code> if it is a java primitive class
	 */
	public static boolean isPrimitive(Class<?> clazz) {
		return PRIMITIVES.contains(clazz);
	}

	/**
	 * Checks if the class is a class number
	 * 
	 * @param cls
	 *            The possible number class
	 * @return <code>true</code> if it's a number
	 */
	public static boolean isNumber(Class<?> cls) {
		return cls == null ? false : NUMBERS.contains(cls);
	}

	/**
	 * Checks if the object is a number
	 * 
	 * @param obj
	 *            The possible number object
	 * @return <code>true</code> if it's a number
	 */
	public static boolean isNumber(Object obj) {
		return obj == null ? false : NUMBERS.contains(obj.getClass());
	}

	/**
	 * Creates a numebr from a String according to the desired number class
	 * 
	 * @param clazz
	 *            The number class
	 * @param number
	 *            The string representation of the number
	 * @return and instance of the desired number
	 */
	@SuppressWarnings("unchecked")
	public static Number getNumber(Class<?> clazz, String number) {

		if (!isNumber(clazz))
			throw new IllegalArgumentException(
					"The providen class is not a number: " + clazz);

		Class<? extends Number> cls = null;
		if (isWrapper(clazz)) {
			cls = (Class<? extends Number>) clazz;
		} else if (isPrimitive(clazz)) {
			cls = (Class<? extends Number>) getWrapper(clazz);
		}
		return instantiateNumber(cls, number);
	}

	/**
	 * Creates a new instance of the number
	 * 
	 * @param cls
	 *            The number class
	 * @param number
	 *            The string representation of the desired number
	 * @return The instance of the number
	 */
	@SuppressWarnings("unchecked")
	private static Number instantiateNumber(Class<? extends Number> cls,
			String number) {
		assert cls != null : "The class may not be null";

		if (number == null || number.isEmpty()) {
			return null;
		}

		try {
			Constructor<Number> numberConstructor = (Constructor<Number>) cls
					.getConstructor(String.class);
			Number resnum = numberConstructor.newInstance(number);
			return resnum;
		} catch (SecurityException e) {
			throw new RuntimeException(e);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Creates a string representation of the method signature
	 * 
	 * @param method
	 *            The method to create the signature
	 * @return A string representation of the method
	 */
	public static String getSignature(Method method) {
		StringBuilder builder = new StringBuilder(128);

		int modifiers = method.getModifiers();

		if (Modifier.isPublic(modifiers))
			builder.append("public ");
		if (Modifier.isPrivate(modifiers))
			builder.append("private ");
		if (Modifier.isProtected(modifiers))
			builder.append("protected ");
		if (Modifier.isStatic(modifiers))
			builder.append("static ");
		if (Modifier.isSynchronized(modifiers))
			builder.append("synchronized ");

		builder.append(method.getReturnType());
		builder.append(' ');
		builder.append(method.getDeclaringClass());
		builder.append('.');
		builder.append(method.getName());
		builder.append('(');

		Class<?>[] parameterTypes = method.getParameterTypes();
		boolean first = true;

		for (Class<?> param : parameterTypes) {
			if (!first)
				builder.append(", ");

			builder.append(param.getName());
			first = false;
		}

		builder.append(')');

		return builder.toString();
	}

}
