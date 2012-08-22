package br.mikhas.reflector.method;

import java.lang.reflect.Method;

import br.mikhas.reflector.ReflectionException;
import br.mikhas.reflector.ReflectionUtils;
import br.mikhas.reflector.config.ReflectionFactory;

public class AmbiguousMethodProxyImpl implements AmbiguousMethodProxy {

	private final String name;
	private final Class<?> clazz;
	private Object object;
	private final ReflectionFactory reflectionFactory;

	public AmbiguousMethodProxyImpl(ReflectionFactory reflectionFactory,
			Class<?> clazz, String name) {
		this.reflectionFactory = reflectionFactory;
		this.name = name;
		this.clazz = clazz;
	}

	public AmbiguousMethodProxyImpl(ReflectionFactory reflectionFactory,
			Class<?> clazz, String name, Object object) {
		this.reflectionFactory = reflectionFactory;
		this.name = name;
		this.clazz = clazz;
		this.object = object;
	}

	public MethodProxy withArgs(Class<?>... classes) {
		try {
			Method method = ReflectionUtils.lookupMethod(this.clazz, name,
					classes);

			if (method == null) {
				throw new ReflectionException("Method not found: " + this.name
						+ '(' + ReflectionUtils.typeNames(classes) + ')');
			}

			return new CoreMethodProxy(reflectionFactory, clazz, method,
					object == null ? clazz : object);

		} catch (SecurityException e) {
			throw new ReflectionException("Permission denied to method "
					+ this.name + ReflectionUtils.typeNames(classes), e);
		}
	}

	public MethodProxy withoutArgs() {
		try {
			Method method = this.clazz.getDeclaredMethod(name);

			return new CoreMethodProxy(reflectionFactory, clazz, method,
					object == null ? clazz : object);
		} catch (SecurityException e) {
			throw new ReflectionException("Permission denied to method "
					+ this.name + "()", e);
		} catch (NoSuchMethodException e) {
			throw new ReflectionException("Method not found: " + this.name
					+ "()", e);
		}
	}

	public String name() {
		return this.name;
	}

	public Object invoke(Object... objects) {
		if (objects.length > 0) {
			Class<?>[] types;

			try {
				types = ReflectionUtils.getArgumentsTypes(objects);
			} catch (NullPointerException npe) {
				throw new MethodInvocationException(
						"Could not determine the type of all arguments on array",
						npe);
			}

			MethodProxy proxy = this.withArgs(types);
			return proxy.invoke(objects);

		} else {
			MethodProxy proxy = this.withoutArgs();
			return proxy.invoke();
		}
	}
}
