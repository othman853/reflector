package br.mikhas.reflector.config;

/**
 * The field accessor is responsible by accessing and altering the value of a
 * field.
 * 
 * @author Mikhail Domanoski
 * 
 */
public interface FieldAccessor {

	/**
	 * Gets the value of the field in the target object
	 * 
	 * @param target
	 *            The target object which holds the field
	 * @return The value of the field in the object
	 */
	public Object get(Object target);

	/**
	 * Sets the value of the field in the target object
	 * 
	 * @param target
	 *            The object which holds the field
	 * @param value
	 *            The new value of the field
	 */
	public void set(Object target, Object value);
}
