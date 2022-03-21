package org.training.spring.ioc.exception;

public class NoSetterFoundException extends BeanInstantiationException {

	public NoSetterFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoSetterFoundException(String message) {
		super(message);
	}

}
