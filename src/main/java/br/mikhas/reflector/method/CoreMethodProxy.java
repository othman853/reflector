package br.mikhas.reflector.method;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import br.mikhas.reflector.ReflectionUtils;
import br.mikhas.reflector.annotation.AnnotationProxy;
import br.mikhas.reflector.annotation.CoreAnnotationProxy;
import br.mikhas.reflector.config.MethodInvoker;
import br.mikhas.reflector.config.ReflectionFactory;

public class CoreMethodProxy implements MethodProxy {

	protected final Class<?> clazz;
	protected final Method method;
	// method invokation target
	protected Object target;

	protected int modifiers;
	protected boolean isStatic;

	private MethodInvoker invoker;

	private Map<Annotation, AnnotationProxy> annotationProxy;
	private final ReflectionFactory reflectionFactory;

	public CoreMethodProxy(ReflectionFactory reflectionFactory, Class<?> clazz,
			Method method, Object target) {
		this.reflectionFactory = reflectionFactory;
		assert method != null : "The method cannot be null";
		assert clazz != null : "The class cannot be null";
		assert target != null : "The target object cannot be null";

		this.clazz = clazz;
		this.method = method;
		this.modifiers = method.getModifiers();
		method.setAccessible(true);
		this.target = target;

		this.isStatic = Modifier.isStatic(this.modifiers);
	}

	public Object invoke(Object... args) {
		return this.invokeOn(this.target, args);
	}

	public String name() {
		return this.method.getName();
	}

	protected String signature() {
		return ReflectionUtils.getSignature(method);
	}

	public boolean isStatic() {
		return this.isStatic;
	}

	@Override
	public boolean isPrivate() {
		return Modifier.isPrivate(this.modifiers);
	}

	@Override
	public boolean isPublic() {
		return Modifier.isPublic(this.modifiers);
	}

	public AnnotationProxy annotation(Class<? extends Annotation> annotation) {
		Annotation annotation2 = (Annotation) this.method
				.getAnnotation(annotation);
		return this.getAnnotationProxy(annotation2);
	}

	public AnnotationProxy[] annotations() {
		Annotation[] annotations = this.method.getAnnotations();
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

	public boolean isAnnotated(Class<? extends Annotation> annotation) {
		return this.method.isAnnotationPresent(annotation);
	}

	public Class<?> type() {
		return this.method.getReturnType();
	}

	public boolean isAnnotated() {
		return this.method.getAnnotations().length > 0;
	}

	public Parameter[] parameters() {
		Class<?>[] parameterTypes = this.method.getParameterTypes();
		Annotation[][] parametersAnnotations = this.method
				.getParameterAnnotations();
		Parameter[] res = new Parameter[parameterTypes.length];

		for (int i = 0; i < parameterTypes.length; i++) {
			res[i] = new Parameter(i, parameterTypes[i],
					parametersAnnotations[i]);
		}

		return res;
	}

	@Override
	public Method method() {
		return this.method;
	}

	@Override
	public String toString() {
		return ReflectionUtils.getSignature(method);
	}

	@Override
	public Object invokeOn(Object target, Object... args) {
		try {
			if (invoker == null)
				this.invoker = reflectionFactory.getMethodInvoker(method);

			return invoker.invoke(target, args);

		} catch (IllegalArgumentException e) {
			Class<?>[] types = ReflectionUtils.getArgumentsTypes(args);
			String names = ReflectionUtils.typeNames(types);
			throw new MethodInvocationException("Illegal argument types "
					+ names, e);
		}
	}

}
