package br.mikhas.reflector.config;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public interface ReflectionFactory {

	public FieldAccessor getFieldAcessor(Field field);

	public MethodInvoker getMethodInvoker(Method method);
}
