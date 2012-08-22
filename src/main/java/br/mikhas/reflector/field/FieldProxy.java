package br.mikhas.reflector.field;

import java.lang.reflect.Field;

import br.mikhas.reflector.AnnotatedElement;

public interface FieldProxy extends AnnotatedElement {

	/**
	 * Sets a new value value to the field
	 * 
	 * @param value
	 *            The new value for the field
	 */
	public void set(Object value);

	/**
	 * Sets a new value value to the field on the given target
	 * 
	 * @param target
	 *            The target object to change the field value
	 * @param value
	 *            The new value for the field
	 */
	public void set(Object target, Object value);

	/**
	 * Gets the value of the field
	 * 
	 * @param <T>
	 *            The type of the field value
	 * @return The value of the field
	 */
	public <T> T get();

	/**
	 * Gets the value of the field from the target object
	 * 
	 * @param <T>
	 *            The type of the field value
	 * @param target
	 *            The object which contains the field
	 * @return The value of the field
	 */
	public <T> T get(Object target);

	/**
	 * Gets the name of the field
	 * 
	 * @return the name of the field
	 */
	public String name();

	/**
	 * Gets the type of the field
	 * 
	 * @return The type of the field
	 */
	public Class<?> type();

	/**
	 * Checks if the field is static
	 * 
	 * @return <tt>true</tt> if the field is static
	 */
	public boolean isStatic();

	/**
	 * Checks if the field is private
	 * 
	 * @return <tt>true</tt> if the field is private
	 */
	public boolean isPrivate();

	/**
	 * Checks if the field is public
	 * 
	 * @return <tt>true</tt> if the field is public
	 */
	public boolean isPublic();

	/**
	 * Checks if the field contains any annotation
	 * 
	 * @return <tt>true</tt> if the field contains an annotation
	 */
	public boolean isAnnotated();

	/**
	 * Checks if the field is final
	 * 
	 * @return <tt>true</tt> if the field is final
	 */
	public boolean isFinal();

	/**
	 * Checks if the field is transient
	 * 
	 * @return <tt>true</tt> if the field is transient
	 */
	public boolean isTransient();

	/**
	 * Checks if the field is volatile
	 * 
	 * @return <tt>true</tt> if the field is volatile
	 */
	public boolean isVolatile();

	/**
	 * Gets the wrapped field
	 * 
	 * @return
	 */
	public Field field();
}
