package br.mikhas.reflector.field;

import java.lang.reflect.Field;

import br.mikhas.reflector.config.ReflectionFactory;

public class ObjectFieldProxy extends ClassFieldProxy implements FieldProxy {

	protected final Object target;

	public ObjectFieldProxy(ReflectionFactory reflectionFactory,
			Class<?> clazz, Field field, Object object) {
		super(reflectionFactory, clazz, field);
		this.target = this.isStatic ? this.clazz : object;
	}

	public void set(Object value) {
		this.fieldAccessor.set(target, value);
	}

	@SuppressWarnings("unchecked")
	public <T> T get() {
		return (T) this.fieldAccessor.get(target);
	}
}
