package br.mikhas.reflector.config;

import java.lang.reflect.Method;


public class MethodInvokerFactory {

	private static final NativeMethodInvokerFactory DEFAULT_NATIVE_FACTORY = NativeMethodInvokerFactory
			.getDefault();

	public static MethodInvoker getFor(Method method) {
		if (NativeMethodInvokerFactory.isAvailable()) {
			try {
				return DEFAULT_NATIVE_FACTORY.getFor(method);
			} catch (NativeInvokerCreationException e) {
				return new JdkMethodInvoker(method);
			}
		} else {
			return new JdkMethodInvoker(method);
		}
	}
}
