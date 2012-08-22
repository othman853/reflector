package br.mikhas.reflector;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import br.mikhas.reflector.field.FieldProxy;
import br.mikhas.reflector.method.MethodProxy;

/**
 * Base class of Reflector library.
 * <p>
 * Reflection features can be easily accessed from this class using friendly
 * method names.
 * 
 * @author Mikhail Domanoski
 * 
 */
public final class Reflect {

	private static Reflector reflector = new Reflector();

	/**
	 * Using a private constructor to keep it an untility class
	 */
	private Reflect() {

	}

	/**
	 * Applies Reflector features to an instance of the Class
	 * 
	 * @param <T>
	 *            The type that the Class represents
	 * @param cls
	 *            The class to be reflected
	 * @return A proxy that facilitates the use of reflection over the Object
	 */
	public static <T> ObjectProxy<T> onInstance(Class<T> cls) {
		return reflector.onInstance(cls);
	}

	/**
	 * Applies Reflector features to an instance of a Class
	 * 
	 * @param cls
	 *            The full qualified name of the class to be reflected
	 * @return A proxy that facilitates the use of reflection over the Object
	 */
	public static ObjectProxy<?> onInstance(String cls) {
		return reflector.onInstance(cls);
	}

	/**
	 * Applies Refractor features to an Object
	 * 
	 * @param object
	 *            The object that will be wrapped into an Reflector proxy
	 * @return A proxy that facilitates the use of reflection over the Object
	 */
	public static <T> ObjectProxy<T> on(T object) {
		return reflector.on(object);
	}

	/**
	 * Applies Reflector features to a Class
	 * 
	 * @param clazz
	 *            The Class that will be wrapped into an Refractor proxy
	 * @return A proxy that facilitates the use of reflection over the Class
	 */
	public static <T> ClassProxy<T> on(Class<T> clazz) {
		return reflector.on(clazz);
	}

	/**
	 * Applies Reflector features to a Class
	 * 
	 * @param className
	 *            The name of the class that will be wrapped into an Refractor
	 *            proxy
	 * @return A proxy that facilitates the use of reflection over the Class
	 */
	public static ClassProxy<?> on(String className) {
		return reflector.on(className);
	}

	/**
	 * Applies Reflector features to a Field
	 * 
	 * @param field
	 *            The field which will be wrapped
	 * @return A proxy that facilitates the use of reflection over the Field
	 */
	public static FieldProxy on(Field field) {
		return reflector.on(field);
	}

	/**
	 * Applies Reflector features to a Method
	 * 
	 * @param method
	 *            The method which will be wrapped
	 * @return A proxy that facilitates the use of reflection over the Method
	 */
	public static MethodProxy on(Method method) {
		return reflector.on(method);
	}

	/**
	 * Sets the default reflector
	 * 
	 * @param reflector
	 */
	public static void setDefaultReflector(Reflector reflector) {
		if (reflector == null)
			throw new NullPointerException("Default reflector must not be null");

		Reflect.reflector = reflector;
	}
}
