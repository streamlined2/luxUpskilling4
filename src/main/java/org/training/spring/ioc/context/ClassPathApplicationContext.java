package org.training.spring.ioc.context;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.training.spring.ioc.context.utility.Inspector;
import org.training.spring.ioc.entity.Bean;
import org.training.spring.ioc.entity.BeanDefinition;
import org.training.spring.ioc.exception.MultipleBeansForClassException;
import org.training.spring.ioc.exception.NoBeanInContextException;
import org.training.spring.ioc.exception.NoClassForBeanException;
import org.training.spring.ioc.exception.NoSuitableBeanException;
import org.training.spring.ioc.io.BeanDefinitionReader;
import org.training.spring.ioc.io.XMLBeanDefinitionReader;

public class ClassPathApplicationContext implements ApplicationContext {

	private Map<String, Bean> beans;
	private BeanDefinitionReader beanDefinitionReader;

	public ClassPathApplicationContext(String... path) {
		setBeanDefinitionReader(new XMLBeanDefinitionReader(path));
		start();
	}

	private void start() {
		beans = new HashMap<>();
		List<BeanDefinition> beanDefinitions = beanDefinitionReader.getBeanDefinitions();
		instantiateAndConfigureBeans(beanDefinitions);
	}

	@Override
	public <T> T getBean(Class<T> cl) {
		checkClassReference(cl);
		List<Bean> candidates = beans.values().stream().filter(bean -> cl.isAssignableFrom(bean.getValue().getClass()))
				.toList();
		checkCandidateBeans(candidates, String.format("more than 1 candidate bean for class %s", cl.getName()),
				String.format("no suitable bean found for class %s", cl.getName()));
		return (T) candidates.get(0).getValue();
	}

	private void checkClassReference(Class<?> cl) {
		if (cl == null) {
			throw new IllegalArgumentException("no class reference provided to fetch bean");
		}
	}

	private void checkCandidateBeans(List<Bean> candidates, String tooManyBeansMessage, String noSuitableBeanMessage) {
		if (candidates.size() > 1) {
			throw new MultipleBeansForClassException(tooManyBeansMessage);
		}
		if (candidates.isEmpty()) {
			throw new NoSuitableBeanException(noSuitableBeanMessage);
		}
	}

	@Override
	public <T> T getBean(String name, Class<T> cl) {
		checkClassReference(cl);
		List<Bean> candidates = beans.values().stream()
				.filter(bean -> bean.getId().equals(name) && cl.isAssignableFrom(bean.getValue().getClass())).toList();
		checkCandidateBeans(candidates,
				String.format("more than 1 candidate bean for id %s and class %s", name, cl.getName()),
				String.format("no suitable bean found for id %s and class %s", name, cl.getName()));
		return (T) candidates.get(0).getValue();
	}

	@Override
	public <T> T getBean(String name) {
		List<Bean> candidates = beans.values().stream().filter(bean -> bean.getId().equals(name)).toList();
		checkCandidateBeans(candidates, String.format("more than 1 candidate bean for id %s", name),
				String.format("no suitable bean found for id %s", name));
		return (T) candidates.get(0).getValue();
	}

	@Override
	public void setBeanDefinitionReader(BeanDefinitionReader beanDefinitionReader) {
		this.beanDefinitionReader = beanDefinitionReader;
	}

	private void instantiateAndConfigureBeans(List<BeanDefinition> beanDefinitions) {
		Set<BeanDefinition> sortedByReferenceCountDefinitions = new TreeSet<>(BeanDefinition.REFERENCE_COUNTER_COMPARATOR);
		sortedByReferenceCountDefinitions.addAll(beanDefinitions);
		for (var beanDefinition : sortedByReferenceCountDefinitions) {
			String id = beanDefinition.getId();
			try {
				Class<?> cl = Class.forName(beanDefinition.getClassName());
				Object obj = Inspector.spawnObject(cl);
				setPrimitiveValues(obj, beanDefinition);
				setReferences(obj, beanDefinition);
				beans.put(id, new Bean(id, obj));
			} catch (ClassNotFoundException e) {
				throw new NoClassForBeanException(
						String.format("can't find class %s for bean", beanDefinition.getClassName()), e);
			}
		}
	}

	private void setPrimitiveValues(Object bean, BeanDefinition beanDefinition) {
		for (var dependency : beanDefinition.getDependencies().entrySet()) {
			String property = dependency.getKey();
			String value = dependency.getValue();
			Inspector.setPropertyValue(bean, property, value);
		}
	}

	private void setReferences(Object bean, BeanDefinition beanDefinition) {
		for (var dependency : beanDefinition.getRefDependencies().entrySet()) {
			String property = dependency.getKey();
			String referencedBeanName = dependency.getValue();
			Object referencedBean = getBean(referencedBeanName);
			if(referencedBean == null) {
				throw new NoBeanInContextException(String.format("can't find bean %s in context", referencedBeanName));
			}
			Inspector.setPropertyReference(bean, property, referencedBean);
		}
	}

}
