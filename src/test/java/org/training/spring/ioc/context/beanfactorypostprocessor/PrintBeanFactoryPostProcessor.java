package org.training.spring.ioc.context.beanfactorypostprocessor;

import org.training.spring.ioc.entity.BeanDefinition;

public class PrintBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

	@Override
	public void postProcessBeanFactory(BeanDefinition beanDefinition) {
		System.out.println(beanDefinition);
	}

}
