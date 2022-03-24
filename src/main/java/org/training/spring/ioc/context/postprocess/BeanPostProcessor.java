package org.training.spring.ioc.context.postprocess;

public interface BeanPostProcessor {
	
	Object postProcessBeforeInitialization(Object bean, String beanName);

}
