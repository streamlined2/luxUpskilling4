package org.training.spring.ioc.context.beanfactorypostprocessor;

import org.training.spring.ioc.entity.BeanDefinition;

public interface BeanFactoryPostProcessor {
	
	void postProcessBeanFactory(BeanDefinition beanDefinition);
	
}
