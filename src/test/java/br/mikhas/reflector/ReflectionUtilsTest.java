package br.mikhas.reflector;

import static br.mikhas.reflector.ReflectionUtils.getArgumentsTypes;

import java.lang.reflect.Method;

import org.junit.Assert;
import org.junit.Test;

public class ReflectionUtilsTest {

	@Test
	public void testMethodLookup() {
		Method method1 = ReflectionUtils.lookupMethod(TestBean.class,
				"testMethod1", null);

		Assert.assertEquals(0, method1.getParameterTypes().length);

		Method method2 = ReflectionUtils.lookupMethod(TestBean.class,
				new Class<?>[] { int.class, Object.class });

		Assert.assertEquals("testMethod2", method2.getName());

		Method method3 = ReflectionUtils.lookupMethod(TestBean.class,
				new Class<?>[] { Integer.class, Object.class });

		Assert.assertEquals("testMethod3", method3.getName());

		method3 = ReflectionUtils.lookupMethod(TestBean.class, new Class<?>[] {
				null, null });

		Assert.assertEquals("testMethod3", method3.getName());
	}

	@Test
	public void testGetTypes() {
		Class<?>[] types = getArgumentsTypes("asd");

		Assert.assertArrayEquals(array(String.class), types);

		types = getArgumentsTypes(1, 1L, 0.1, 0.1f);

		Assert.assertArrayEquals(array(Integer.class, Long.class, Double.class,
				Float.class), types);

	}

	public static Object[] array(Object... args) {
		return args;
	}

	public static void main(String[] args) {
		System.out.println(Integer.class.isAssignableFrom(int.class));
		System.out.println(int.class.isAssignableFrom(Integer.class));
	}
}
