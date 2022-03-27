package org.training.spring.ioc.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(METHOD)
public @interface Transactional {

	String startMessage() default "transaction started";
	String finishMessage() default "transaction finished";
	
}
