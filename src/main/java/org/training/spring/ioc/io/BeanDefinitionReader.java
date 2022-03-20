package org.training.spring.ioc.io;

import java.util.List;

import org.training.spring.ioc.entity.BeanDefinition;

public interface BeanDefinitionReader {

    List<BeanDefinition> getBeanDefinitions();

}
