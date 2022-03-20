package org.training.spring.ioc.exception;

public class MultipleBeansForClassException extends RuntimeException {

	public MultipleBeansForClassException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public MultipleBeansForClassException(String message) {
		super(message);
	}

}
