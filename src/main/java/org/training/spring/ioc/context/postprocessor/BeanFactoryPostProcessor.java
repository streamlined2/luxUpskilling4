package org.training.spring.ioc.context.postprocessor;

import org.training.spring.ioc.entity.BeanDefinition;

public interface BeanFactoryPostProcessor {
	
	void postProcessBeanFactory(BeanDefinition beanDefinition);
	
}
