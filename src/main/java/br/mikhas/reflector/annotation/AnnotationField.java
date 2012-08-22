package br.mikhas.reflector.annotation;

/**
 * 
 * Defines an annotation field and defines access methods to it's properties
 * 
 * @author Mikhail Domanoski
 * 
 */
public interface AnnotationField {

	/**
	 * Gets the name of the field
	 * 
	 * @return the name
	 */
	public String name();

	/**
	 * Gets the type of the field
	 * 
	 * @return the type of the field
	 */
	public Class<?> type();

	/**
	 * Gets the value of the annotation field
	 * 
	 * @return Th value
	 */
	public <T> T get();

}
