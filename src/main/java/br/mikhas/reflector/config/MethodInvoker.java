package br.mikhas.reflector.config;

/**
 * A runtime built method invoker which can execute native method calls on java
 * objects.
 * 
 * @author Mikhail Domanoski
 * 
 */
public interface MethodInvoker {
	/**
	 * Invoke the method on a target object.
	 * 
	 * @param target
	 *            The target object
	 * @param args
	 *            The method arguments on an array
	 * @return The result of the method or <code>null</code> if the method
	 *         return is <code>void</code>
	 */
	public Object invoke(Object target, Object... args);
}
