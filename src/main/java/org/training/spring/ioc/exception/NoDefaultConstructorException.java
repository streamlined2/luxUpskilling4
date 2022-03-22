package org.training.spring.ioc.exception;

public class NoDefaultConstructorException extends BeanInstantiationException {

	public NoDefaultConstructorException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoDefaultConstructorException(String message) {
		super(message);
	}

}
