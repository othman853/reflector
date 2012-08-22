package br.mikhas.reflector.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import br.mikhas.reflector.ReflectionException;
import br.mikhas.reflector.method.MethodInvocationException;

public class CoreAnnotationField implements AnnotationField {

	/**
	 * The annotation which owns the field
	 */
	protected final Annotation annotation;

	/**
	 * The name of the field
	 */
	protected final String name;

	/**
	 * The method which access the field
	 */
	protected final Method method;

	public CoreAnnotationField(Annotation annotation, String name) {
		try {
			method = annotation.getClass().getDeclaredMethod(name);
			this.annotation = annotation;
			this.name = name;
		} catch (SecurityException e) {
			throw new ReflectionException("The field \"" + name
					+ "\" may not be accessed", e);
		} catch (NoSuchMethodException e) {
			throw new ReflectionException("The field \"" + name
					+ "\" does not exists on type: "
					+ annotation.annotationType(), e);
		}
	}

	@SuppressWarnings("unchecked")
	public <T> T get() {
		try {
			return (T) this.method.invoke(annotation);
		} catch (IllegalArgumentException e) {
			throw new MethodInvocationException("Could not invoke method \""
					+ this.name + "\" on type: " + this.type());
		} catch (IllegalAccessException e) {
			throw new MethodInvocationException("Method \"" + this.name
					+ "\" on type " + this.type() + " is not accessible");
		} catch (InvocationTargetException e) {
			throw new MethodInvocationException("The target " + this.annotation
					+ " does not contains method: " + this.name);
		}
	}

	@Override
	public String name() {
		return this.name;
	}

	@Override
	public Class<?> type() {
		return this.method.getReturnType();
	}

}
