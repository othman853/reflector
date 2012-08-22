package br.mikhas.reflector.config;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import br.mikhas.reflector.ReflectionUtils;

/**
 * Factory which creates Native mathod invokers which can invoke methods without
 * using reflection API.
 * <p>
 * It's usage guarantees faster method access than default reflection API
 * 
 * @author Mikhail Domanoski
 * 
 */
public final class NativeMethodInvokerFactory {

	/**
	 * The used class pool
	 */
	private static ClassPool pool;

	/**
	 * {@link Object} CtClass
	 */
	private static CtClass objectCt;
	/**
	 * {@link Object}[] CtClass
	 */
	private static CtClass objectArrCt;

	/**
	 * {@link MethodInvoker} CtClass
	 */
	private static CtClass invokerCt;

	/**
	 * Unique {@link NativeMethodInvokerFactory} instance
	 */
	private static NativeMethodInvokerFactory instance;

	/**
	 * Initialized common used CtClass'es
	 */
	private static void initialize() {
		try {
			pool = ClassPool.getDefault();
			objectCt = pool.get("java.lang.Object");
			objectArrCt = pool.get("java.lang.Object[]");
			invokerCt = pool.get(MethodInvoker.class.getName());
		} catch (Exception e) {
			throw new RuntimeException(
					"Could not initialize the MethodInvokerFactory", e);
		}
	}

	/**
	 * Gets the default instance of the method factory
	 * 
	 * @return the default instance of the {@link NativeMethodInvokerFactory}
	 */
	public static NativeMethodInvokerFactory getDefault() {

		if (!isAvailable())
			return null;

		if (instance == null) {
			initialize();
			instance = new NativeMethodInvokerFactory();
		}

		return instance;
	}

	/**
	 * Checks if the {@link NativeMethodInvokerFactory} is available on the
	 * environment.
	 * <p>
	 * To make it available, add javassist to the classpath
	 */
	public static boolean isAvailable() {
		try {
			Class.forName("javassist.ClassPool");
			return true;
		} catch (Throwable t) {
			return false;
		}
	}

	/**
	 * Creates a new instance of the {@link NativeMethodInvokerFactory}
	 */
	private NativeMethodInvokerFactory() {

	}

	/**
	 * Gets or creates a new {@link MethodInvoker} for the providen method
	 * 
	 * @param method
	 *            The method which a {@link MethodInvoker} will be created
	 * @return A {@link MethodInvoker} which can invoke the given method
	 */
	public MethodInvoker getFor(Method method) {
		int modifiers = method.getModifiers();

		if (!Modifier.isPublic(modifiers) || Modifier.isAbstract(modifiers)) {
			throw new RuntimeException(
					"Invokers may be created on for non-abstract public methods.");
		}

		return this.createInvokerFor(method);
	}

	/**
	 * Creates a new {@link MethodInvoker} foe the given method
	 * 
	 * @param method
	 *            The method
	 * @return The {@link MethodInvoker} for <code>method</code>
	 */
	private MethodInvoker createInvokerFor(Method method) {
		Class<? extends MethodInvoker> invokerClass = getInvokerClass(method);

		try {
			return invokerClass.newInstance();
		} catch (Throwable e) {
			throw new NativeInvokerCreationException(
					"Error while instantiating invoker for: "
							+ ReflectionUtils.getSignature(method), e);
		}
	}

	@SuppressWarnings("unchecked")
	private Class<? extends MethodInvoker> getInvokerClass(Method method) {
		Class<?> declaringClass = method.getDeclaringClass();

		String invokerClassName = declaringClass.getSimpleName() + '$'
				+ Math.abs(method.hashCode()) + "$Invoker";
		Class<? extends MethodInvoker> invokerClass;

		try {
			invokerClass = (Class<? extends MethodInvoker>) Class
					.forName(invokerClassName);
			return invokerClass;
		} catch (ClassNotFoundException e) {
			invokerClass = createInvokerClass(method, invokerClassName);
			return invokerClass;
		}
	}

	@SuppressWarnings("unchecked")
	private Class<? extends MethodInvoker> createInvokerClass(Method method,
			String name) {
		CtClass invokerClass = pool.makeClass(name);

		invokerClass.setModifiers(javassist.Modifier.PUBLIC);
		invokerClass.addInterface(invokerCt);

		try {
			addInvokeMethod(invokerClass, method);
			Class<? extends MethodInvoker> invokerCls = invokerClass.toClass();

			/**
			 * Executes extra JIT compilation
			 */
			java.lang.Compiler.compileClass(invokerCls);

			return invokerCls;

		} catch (CannotCompileException e) {
			throw new NativeInvokerCreationException(
					"Error while creating invoker for: "
							+ ReflectionUtils.getSignature(method), e);
		}
	}

	/**
	 * Adds the <code>invoke</code> method to the {@link MethodInvoker} class
	 * 
	 * @param invokerClass
	 *            The new {@link MethodInvoker} class
	 * @param method
	 *            The method which the invoker is being created
	 * @throws CannotCompileException
	 *             If the new <code>invoke</code> method could not be created
	 * @throws NotFoundException
	 */
	private void addInvokeMethod(CtClass invokerClass, Method method)
			throws CannotCompileException {
		Class<?> declaringClass = method.getDeclaringClass();
		boolean isStatic = Modifier.isStatic(method.getModifiers());
		String declaringName = declaringClass.getName();
		String methodName = method.getName();
		Class<?>[] parameters = method.getParameterTypes();
		Class<?> returnType = method.getReturnType();
		boolean isVoid = returnType == Void.class || returnType == void.class;

		CtMethod invoke = new CtMethod(objectCt, "invoke", new CtClass[] {
				objectCt, objectArrCt }, invokerClass);

		invoke.setModifiers(javassist.Modifier.PUBLIC);

		StringBuilder builder = new StringBuilder();

		builder.append('{');
		// Sets the target

		builder.append("try {");

		if (!isStatic) {
			builder.append(declaringName).append(" target = (").append(
					declaringName).append(") $1;");
		}

		if (!isVoid) {
			builder.append(returnType.getName());
			builder.append(" ret = ");
		}

		if (isStatic)
			builder.append(declaringName).append('.');
		else
			builder.append("target.");

		builder.append(methodName).append('(');

		for (int i = 0; i < parameters.length; i++) {
			if (i > 0)
				builder.append(',');
			// Cast first

			if (parameters[i].isPrimitive()) {
				getPrimitiveConverter(builder, parameters[i], i);
			} else {
				builder.append('(').append(parameters[i].getName()).append(')');
				builder.append("$2[").append(i).append(']');
			}
		}

		builder.append(");return ");

		if (isVoid) {
			builder.append("null;");
		} else {
			if (returnType.isPrimitive())
				wrapPrimitiveReturn(builder, returnType);
			else
				builder.append("ret;");
		}

		builder
				.append("}catch(java.lang.Throwable t){throw new RuntimeException(t);}");

		builder.append('}');
		invoke.setBody(builder.toString());
		invokerClass.addMethod(invoke);
	}

	private void wrapPrimitiveReturn(StringBuilder builder, Class<?> type) {
		builder.append(ReflectionUtils.getWrapper(type).getName());
		builder.append(".valueOf(ret);");
	}

	private void getPrimitiveConverter(StringBuilder builder, Class<?> type,
			int index) {
		builder.append("((");
		builder.append(ReflectionUtils.getWrapper(type).getName());
		builder.append(")$2[");
		builder.append(index);
		builder.append("]).");
		builder.append(type.getName());
		builder.append("Value()");
	}
}
