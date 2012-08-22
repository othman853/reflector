package br.mikhas.reflector.annotation;

import java.lang.annotation.Annotation;

/**
 * Provides access to annotation information and metada data which it holds
 * 
 * @author Mikhail Domanoski
 * 
 */
public interface AnnotationProxy {
	/**
	 * Gets the value of a field of the annotation
	 * 
	 * @param name
	 *            The name of the field to get
	 * @return The value of the field metadata
	 */
	public AnnotationField field(String name);

	/**
	 * Gets an array with all annotation fields
	 * 
	 * @return The field in this annotation
	 */
	public AnnotationField[] fields();

	/**
	 * Gets the annotation object
	 * 
	 * @return The annotation
	 */
	public Annotation getAnnotation();

	/**
	 * Gets the type of the annotation
	 * 
	 * @return The type of annotation
	 */
	public Class<? extends Annotation> getType();

	/**
	 * Gets the name of the annotation
	 * 
	 * @return The name of teh annotation
	 */
	public String name();
}
