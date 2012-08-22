package br.mikhas.reflector;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import br.mikhas.reflector.config.ReflectionFactory;
import br.mikhas.reflector.field.FieldProxy;
import br.mikhas.reflector.field.ObjectFieldProxy;
import br.mikhas.reflector.method.AmbiguousMethodProxy;
import br.mikhas.reflector.method.AmbiguousMethodProxyImpl;
import br.mikhas.reflector.method.CoreMethodProxy;
import br.mikhas.reflector.method.MethodProxy;

/**
 * Implements {@link ObjectProxy} using standard Reflection API.
 * 
 * @author Mikhail Domanoski
 * 
 * @param <T>
 *            The type of the object
 */
class CoreObjectProxy<T> extends CoreClassProxy<T> implements ObjectProxy<T> {

	/**
	 * The object
	 */
	protected final T object;

	/**
	 * Caches the reflected fields of this class
	 */
	protected Map<Field, FieldProxy> fieldsCache;

	/**
	 * Caches the reflected methods of this class
	 */
	protected Map<Method, MethodProxy> methodsCache;

	/**
	 * Creates a new instance of the proxy using an object
	 * 
	 * @param reflectionFactory
	 * 
	 * @param reflectionFactory
	 * 
	 * @param object
	 */
	@SuppressWarnings("unchecked")
	CoreObjectProxy(ReflectionFactory reflectionFactory, T object) {
		super(reflectionFactory, (Class<T>) object.getClass());

		this.object = object;
	}

	public T object() {
		return this.object;
	}

	@Override
	public FieldProxy field(String name) {
		try {
			Field field = this.clazz.getDeclaredField(name);
			return this.getFieldProxy(field);
		} catch (SecurityException e) {
			throw new ReflectionException("No access to such field: " + name);
		} catch (NoSuchFieldException e) {
			throw new ReflectionException("No such field: " + name);
		}
	}

	@Override
	public FieldProxy[] fields() {
		Field[] fields = this.clazz.getDeclaredFields();
		FieldProxy[] proxies = new FieldProxy[fields.length];

		for (int i = 0; i < fields.length; i++) {
			proxies[i] = this.getFieldProxy(fields[i]);
		}

		return proxies;
	}

	@Override
	public AmbiguousMethodProxy method(String name) {
		return new AmbiguousMethodProxyImpl(reflectionFactory, this.clazz,
				name, this.object);
	}

	@Override
	public MethodProxy method(String name, Class<?>... types) {
		try {
			Method method;

			if (types.length > 0) {
				method = ReflectionUtils.lookupMethod(this.clazz, name, types);

				if (method == null) {
					throw new ReflectionException("Method not found: " + name
							+ ReflectionUtils.typeNames(types));
				}

			} else {
				method = this.clazz.getDeclaredMethod(name);
			}

			return this.getMethodProxy(method);
		} catch (SecurityException e) {
			throw new ReflectionException(e);
		} catch (NoSuchMethodException e) {
			throw new ReflectionException(e);
		}
	}

	@Override
	public MethodProxy[] methods() {
		Method[] methods = this.clazz.getDeclaredMethods();
		MethodProxy[] proxies = new MethodProxy[methods.length];

		for (int i = 0; i < methods.length; i++) {
			proxies[i] = this.getMethodProxy(methods[i]);
		}

		return proxies;
	}

	private FieldProxy getFieldProxy(Field field) {
		if (fieldsCache == null) {
			fieldsCache = new HashMap<Field, FieldProxy>();
		}

		FieldProxy proxy = fieldsCache.get(field);
		if (proxy == null) {
			proxy = new ObjectFieldProxy(this.reflectionFactory, this.clazz,
					field, this.object);
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
					this.object);
			methodsCache.put(method, proxy);
		}
		return proxy;
	}

	public <C> C cast(Class<C> clazz) {
		return clazz.cast(this.object);
	}
}
