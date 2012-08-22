package br.mikhas.reflector.config;

import br.mikhas.reflector.ReflectionException;

public class NativeInvokerCreationException extends ReflectionException {

	private static final long serialVersionUID = 6181514050159181264L;

	public NativeInvokerCreationException() {
		super();
	}

	public NativeInvokerCreationException(String string) {
		super(string);
	}

	public NativeInvokerCreationException(Throwable throwable) {
		super(throwable);
	}

	public NativeInvokerCreationException(String string, Throwable throwable) {
		super(string, throwable);
	}
}
