package org.training.spring.ioc.annotation;

import java.lang.annotation.Target;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;

@Retention(RUNTIME)
@Target({ FIELD })
public @interface RandomValue {

	double min() default 0;

	double max() default 100;

	int length() default 10;

}
