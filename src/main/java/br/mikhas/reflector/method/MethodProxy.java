package br.mikhas.reflector.method;

import java.lang.reflect.Method;

import br.mikhas.reflector.AnnotatedElement;

/**
 * Defines methods handle methods.
 * 
 * @author Mikhail Domanoski
 * 
 */
public interface MethodProxy extends AnnotatedElement {
	/**
	 * Gets the name of the method
	 * 
	 * @return The name of the method
	 */
	public String name();

	/**
	 * Invokes the method passing it's parameters
	 * 
	 * @param args
	 *            The method arguments
	 * @return The return value of the method
	 */
	public Object invoke(Object... args);

	/**
	 * Checks if the method is static
	 * 
	 * @return <code>true</code> is it's an static method
	 */
	public boolean isStatic();

	public boolean isPublic();

	public boolean isPrivate();

	/**
	 * Gets the method return type
	 * 
	 * @return the return type
	 */
	public Class<?> type();

	/**
	 * Gets an array with the parameter definition and metadata
	 * 
	 * @return an array with the method parameters
	 */
	public Parameter[] parameters();

	/**
	 * Gets the wrapped method by Reflector
	 * 
	 * @return The wrapped method
	 */
	public Method method();

	/**
	 * Sets the method invokation target and invokes the method
	 * 
	 * @param target
	 *            The method invokation target
	 * @param args
	 *            The method invokation arguments
	 * @return the method invokation result
	 */
	public Object invokeOn(Object target, Object... args);
}
