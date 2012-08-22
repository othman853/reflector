package br.mikhas.reflector;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import net.vidageek.mirror.dsl.AccessorsController;
import net.vidageek.mirror.dsl.Mirror;
import net.vidageek.mirror.get.dsl.GetterHandler;
import net.vidageek.mirror.invoke.dsl.MethodHandler;
import net.vidageek.mirror.provider.experimental.sun15.Sun15ReflectionProvider;
import net.vidageek.mirror.set.dsl.SetterHandler;

import org.junit.Before;
import org.junit.Test;

import br.mikhas.reflector.config.JavassistReflectionFactory;
import br.mikhas.reflector.config.MethodInvoker;
import br.mikhas.reflector.config.NativeMethodInvokerFactory;
import br.mikhas.reflector.field.FieldProxy;
import br.mikhas.reflector.method.MethodProxy;

public class ReflectorPerformanceTest {

	public static final int loops = 10000;

	public static final int times = 100;

	private TestBean testBeanInstance;

	private Field field;

	private Method method;

	private FieldProxy fieldProxy;

	private MethodInvoker invoker;

	private MethodProxy methodProxy;

	private GetterHandler getterHandler;

	private SetterHandler setterHandler;

	private MethodHandler methodHandler;

	@Before
	public void setup() {
		ClassProxy<TestBean> beanClass = Reflect.on(TestBean.class);
		testBeanInstance = beanClass.newInstance();

		field = beanClass.field("performanceTest").field();
		Reflect.setDefaultReflector(new Reflector(
				new JavassistReflectionFactory()));
		fieldProxy = Reflect.on(testBeanInstance).field("performanceTest");

		method = beanClass.method("performanceTest", (Class<?>[]) null)
				.method();
		invoker = NativeMethodInvokerFactory.getDefault().getFor(method);
		methodProxy = Reflect.on(testBeanInstance).method("performanceTest")
				.withoutArgs();

		AccessorsController on = new Mirror(new Sun15ReflectionProvider())
				.on(testBeanInstance);
		getterHandler = on.get();
		setterHandler = on.set();
		methodHandler = on.invoke().method("performanceTest");

	}

	public void reflectionMethod() throws Exception {
		for (int i = 0; i < loops; i++) {
			method.invoke(testBeanInstance);
		}
	}

	public void invokerMethod() throws Exception {
		for (int i = 0; i < loops; i++) {
			invoker.invoke(testBeanInstance);
		}
	}

	public void reflectorMethod() throws Exception {
		for (int i = 0; i < loops; i++) {
			methodProxy.invoke();
		}
	}

	public void reflectionField() throws Exception {
		for (int i = 0; i < loops; i++) {
			long val = (Long) field.get(testBeanInstance);
			field.set(testBeanInstance, val + 1);
		}
	}

	public void reflectorField() throws Exception {
		for (int i = 0; i < loops; i++) {
			long val = (Long) fieldProxy.get();
			fieldProxy.set(val + 1);
		}
	}

	public void mirrorField() throws Exception {
		for (int i = 0; i < loops; i++) {
			long val = (Long) getterHandler.field(field);
			setterHandler.field(field).withValue(val + 1);
		}
	}

	public void mirrorMethod() throws Exception {
		for (int i = 0; i < loops; i++) {
			methodHandler.withoutArgs();
		}
	}

	public static long time = 0;

	public static void startTick() {
		time = System.nanoTime();
	}

	public static long tick() {
		return System.nanoTime() - time;
	}

	@Test
	public void test() {
		try {
			for (int i = 0; i < times; i++) {
				startTick();
				reflectionMethod();
				reflectionMethod += tick();

				startTick();
				reflectorField();
				reflectorField += tick();

				startTick();
				reflectorMethod();
				reflectorMethod += tick();

				startTick();
				invokerMethod();
				methodInvoker += tick();

				startTick();
				reflectionField();
				reflectionField += tick();

				startTick();
				mirrorField();
				mirrorField += tick();

				startTick();
				mirrorMethod();
				mirrorMethod += tick();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		System.out.printf("Reflection method\t%d\n", reflectionMethod);
		System.out.printf("Reflector method\t%d\n", reflectorMethod);
		System.out.printf("Method invoker\t%d\n", methodInvoker);
		System.out.printf("Mirror method handler\t%d\n", mirrorMethod);

		System.out.printf("Reflection field\t%d\n", reflectionField);
		System.out.printf("Reflector field\t%d\n", reflectorField);
		System.out.printf("Field accessor\t%d\n", fieldAccessor);
		System.out.printf("Mirror field handler\t%d\n", mirrorField);
	}

	private long reflectionField = 0;
	private long reflectionMethod = 0;

	private long reflectorField = 0;
	private long reflectorMethod = 0;

	private long fieldAccessor = 0;
	private long methodInvoker = 0;

	private long mirrorMethod = 0;

	private long mirrorField = 0;
}
