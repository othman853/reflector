/**
 * 
 */
package br.mikhas.reflector;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.lang.annotation.Annotation;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.mikhas.reflector.field.FieldProxy;
import br.mikhas.reflector.method.Parameter;

/**
 * @author n068815
 * 
 */
public class CoreClassProxyTest {

	private ClassProxy<TestBean> classProxy;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setup() throws Exception {
		classProxy = Reflect.on(TestBean.class);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link br.mikhas.reflector.CoreClassProxy#newInstance()}.
	 */
	@Test
	public void testNewInstance() {
		classProxy.newInstance();
	}

	/**
	 * Test method for {@link
	 * br.mikhas.refractor.CoreClassProxy#constructor(java.lang.Class<?>[])}.
	 */
	@Test
	public void testConstructor() {
		classProxy.constructor();
		classProxy.constructor(String.class);
		TestBean instance = classProxy.constructor(String.class).invoke("TEST");

		assertEquals("TEST", instance.constructorParameter);
	}

	/**
	 * Test method for {@link br.mikhas.reflector.CoreClassProxy#name()}.
	 */
	@Test
	public void testName() {
		assertEquals("TestBean", classProxy.name());
	}

	/**
	 * Test method for {@link br.mikhas.reflector.CoreClassProxy#fullName()}.
	 */
	@Test
	public void testFullName() {
		assertEquals("br.mikhas.reflector.TestBean", classProxy.fullName());
	}

	/**
	 * Test method for
	 * {@link br.mikhas.reflector.CoreClassProxy#field(java.lang.String)}.
	 */
	@Test
	public void testField() {
		String publicFinalStaticAttribute = classProxy.field(
				"publicFinalStaticAttribute").get();

		FieldProxy field = Reflect.on(classProxy.newInstance()).field(
				"privateAttribute");

		String privateAttribute = field.get();

		String pStaticAttribute = classProxy.field("pStaticAttribute").get();

		assertEquals("publicFinalStaticAttribute", publicFinalStaticAttribute);
		assertEquals("pStaticAttribute", pStaticAttribute);
		assertEquals("privateAttribute", privateAttribute);
	}

	/**
	 * Test method for {@link br.mikhas.reflector.CoreClassProxy#fields()}.
	 */
	@Test
	public void testFields() {
		assertTrue(classProxy.fields().length > 0);
	}

	/**
	 * Test method for
	 * {@link br.mikhas.reflector.CoreClassProxy#method(java.lang.String)}.
	 */
	@Test
	public void testMethodString() {
		classProxy.method("publicMethod");
		classProxy.method("publicStaticMethod");
	}

	/**
	 * Test method for {@link
	 * br.mikhas.refractor.CoreClassProxy#method(java.lang.String,
	 * java.lang.Class<?>[])}.
	 */
	@Test
	public void testMethodStringClassOfQArray() {
		classProxy.method("publicMethod", String.class);
		classProxy.method("publicStaticMethod", String.class);
	}

	/**
	 * Test method for
	 * {@link br.mikhas.reflector.CoreClassProxy#method(java.lang.String, br.mikhas.reflector.method.Parameter[])}
	 * .
	 */
	@Test
	public void testMethodStringParameterArray() {
		Parameter parameter = new Parameter(0, Object.class, new Annotation[0]);

		assertNotNull(classProxy.method("publicStaticMethod", parameter));

	}

	/**
	 * Test method for {@link br.mikhas.reflector.CoreClassProxy#methods()}.
	 */
	@Test
	public void testMethods() {
		assertTrue(classProxy.methods().length > 0);
	}

	/**
	 * Test method for
	 * {@link br.mikhas.reflector.CoreClassProxy#annotation(java.lang.Class)}.
	 */
	@Test
	public void testAnnotation() {
		classProxy.annotation(Resource.class);
	}

	/**
	 * Test method for {@link br.mikhas.reflector.CoreClassProxy#annotations()}.
	 */
	@Test
	public void testAnnotations() {
		assertTrue(classProxy.annotations().length > 0);
	}

	/**
	 * Test method for
	 * {@link br.mikhas.reflector.CoreClassProxy#isAnnotated(java.lang.Class)}.
	 */
	@Test
	public void testIsAnnotatedClassOfQextendsAnnotation() {
		assertTrue(classProxy.isAnnotated(Resource.class));
	}

	/**
	 * Test method for {@link br.mikhas.reflector.CoreClassProxy#isAnnotated()}.
	 */
	@Test
	public void testIsAnnotated() {
		assertTrue(classProxy.isAnnotated());
	}

	/**
	 * Test method for {@link br.mikhas.reflector.CoreClassProxy#constructors()}
	 * .
	 */
	@Test
	public void testConstructors() {
		assertTrue(classProxy.constructors().length > 0);
	}

}
