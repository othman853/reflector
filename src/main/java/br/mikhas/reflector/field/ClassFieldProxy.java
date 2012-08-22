package br.mikhas.reflector.field;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import br.mikhas.reflector.annotation.AnnotationProxy;
import br.mikhas.reflector.annotation.CoreAnnotationProxy;
import br.mikhas.reflector.config.FieldAccessor;
import br.mikhas.reflector.config.ReflectionFactory;

public class ClassFieldProxy implements FieldProxy {

	protected final Field field;
	protected final Class<?> clazz;
	protected final int modifiers;
	protected final FieldAccessor fieldAccessor;
	protected final boolean isStatic;

	private Map<Annotation, AnnotationProxy> annotationProxy;

	public ClassFieldProxy(ReflectionFactory reflectionFactory, Class<?> clazz,
			Field field) {
		this.field = field;
		this.clazz = clazz;
		this.modifiers = field.getModifiers();
		this.fieldAccessor = reflectionFactory.getFieldAcessor(field);

		this.isStatic = this.isStatic();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T get() {
		if (this.isStatic) {
			return (T) this.fieldAccessor.get(this.clazz);
		} else {
			throw new FieldReflectionException(
					"Non-static fields cannot be accessed without an instance: "
							+ this.name());
		}
	}

	@Override
	public void set(Object value) {
		if (this.isStatic) {
			this.fieldAccessor.set(this.clazz, value);
		} else {
			throw new FieldReflectionException(
					"Non-static fields cannot be accessed without an instance: "
							+ this.name());
		}
	}

	public void set(Object target, Object value) {
		this.fieldAccessor.set(target, value);
	}

	@SuppressWarnings("unchecked")
	public <T> T get(Object target) {
		return (T) this.fieldAccessor.get(target);
	}

	public boolean isAnnotated(Class<? extends Annotation> annotation) {
		return this.field.getAnnotation(annotation) != null;
	}

	public AnnotationProxy annotation(Class<? extends Annotation> annotation) {
		Annotation annotation2 = (Annotation) this.field
				.getAnnotation(annotation);
		if (annotation2 != null)
			return this.getAnnotationProxy(annotation2);
		else
			return null;
	}

	public AnnotationProxy[] annotations() {
		Annotation[] annotations = this.field.getAnnotations();
		AnnotationProxy[] proxies = new AnnotationProxy[annotations.length];

		for (int i = 0; i < annotations.length; i++) {
			proxies[i] = this.getAnnotationProxy(annotations[i]);
		}

		return proxies;
	}

	private AnnotationProxy getAnnotationProxy(Annotation annotation) {
		if (this.annotationProxy == null) {
			this.annotationProxy = new HashMap<Annotation, AnnotationProxy>();
		}
		AnnotationProxy proxy = this.annotationProxy.get(annotation);
		if (proxy == null) {
			proxy = new CoreAnnotationProxy(annotation);
			this.annotationProxy.put(annotation, proxy);
		}
		return proxy;
	}

	public String name() {
		return this.field.getName();
	}

	public Class<?> type() {
		return this.field.getType();
	}

	public boolean isAnnotated() {
		return this.field.getAnnotations().length > 0;
	}

	@Override
	public boolean isPrivate() {
		return Modifier.isPrivate(modifiers);
	}

	@Override
	public boolean isPublic() {
		return Modifier.isPublic(modifiers);
	}

	@Override
	public boolean isStatic() {
		return Modifier.isStatic(modifiers);
	}

	@Override
	public boolean isFinal() {
		return Modifier.isFinal(modifiers);
	}

	@Override
	public boolean isTransient() {
		return Modifier.isTransient(modifiers);
	}

	@Override
	public boolean isVolatile() {
		return Modifier.isVolatile(modifiers);
	}

	@Override
	public Field field() {
		return this.field;
	}
}
