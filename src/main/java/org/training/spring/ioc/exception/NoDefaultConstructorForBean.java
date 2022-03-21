package org.training.spring.ioc.exception;

public class NoDefaultConstructorForBean extends BeanInstantiationException {

	public NoDefaultConstructorForBean(String message, Throwable cause) {
		super(message, cause);
	}

	public NoDefaultConstructorForBean(String message) {
		super(message);
	}

}
