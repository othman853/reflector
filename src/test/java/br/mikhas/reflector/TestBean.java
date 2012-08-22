package br.mikhas.reflector;

import javax.annotation.Resource;

import org.junit.Ignore;

@Ignore
@Resource
public class TestBean {

	public TestBean() {

	}

	public String constructorParameter;

	public TestBean(String string) {
		constructorParameter = string;
	}

	public String publicAttribute = "publicAttribute";

	private String privateAttribute = "privateAttribute";

	public final String publicFinalAttribute = "publicFinalAttribute";

	public static final String publicFinalStaticAttribute = "publicFinalStaticAttribute";

	public static String pStaticAttribute = "pStaticAttribute";

	public String publicMethod() {
		return "publicMethod";
	}

	public static String publicStaticMethod() {
		return "publicStaticMethod";
	}

	public String publicMethod(Object parameter) {
		return "publicMethod: " + parameter;
	}

	public static String publicStaticMethod(Object parameter) {
		return "publicStaticMethod: " + parameter;
	}

	public String getPrivateAttribute() {
		return this.privateAttribute;
	}

	public long performanceTest = 0;

	public void performanceTest() {
		performanceTest++;
	}

	public void testMethod1() {

	}

	public void testMethod2(int a, Object b) {

	}

	public void testMethod3(Integer a, Object b) {

	}
}
