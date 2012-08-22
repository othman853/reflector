package br.mikhas.reflector;

import br.mikhas.reflector.method.Parameter;

/**
 * A simple interface to handle a object contructor
 * 
 * @author Mikhail Domanoski
 * 
 */
public interface ConstructorProxy<T> extends AnnotatedElement {

	/**
	 * Invokes the object constructor
	 * 
	 * @param args
	 *            the arguments of the contructor
	 * @return an instance of the object
	 */
	public T invoke(Object... args);

	public boolean isPublic();

	public boolean isPrivate();

	/**
	 * Gets an array with the parameter definition and metadata
	 * 
	 * @return an array with the method parameters
	 */
	public Parameter[] parameters();

}
