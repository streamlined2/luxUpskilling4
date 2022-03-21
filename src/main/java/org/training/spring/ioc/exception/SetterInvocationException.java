package org.training.spring.ioc.exception;

public class SetterInvocationException extends BeanInstantiationException {

	public SetterInvocationException(String message, Throwable cause) {
		super(message, cause);
	}

	public SetterInvocationException(String message) {
		super(message);
	}

}
