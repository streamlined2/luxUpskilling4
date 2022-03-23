package org.training.spring.ioc.io;

import java.util.Set;

import org.training.spring.ioc.entity.BeanDefinition;

public interface BeanDefinitionReader {

    Set<BeanDefinition> getBeanDefinitions();

}
