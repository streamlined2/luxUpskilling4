package org.training.spring.ioc.context;

import org.training.spring.ioc.io.BeanDefinitionReader;

public interface ApplicationContext {

	<T> T getBean(Class<T> cl);

	<T> T getBean(String name, Class<T> cl);

	<T> T getBean(String name);

	void setBeanDefinitionReader(BeanDefinitionReader beanDefinitionReader);

}
