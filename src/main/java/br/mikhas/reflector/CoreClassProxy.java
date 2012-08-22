package br.mikhas.reflector;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import br.mikhas.reflector.annotation.AnnotationProxy;
import br.mikhas.reflector.annotation.CoreAnnotationProxy;
import br.mikhas.reflector.config.ReflectionFactory;
import br.mikhas.reflector.field.ClassFieldProxy;
import br.mikhas.reflector.field.FieldProxy;
import br.mikhas.reflector.field.FieldReflectionException;
import br.mikhas.reflector.method.AmbiguousMethodProxy;
import br.mikhas.reflector.method.AmbiguousMethodProxyImpl;
import br.mikhas.reflector.method.CoreMethodProxy;
import br.mikhas.reflector.method.MethodProxy;
import br.mikhas.reflector.method.Parameter;

/**
 * Implements easy-to-use reflection on a Class using standard reflaction API.
 * 
 * @author Mikhail Domanoski
 * 
 * @param <T>
 *            The type that the Class represents
 */
class CoreClassProxy<T> implements ClassProxy<T> {

	/**
	 * The wrapped class
	 */
	protected final Class<T> clazz;

	/**
	 * Caches the reflected fields of this class
	 */
	protected static Map<Field, FieldProxy> fieldsCache;

	/**
	 * Caches the reflected annotation of this class
	 */
	protected static Map<Annotation, AnnotationProxy> annotationsCache;

	/**
	 * Caches the reflected methods of this class
	 */
	protected static Map<Method, MethodProxy> methodsCache;

	/**
	 * Caches the implemented interfaces of this class
	 */
	private final Set<Class<?>> interfaces = new LinkedHashSet<Class<?>>();

	protected final ReflectionFactory reflectionFactory;

	/**
	 * <p>
	 * Main constructor of the ClassProxy
	 * <p>
	 * Accessible only from inside the framework package
	 * 
	 * @param clazz
	 *            The class to be wrapped by the proxy
	 */
	CoreClassProxy(ReflectionFactory reflectionFactory, Class<T> clazz) {
		this.reflectionFactory = reflectionFactory;
		this.clazz = clazz;
		Collection<Class<?>> i = Arrays.asList(this.clazz.getInterfaces());
		this.interfaces.addAll(i);
	}

	/**
	 * Creates and return an instance of the wrapped class
	 */
	public T newInstance() {
		try {
			if (this.clazz.isInterface() || this.clazz.isPrimitive()) {
				throw new ReflectionException(
						"Illegal to instantiate an interface or primitive.");
			}
			return this.clazz.newInstance();
		} catch (InstantiationException e) {
			throw new ReflectionException(
					"An exception was throwed while instantiating the class: "
							+ this.name(), e);
		} catch (IllegalAccessException e) {
			throw new ReflectionException(
					"Access denied to access this constructor: " + this.name(),
					e);
		}
	}

	/**
	 * Returns a wrapper to the consreuctor of this class
	 */
	public ConstructorProxy<T> constructor(Class<?>... args) {
		try {

			Constructor<T> constructor = ReflectionUtils.lookupConstructor(
					this.clazz, args);

			if (constructor == null) {
				throw new ReflectionException(
						"Could not find constructor with arguments: "
								+ ReflectionUtils.typeNames(args));
			}

			return new CoreConstructorProxy<T>(constructor);
		} catch (SecurityException e) {
			throw new ReflectionException(e);
		}
	}

	/**
	 * Gets the name of the class
	 * 
	 * @return The Simple name of the class
	 */
	public String name() {
		return this.clazz.getSimpleName();
	}

	/**
	 * gets the name of the class
	 * 
	 * @return The fully quilified name of the class
	 */
	public String fullName() {
		return this.clazz.getName();
	}

	/**
	 * Wrapps the named field into an
	 */
	public FieldProxy field(String name) {
		try {
			Field field = this.clazz.getDeclaredField(name);

			return this.getFieldProxy(field);
		} catch (SecurityException e) {
			throw new FieldReflectionException("No access to the attribute: "
					+ name, e);
		} catch (NoSuchFieldException e) {
			throw new FieldReflectionException(
					"The attribute does not exists: " + name, e);
		}
	}

	/**
	 * Gets and wrapps all declared fields
	 */
	public FieldProxy[] fields() {
		Field[] fields = this.clazz.getDeclaredFields();
		ArrayList<FieldProxy> proxies = new ArrayList<FieldProxy>(fields.length);

		for (int i = 0; i < fields.length; i++) {
			proxies.add(this.getFieldProxy(fields[i]));
		}

		return proxies.toArray(new FieldProxy[proxies.size()]);
	}

