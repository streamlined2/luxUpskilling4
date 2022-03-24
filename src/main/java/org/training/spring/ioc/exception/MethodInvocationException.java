package org.training.spring.ioc.exception;

public class MethodInvocationException extends RuntimeException {

	public MethodInvocationException(String message, Throwable cause) {
		super(message, cause);
	}

	public MethodInvocationException(String message) {
		super(message);
	}

}
