package br.mikhas.reflector;

import java.lang.annotation.Annotation;

import br.mikhas.reflector.annotation.AnnotationProxy;

/**
 * Defines the methods to get the annotations from Annotated Elements
 * 
 * @author Mikhail Domanoski
 */
public interface AnnotatedElement {

	/**
	 * Gets the annotation with the providen class from the elements
	 * 
	 * @param annotation The class of the annotation
	 * @return an annotations handler
	 */
	public AnnotationProxy annotation(Class<? extends Annotation> annotation);

	/**
	 * Gets all annotations from the element
	 * 
	 * @return the element's annotations
	 */
	public AnnotationProxy[] annotations();

	/**
	 * Checks if the elements is annotated with an annotations of the providen
	 * class
	 * 
	 * @param annotation the class of the annotation
	 * @return an annotations handler
	 */
	public boolean isAnnotated(Class<? extends Annotation> annotation);

	/**
	 * Checks if the element is annotated
	 * 
	 * @return <code>true</code> if the element has nay annotation
	 */
	public boolean isAnnotated();
}
