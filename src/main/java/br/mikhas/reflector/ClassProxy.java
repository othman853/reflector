package br.mikhas.reflector;

import br.mikhas.reflector.field.FieldProxy;
import br.mikhas.reflector.method.AmbiguousMethodProxy;
import br.mikhas.reflector.method.MethodProxy;
import br.mikhas.reflector.method.Parameter;

/**
 * Defines methods to get information about the class fields, methods and
 * annotations
 * 
 * @author Mikhail Domanoski
 * 
 */
public interface ClassProxy<T> extends AnnotatedElement {

	/**
	 * Try to find a constructor that match the given arguments
	 * 
	 * @param args
	 *            the arguments of the constructor
	 * @return a constructor handler
	 */
	public ConstructorProxy<T> constructor(Class<?>... args);

	/**
	 * List all possible constructors for the current class
	 * 
	 * @return an array with constructos for the current class
	 */
	public ConstructorProxy<T>[] constructors();

	/**
	 * Creates a new instance of the Class using a constructor that has no
	 * argument
	 * 
	 * @return an object that is an instance of the Class
	 */
	public T newInstance();

	/**
	 * Returns the named field
	 * 
	 * @param name
	 *            The name of the field
	 * @return a field handler
	 */
	public FieldProxy field(String name);

	/**
	 * Get all fields of the class
	 * 
	 * @return field handlers for all fields on the class
	 */
	public FieldProxy[] fields();

	/**
	 * Gets a method that the arguments are not defined or known
	 * 
	 * @param name
	 *            The name of the method
	 * @return A method handler
	 */
	public AmbiguousMethodProxy method(String name);

	/**
	 * Gets a method that the arguments types matches the given classes
	 * 
	 * @param name
	 *            The name of the method
	 * @param classes
	 *            The arguments types
	 * @return A method handler
	 */
	public MethodProxy method(String name, Class<?>... classes);

	/**
	 * Gets a method that the arguments types matches the given parameters
	 * 
	 * @param name
	 *            The name of the method
	 * @param parameters
	 *            The paramenters of the method
	 * @return A method handler
	 */
	public MethodProxy method(String name, Parameter... parameters);

	/**
	 * Gets all the methods of the class
	 * 
	 */
	public MethodProxy[] methods();

	/**
	 * Checks if the class is an implementation of an interface
	 * 
	 * @param interfac
	 * @return if the class implements the Interface
	 */
	public boolean implementationOf(Class<?> interfac);

	/**
	 * The name of the class
	 * 
	 * @return The name of the class
	 */
	public String name();

	public String fullName();
}
