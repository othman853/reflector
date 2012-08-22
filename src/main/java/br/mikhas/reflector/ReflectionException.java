package br.mikhas.reflector;

public class ReflectionException extends RuntimeException {

	private static final long serialVersionUID = -3974638910428258345L;

	public ReflectionException() {
		super();
	}

	public ReflectionException(String string) {
		super(string);
	}

	public ReflectionException(Throwable throwable) {
		super(throwable);
	}

	public ReflectionException(String string, Throwable throwable) {
		super(string, throwable);
	}

}
