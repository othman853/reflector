package br.mikhas.reflector.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import br.mikhas.reflector.ObjectProxy;
import br.mikhas.reflector.Reflect;

/**
 * Core implementation of {@link AnnotationProxy} using defatult reflection API.
 * 
 * @author Mikhail Domanoski
 * 
 */
public class CoreAnnotationProxy implements AnnotationProxy {

	/**
	 * The annotation object being reflected
	 */
	protected final Annotation annotation;
	/**
	 * The refractor to help dealing with reflaction
	 */
	protected ObjectProxy<Annotation> refractor;

	private Map<String, AnnotationField> fieldsCache = new HashMap<String, AnnotationField>();

	/**
	 * Creates a new instance of the annotation proxy
	 * 
	 * @param reflectionFactory
	 * 
	 * @param annotation
	 */
	public CoreAnnotationProxy(Annotation annotation) {

		this.annotation = annotation;
		this.refractor = Reflect.on(this.annotation);
	}

	/**
	 * Gets the value of a field
	 */
	@Override
	public AnnotationField field(String name) {
		return doGetField(name);
	}

	@Override
	public AnnotationField[] fields() {
		Method[] methods = this.annotation.getClass().getDeclaredMethods();
		AnnotationField[] fields = new AnnotationField[methods.length];

		for (int i = 0; i < methods.length; i++) {
			fields[i] = doGetField(methods[i].getName());
		}

		return fields;
	}

	/**
	 * Gets field from cache or creates it
	 * 
	 * @param name
	 *            The name of the field
	 */
	private AnnotationField doGetField(String name) {
		AnnotationField field = this.fieldsCache.get(name);

		if (field == null) {
			field = new CoreAnnotationField(annotation, name);
			this.fieldsCache.put(name, field);
		}

		return field;
	}

	/**
	 * Gets the annotation object
	 */
	@Override
	public Annotation getAnnotation() {
		return this.annotation;
	}

	/**
	 * Gets the type of the annotation
	 */
	@Override
	public Class<? extends Annotation> getType() {
		return this.annotation.annotationType();
	}

	@Override
	public String name() {
		return this.annotation.annotationType().getSimpleName();
	}
}
