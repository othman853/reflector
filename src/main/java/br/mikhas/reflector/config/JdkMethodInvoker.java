package br.mikhas.reflector.config;

import java.lang.reflect.Method;

import br.mikhas.reflector.ReflectionUtils;
import br.mikhas.reflector.method.MethodInvocationException;

class JdkMethodInvoker implements MethodInvoker {

	private final Method method;

	public JdkMethodInvoker(Method method) {
		this.method = method;
	}

	@Override
	public Object invoke(Object target, Object... args) {
		try {
			return method.invoke(target, args);
		} catch (Exception e) {
			throw new MethodInvocationException("Error while calling method: "
					+ ReflectionUtils.getSignature(method), e);
		}
	}

}
