package br.mikhas.reflector.method;

import br.mikhas.reflector.ReflectionException;

public class MethodInvocationException extends ReflectionException {

	private static final long serialVersionUID = 7011277503023643L;

	public MethodInvocationException() {
		super();
	}

	public MethodInvocationException(String message) {
		super(message);
	}

	public MethodInvocationException(Throwable cause) {
		super(cause);
	}

	public MethodInvocationException(String message, Throwable cause) {
		super(message, cause);
	}
}
