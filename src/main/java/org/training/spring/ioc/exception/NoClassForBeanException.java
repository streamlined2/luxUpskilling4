package org.training.spring.ioc.exception;

public class NoClassForBeanException extends BeanInstantiationException {

	public NoClassForBeanException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoClassForBeanException(String message) {
		super(message);
	}

}