	/**
	 * Returns a new or a cached FieldProxy
	 * 
	 * @param field
	 *            The field to be wrapped into a proxy
	 * @return The wrapped field
	 */
	private FieldProxy getFieldProxy(Field field) {
		if (fieldsCache == null) {
			fieldsCache = new HashMap<Field, FieldProxy>();
		}

		FieldProxy proxy = fieldsCache.get(field);
		if (proxy == null) {

			proxy = new ClassFieldProxy(reflectionFactory, this.clazz, field);

			fieldsCache.put(field, proxy);
		}
		return proxy;
	}

	private MethodProxy getMethodProxy(Method method) {
		if (methodsCache == null) {
			methodsCache = new HashMap<Method, MethodProxy>();
		}

		MethodProxy proxy = methodsCache.get(method);
		if (proxy == null) {
			proxy = new CoreMethodProxy(reflectionFactory, this.clazz, method,
					this.clazz);
			methodsCache.put(method, proxy);
		}
		return proxy;
	}

	private AnnotationProxy getAnnotationProxy(Annotation annotation) {
		if (annotationsCache == null) {
			annotationsCache = new HashMap<Annotation, AnnotationProxy>();
		}

		AnnotationProxy proxy = annotationsCache.get(annotation);
		if (proxy == null) {
			proxy = new CoreAnnotationProxy(annotation);
			annotationsCache.put(annotation, proxy);
		}
		return proxy;
	}

	public AmbiguousMethodProxy method(String name) {
		return new AmbiguousMethodProxyImpl(reflectionFactory, this.clazz, name);
	}

	public MethodProxy method(String name, Class<?>... types) {
		try {
			Method method;

			if (types == null || types.length == 0) {
				method = this.noArgMethod(name);
			} else {
				method = ReflectionUtils.lookupMethod(this.clazz, name, types);
			}

			return this.getMethodProxy(method);
		} catch (SecurityException e) {
			throw new ReflectionException(e);
		}
	}

	public MethodProxy method(String name, Parameter... parameters) {

		if (parameters == null || parameters.length == 0)
			return getMethodProxy(this.noArgMethod(name));

		Class<?>[] types = new Class<?>[parameters.length];

		for (int i = 0; i < parameters.length; i++) {
			types[i] = parameters[i].type();
		}

		return this.method(name, types);
	}

	private Method noArgMethod(String name) {
		try {
			return this.clazz.getDeclaredMethod(name);
		} catch (SecurityException e) {
			throw new ReflectionException(e);
		} catch (NoSuchMethodException e) {
			throw new ReflectionException(e);
		}
	}

	public MethodProxy[] methods() {
		Method[] methods = this.clazz.getDeclaredMethods();
		MethodProxy[] proxies = new MethodProxy[methods.length];

		for (int i = 0; i < methods.length; i++) {
			proxies[i] = this.getMethodProxy(methods[i]);
		}

		return proxies;
	}

	public AnnotationProxy annotation(Class<? extends Annotation> annotation) {
		if (this.clazz.isAnnotationPresent(annotation)) {
			Annotation annotation2 = this.clazz.getAnnotation(annotation);
			return this.getAnnotationProxy(annotation2);
		}
		return null;
	}

	public AnnotationProxy[] annotations() {
		Annotation[] annotations = this.clazz.getAnnotations();
		AnnotationProxy[] proxies = new AnnotationProxy[annotations.length];

		for (int i = 0; i < annotations.length; i++) {
			proxies[i] = this.getAnnotationProxy(annotations[i]);
		}

		return proxies;
	}

	/**
	 * Checks if the class is annotated with the given annotation
	 */
	public boolean isAnnotated(Class<? extends Annotation> annotation) {
		return this.clazz.isAnnotationPresent(annotation);
	}

	/**
	 * Checks if the current class contains any annotation
	 */
	public boolean isAnnotated() {
		return this.clazz.getAnnotations().length > 0;
	}

	/**
	 * Checks if the class implements the given interface
	 */
	public boolean implementationOf(Class<?> interfac) {
		if (!interfac.isInterface()) {
			throw new IllegalArgumentException(
					"The given class is not an interface: " + interfac);
		}
		return this.interfaces.contains(interfac);
	}

	/**
	 * List all possible constructors for <b>T</b>
	 */
	@SuppressWarnings("unchecked")
	public ConstructorProxy<T>[] constructors() {
		Constructor<T>[] nativeConstructors = (Constructor<T>[]) this.clazz
				.getDeclaredConstructors();

		ConstructorProxy<T>[] proxies = (ConstructorProxy<T>[]) new ConstructorProxy[nativeConstructors.length];

		for (int i = 0; i < proxies.length; i++) {
			proxies[i] = new CoreConstructorProxy<T>(nativeConstructors[i]);
		}

		return proxies;
	}

	@Override
	public String toString() {
		return this.clazz.toString();
	}
}
