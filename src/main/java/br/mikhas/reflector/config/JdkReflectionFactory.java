package br.mikhas.reflector.config;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class JdkReflectionFactory implements ReflectionFactory {

	@Override
	public FieldAccessor getFieldAcessor(Field field) {
		return new JdkFieldAccessor(field);
	}

	@Override
	public MethodInvoker getMethodInvoker(Method method) {
		return new JdkMethodInvoker(method);
	}

}
