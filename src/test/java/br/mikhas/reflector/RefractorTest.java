/**
 * 
 */
package br.mikhas.reflector;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author n068815
 * 
 */
public class RefractorTest {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for
	 * {@link br.mikhas.reflector.Reflect#onInstance(java.lang.Class)}.
	 */
	@Test
	public void testOnInstanceClassOfT() {
		Reflect.onInstance(TestBean.class);
	}

	/**
	 * Test method for
	 * {@link br.mikhas.reflector.Reflect#onInstance(java.lang.String)}.
	 */
	@Test
	public void testOnInstanceString() {
		Reflect.onInstance("br.mikhas.reflector.TestBean");
	}

	/**
	 * Test method for
	 * {@link br.mikhas.reflector.Reflect#reflect(java.lang.Object)}.
	 */
	@Test
	public void testReflectT() {
		Reflect.on(new TestBean());
	}

	/**
	 * Test method for
	 * {@link br.mikhas.reflector.Reflect#reflect(java.lang.Class)}.
	 */
	@Test
	public void testReflectClassOfT() {
		Reflect.on(TestBean.class);
	}

	/**
	 * Test method for
	 * {@link br.mikhas.reflector.Reflect#reflect(java.lang.String)}.
	 */
	@Test
	public void testReflectString() {
		Reflect.on("br.mikhas.reflector.TestBean");
	}

	@Test
	@Ignore
	public void testInvokeOn() {

		Object result = (String) Reflect.on("br.mikhas.reflector.TestBean")
				.method("publicMethod").withoutArgs().invokeOn(new TestBean());

		Assert.assertEquals("publicMethod", result);

	}
}
