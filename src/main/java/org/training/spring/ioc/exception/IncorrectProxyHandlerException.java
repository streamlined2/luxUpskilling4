package org.training.spring.ioc.exception;

public class IncorrectProxyHandlerException extends RuntimeException {

	public IncorrectProxyHandlerException(String message, Throwable cause) {
		super(message, cause);
	}

	public IncorrectProxyHandlerException(String message) {
		super(message);
	}

}
