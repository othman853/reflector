package br.mikhas.reflector.config;

import java.lang.reflect.Field;

import br.mikhas.reflector.field.FieldReflectionException;

/**
 * Uses Reflection API to access and change field value
 * 
 * @author Mikhail Domanoski
 * 
 */
class JdkFieldAccessor implements FieldAccessor {

	private final Field field;

	/**
	 * Creates a new instance of {@link JdkFieldAccessor}
	 * 
	 * @param field
	 *            The field to be accessed
	 */
	JdkFieldAccessor(Field field) {
		this.field = field;
		this.field.setAccessible(true);
	}

	/**
	 * Gets the value of the field using the default java Reflection API
	 */
	@Override
	public Object get(Object target) {
		try {
			return this.field.get(target);
		} catch (IllegalArgumentException e) {
			throw new FieldReflectionException(e);
		} catch (IllegalAccessException e) {
			throw new FieldReflectionException("Access denied to the field", e);
		}
	}

	/**
	 * Sets the value of the field using the default java Reflection API
	 */
	@Override
	public void set(Object target, Object value) {
		try {

			this.field.set(target, value);

		} catch (IllegalArgumentException e) {
			throw new FieldReflectionException(
					"The providen value is not legal", e);
		} catch (IllegalAccessException e) {
			throw new FieldReflectionException("Access denied to the field", e);
		}

	}

}
