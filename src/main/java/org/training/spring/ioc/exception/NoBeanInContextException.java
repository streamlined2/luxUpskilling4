package org.training.spring.ioc.exception;

public class NoBeanInContextException extends RuntimeException {

	public NoBeanInContextException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoBeanInContextException(String message) {
		super(message);
	}

}
