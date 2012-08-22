package br.mikhas.reflector;

import java.lang.reflect.Method;

import javassist.util.proxy.MethodFilter;
import javassist.util.proxy.ProxyFactory;
import javassist.util.proxy.ProxyObject;

public abstract class ProxyUtils {
	private ProxyUtils() {

	}

	public static boolean isProxyClass(Class<?> cls) {
		if (cls == null)
			return false;

		return ProxyObject.class.isAssignableFrom(cls);
	}

	public static boolean isProxyObject(Object object) {
		if (object == null)
			return false;

		return object instanceof ProxyObject;
	}

	public static Class<? extends ProxyObject> createProxyClass(Class<?> cls) {
		return createProxyClass(cls, null);
	}

	@SuppressWarnings("unchecked")
	public static Class<? extends ProxyObject> createProxyClass(Class<?> cls,
			final Method[] filter) {
		ProxyFactory factory = new ProxyFactory();

		factory.setSuperclass(cls);

		if (filter != null) {

			MethodFilter mfilter = new MethodFilter() {
				public boolean isHandled(Method m) {

					for (Method method : filter) {
						if (m.getName().equals(method.getName()))
							if (m.getReturnType()
									.equals(method.getReturnType()))
								if (ReflectionUtils.assignable(m
										.getParameterTypes(), method
										.getParameterTypes()))
									return false;
					}

					return true;

				}
			};
			factory.setFilter(mfilter);
		}

		return (Class<? extends ProxyObject>) factory.createClass();
	}
}
