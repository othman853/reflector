package br.mikhas.reflector.method;

/**
 * A middle tier method proxy that is used when the invoking method is not well
 * defined or known
 * 
 * @author Mikhail Domanoski
 */
public interface AmbiguousMethodProxy {
	/**
	 * Defines that the invoking method needs some arguments
	 * 
	 * @param classes
	 *            The types of the arguments
	 * @return the result of the method
	 */
	public MethodProxy withArgs(Class<?>... classes);

	/**
	 * Defines that the invoking method dont needs arguments
	 * 
	 * @return the result of the method
	 */
	public MethodProxy withoutArgs();

	/**
	 * The name of the method
	 * 
	 * @return The name of the method
	 */
	public String name();

	/**
	 * Try to find a method that matches with the providen arguments
	 * 
	 * @param objects
	 * @return the result of the method invokation
	 */
	public Object invoke(Object... objects);
}
