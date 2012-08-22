package br.mikhas.reflector.field;

import br.mikhas.reflector.ReflectionException;

public class FieldReflectionException extends ReflectionException {

	private static final long serialVersionUID = 6647484564820214011L;

	public FieldReflectionException() {
		super();
	}

	public FieldReflectionException(String message) {
		super(message);
	}

	public FieldReflectionException(Throwable cause) {
		super(cause);
	}

	public FieldReflectionException(String message, Throwable cause) {
		super(message, cause);
	}
}
