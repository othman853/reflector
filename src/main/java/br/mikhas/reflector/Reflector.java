package br.mikhas.reflector;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import br.mikhas.reflector.config.JdkReflectionFactory;
import br.mikhas.reflector.config.ReflectionFactory;
import br.mikhas.reflector.field.FieldProxy;
import br.mikhas.reflector.method.MethodProxy;

/**
 * Base class of Reflector library.
 * <p>
 * Reflection features can be easily accessed from this class using friendly
 * method names
 * 
 * @author Mikhail Domanoski
 * 
 */
public class Reflector {

	/**
	 * Caches the already created proxies
	 */
	private final Map<Class<?>, ClassProxy<?>> classProxyCache;

	private final Map<Class<?>, Integer> classProxyGetCount;

	private final static int PROXY_GET_THRESHOLD = 20;

	private final ReflectionFactory reflectionFactory;

	{
		classProxyCache = new ConcurrentHashMap<Class<?>, ClassProxy<?>>(50);
		classProxyGetCount = new ConcurrentHashMap<Class<?>, Integer>(100);
	}

	/**
	 * Using a private constructor to keep it an untility class
	 */
	public Reflector(ReflectionFactory reflectionFactory) {
		if (reflectionFactory == null)
			throw new NullPointerException("ReflectionFactory must not be null");
		this.reflectionFactory = reflectionFactory;
	}

	public Reflector() {
		this(new JdkReflectionFactory());
	}

	/**
	 * Applies Reflector features to an instance of the Class
	 * 
	 * @param <T>
	 *            The type that the Class represents
	 * @param cls
	 *            The class to be reflected
	 * @return A proxy that facilitates the use of reflection over the Object
	 */
	public <T> ObjectProxy<T> onInstance(Class<T> cls) {
		return on(on(cls).newInstance());
	}

	/**
	 * Applies Reflector features to an instance of a Class
	 * 
	 * @param cls
	 *            The full qualified name of the class to be reflected
	 * @return A proxy that facilitates the use of reflection over the Object
	 */
	public ObjectProxy<?> onInstance(String cls) {
		return on(on(cls).newInstance());
	}

	/**
	 * Applies Refractor features to an Object
	 * 
	 * @param object
	 *            The object that will be wrapped into an Reflector proxy
	 * @return A proxy that facilitates the use of reflection over the Object
	 */
	public <T> ObjectProxy<T> on(T object) {
		return new CoreObjectProxy<T>(reflectionFactory, object);
	}

	/**
	 * Applies Reflector features to a Class
	 * 
	 * @param clazz
	 *            The Class that will be wrapped into an Refractor proxy
	 * @return A proxy that facilitates the use of reflection over the Class
	 */
	public <T> ClassProxy<T> on(Class<T> clazz) {
		assert clazz != null;
		return fromCache(clazz);
	}

	/**
	 * Gets {@link ClassProxy} from cache if it exists.
	 * 
	 * @param <T>
	 *            The class wrapped by the classproxy
	 * @param clazz
	 *            The clazz to be wrapped by the proxy
	 * @return The {@link ClassProxy} for the clazz from the proxy, if it
	 *         exists, or a new instance of it
	 */
	@SuppressWarnings("unchecked")
	private synchronized <T> ClassProxy<T> fromCache(Class<T> clazz) {

		ClassProxy<T> classProxy = (ClassProxy<T>) classProxyCache.get(clazz);

		if (classProxy == null) {
			classProxy = new CoreClassProxy<T>(reflectionFactory, clazz);
			int count = getProxyGetCount(clazz);

			if (count >= PROXY_GET_THRESHOLD) {
				classProxyGetCount.remove(clazz);
				classProxyCache.put(clazz, classProxy);
			} else {
				incrementProxyGetCount(clazz);
			}
		}

		return classProxy;
	}

	/**
	 * Gets how many times the proxy has been required from the cache
	 * 
	 * @param clazz
	 *            The clazz which the proxy is being requested
	 * @return the amount of times which the class proxy has been requested from
	 *         the cache
	 */
	private int getProxyGetCount(Class<?> clazz) {
		Integer integer = classProxyGetCount.get(clazz);
		return integer == null ? 0 : integer.intValue();
	}

	/**
	 * Increments the amount of times a proxy has been requested from the cache
	 * 
	 * @param clazz
	 */
	private void incrementProxyGetCount(Class<?> clazz) {
		int count = getProxyGetCount(clazz);
		classProxyGetCount.put(clazz, count + 1);
	}

	/**
	 * Applies Reflector features to a Class
	 * 
	 * @param className
	 *            The name of the class that will be wrapped into an Refractor
	 *            proxy
	 * @return A proxy that facilitates the use of reflection over the Class
	 */
	public ClassProxy<?> on(String className) {
		assert className != null && !className.isEmpty();
		try {
			Class<?> cls = Class.forName(className);
			return on(cls);
		} catch (ClassNotFoundException e) {
			throw new ReflectionException("Could not find class: " + className,
					e);
		}
	}

	/**
	 * Applies Reflector features to a Field
	 * 
	 * @param field
	 *            The field which will be wrapped
	 * @return A proxy that facilitates the use of reflection over the Field
	 */
	public FieldProxy on(Field field) {
		assert field != null;

		Class<?> cls = field.getDeclaringClass();

		return on(cls).field(field.getName());
	}

	/**
	 * Applies Reflector features to a Method
	 * 
	 * @param method
	 *            The method which will be wrapped
	 * @return A proxy that facilitates the use of reflection over the Method
	 */
	public MethodProxy on(Method method) {
		assert method != null;

		Class<?> cls = method.getDeclaringClass();

		return on(cls).method(method.getName(), method.getParameterTypes());
	}
}
