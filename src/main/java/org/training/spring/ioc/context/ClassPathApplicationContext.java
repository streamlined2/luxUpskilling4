package org.training.spring.ioc.context;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.training.spring.ioc.entity.Bean;
import org.training.spring.ioc.entity.BeanDefinition;
import org.training.spring.ioc.io.BeanDefinitionReader;
import org.training.spring.ioc.io.XMLBeanDefinitionReader;

public class ClassPathApplicationContext implements ApplicationContext {

	private static final String SETTER_PREFIX = "set";

    private Map<String, Bean> beans;
    private BeanDefinitionReader beanDefinitionReader;

    public ClassPathApplicationContext() {

    }

    public ClassPathApplicationContext(String... path) {
        setBeanDefinitionReader(new XMLBeanDefinitionReader(path));
        start();
    }

    public void start() {
        beans = new HashMap<>();
        List<BeanDefinition> beanDefinitions = beanDefinitionReader.getBeanDefinitions();
        instantiateBeans(beanDefinitions);
        injectValueDependencies(beanDefinitions);
        injectRefDependencies(beanDefinitions);
    }

    @Override
    public <T> T getBean(Class<T> clazz) {
        return null;
    }

    @Override
    public <T> T getBean(String name, Class<T> clazz) {
        return null;
    }

    @Override
    public <T> T getBean(String name) {
        return null;
    }

    @Override
    public void setBeanDefinitionReader(BeanDefinitionReader beanDefinitionReader) {
        this.beanDefinitionReader = beanDefinitionReader;
    }

    private void instantiateBeans(List<BeanDefinition> beanDefinitions) {
    }


    private void injectValueDependencies(List<BeanDefinition> beanDefinitions) {

    }

    private void injectRefDependencies(List<BeanDefinition> beanDefinitions) {

    }

    private String getSetterName(String propertyName) {
        return SETTER_PREFIX + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
    }
}
