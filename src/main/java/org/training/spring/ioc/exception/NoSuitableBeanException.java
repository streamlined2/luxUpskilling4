package org.training.spring.ioc.exception;

public class NoSuitableBeanException extends BeanInstantiationException {

	public NoSuitableBeanException(String message) {
		super(message);
	}

	public NoSuitableBeanException(String message, Throwable cause) {
		super(message, cause);
	}

}
