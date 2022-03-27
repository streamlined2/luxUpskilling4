package org.training.spring.ioc.exception;

public class PropertyReaderException extends RuntimeException {

	public PropertyReaderException(String message, Throwable cause) {
		super(message, cause);
	}

	public PropertyReaderException(String message) {
		super(message);
	}

}
