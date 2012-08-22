package br.mikhas.reflector;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import br.mikhas.reflector.annotation.AnnotationProxy;
import br.mikhas.reflector.annotation.CoreAnnotationProxy;
import br.mikhas.reflector.method.Parameter;

/**
 * Implements {@link ConstructorProxy} using standard Reflection API.
 * 
 * @author Mikhail Domanoski
 * 
 * @param <T>
 *            The type which the constructor belongs
 */
class CoreConstructorProxy<T> implements ConstructorProxy<T> {

	/**
	 * The handled constructor
	 */
	private final Constructor<?> constructor;

	/**
	 * Cache the constructor annotations
	 */
	private Map<Annotation, AnnotationProxy> annotationProxyCache = new HashMap<Annotation, AnnotationProxy>();

	private int modifiers;

	/**
	 * Creates a new instance of the constructor proxy
	 * 
	 * @param constructor
	 *            The constructor to be reflected
	 */
	CoreConstructorProxy(Constructor<T> constructor) {
		this.modifiers = constructor.getModifiers();
		this.constructor = constructor;
	}

	@SuppressWarnings("unchecked")
	public T invoke(Object... args) {
		try {
			return (T) this.constructor.newInstance(args);
		} catch (IllegalArgumentException e) {
			throw new ReflectionException(
					"The constructor arguments are incorrenct.", e);
		} catch (InstantiationException e) {
			throw new ReflectionException(
					"An error occured while creating object.", e);
		} catch (IllegalAccessException e) {
			throw new ReflectionException("Access denied to this constructor.",
					e);
		} catch (InvocationTargetException e) {
			throw new ReflectionException(
					"The constructor does not belongs to the target object.", e);
		}
	}

	@Override
	public boolean isPrivate() {
		return Modifier.isPrivate(modifiers);
	}

	@Override
	public boolean isPublic() {
		return Modifier.isPublic(modifiers);
	}

	@Override
	public Parameter[] parameters() {
		Class<?>[] parameterTypes = this.constructor.getParameterTypes();
		Annotation[][] parametersAnnotations = this.constructor
				.getParameterAnnotations();
		Parameter[] res = new Parameter[parameterTypes.length];

		for (int i = 0; i < parameterTypes.length; i++) {
			res[i] = new Parameter(i, parameterTypes[i],
					parametersAnnotations[i]);
		}

		return res;
	}

	@Override
	public AnnotationProxy annotation(Class<? extends Annotation> annotation) {
		Annotation a = this.constructor.getAnnotation(annotation);
		return getAnnotation(a);
	}

	private AnnotationProxy getAnnotation(Annotation annotation) {
		AnnotationProxy annotationProxy = annotationProxyCache.get(annotation);

		if (annotationProxy == null) {
			annotationProxy = new CoreAnnotationProxy(annotation);
			annotationProxyCache.put(annotation, annotationProxy);
		}

		return annotationProxy;
	}

	@Override
	public AnnotationProxy[] annotations() {
		Annotation[] annotations = this.constructor.getAnnotations();

		AnnotationProxy[] proxies = new AnnotationProxy[annotations.length];

		for (int i = 0; i < proxies.length; i++) {
			proxies[i] = getAnnotation(annotations[i]);
		}

		return proxies;
	}

	@Override
	public boolean isAnnotated(Class<? extends Annotation> annotation) {
		return this.constructor.isAnnotationPresent(annotation);
	}

	@Override
	public boolean isAnnotated() {
		return this.constructor.getAnnotations().length > 0;
	}

}
