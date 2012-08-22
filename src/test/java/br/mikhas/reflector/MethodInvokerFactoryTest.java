package br.mikhas.reflector;

import java.lang.reflect.Method;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.mikhas.reflector.config.MethodInvoker;
import br.mikhas.reflector.config.NativeMethodInvokerFactory;

public class MethodInvokerFactoryTest {
	private Method method;

	@Before
	public void setup() {
		method = Reflect.on(TestBean.class).method("performanceTest")
				.withoutArgs().method();
	}

	@Test
	public void testFactory() {
		MethodInvoker invoker1 = NativeMethodInvokerFactory.getDefault().getFor(
				method);
		MethodInvoker invoker2 = NativeMethodInvokerFactory.getDefault().getFor(
				method);

		Assert.assertEquals(invoker1.getClass(), invoker2.getClass());
	}

	@Test
	public void testStaticMethodInvoker() {
		Method method = Reflect.on(TestBean.class).method("publicStaticMethod",
				Object.class).method();
		MethodInvoker invoker = NativeMethodInvokerFactory.getDefault()
				.getFor(method);

		Object invoke = invoker.invoke(null, "arg");
		System.out.println(invoker);

		Assert.assertEquals("publicStaticMethod: arg", invoke);
	}
}
