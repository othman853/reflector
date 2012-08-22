package br.mikhas.reflector;

/**
 * Provides access to object manipulation methods
 * 
 * @author Mikhail Domanoski
 * 
 * @param <T>
 *            The type of the object
 */
public interface ObjectProxy<T> extends ClassProxy<T> {

	/**
	 * Gets the object instance
	 * 
	 * @return the object
	 */
	public T object();

	/**
	 * Casts the objectto a type
	 * 
	 * @param <C>
	 *            The type which the object will be cast
	 * @param clazz
	 *            The type which the object will be cast
	 * @return The object
	 */
	public <C> C cast(Class<C> clazz);
}
