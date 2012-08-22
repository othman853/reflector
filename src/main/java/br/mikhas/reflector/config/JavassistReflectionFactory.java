package br.mikhas.reflector.config;

import java.lang.reflect.Method;

public class JavassistReflectionFactory extends UnsafeReflectionFactory {

	protected final NativeMethodInvokerFactory factory = NativeMethodInvokerFactory
			.getDefault();

	@Override
	public MethodInvoker getMethodInvoker(Method method) {
		if (NativeMethodInvokerFactory.isAvailable()) {
			try {
				return factory.getFor(method);
			} catch (Exception e) {
				return super.getMethodInvoker(method);
			}
		} else
			return super.getMethodInvoker(method);
	}

}
