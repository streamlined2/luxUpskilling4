package org.training.spring.ioc.context;

import static org.training.spring.ioc.context.utility.Inspector.createObject;
import static org.training.spring.ioc.context.utility.Inspector.setPropertyReference;
import static org.training.spring.ioc.context.utility.Inspector.setPropertyValue;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

import org.training.spring.ioc.context.postprocessor.BeanFactoryPostProcessor;
import org.training.spring.ioc.context.postprocessor.BeanPostProcessor;
import org.training.spring.ioc.entity.Bean;
import org.training.spring.ioc.entity.BeanDefinition;
import org.training.spring.ioc.exception.MultipleBeansForClassException;
import org.training.spring.ioc.exception.NoBeanInContextException;
import org.training.spring.ioc.exception.NoClassForBeanException;
import org.training.spring.ioc.io.BeanDefinitionReader;
import org.training.spring.ioc.io.XMLBeanDefinitionReader;

public class ClassPathApplicationContext implements ApplicationContext {

	private static final Predicate<Class<?>> BEAN_FACTORY_POST_PROCESSOR_PREDICATE = BeanFactoryPostProcessor.class::isAssignableFrom;
	private static final Predicate<BeanDefinition> BEAN_DEFINITION_POST_PROCESSOR_PREDICATE = beanDefinition -> BEAN_FACTORY_POST_PROCESSOR_PREDICATE
			.test(beanDefinition.getClassReference());
	private static final Predicate<Object> BEAN_POST_PROCESSOR_PREDICATE = obj -> BeanPostProcessor.class
			.isAssignableFrom(obj.getClass());

	private Map<String, Bean> beans;
	private BeanDefinitionReader beanDefinitionReader;

	public ClassPathApplicationContext(String... path) {
		setBeanDefinitionReader(new XMLBeanDefinitionReader(path));
		start();
	}

	private void start() {
		beans = new HashMap<>();
		Set<BeanDefinition> beanDefinitions = beanDefinitionReader.getBeanDefinitions();
		instantiateAndConfigureBeans(beanDefinitions, BEAN_FACTORY_POST_PROCESSOR_PREDICATE);
		postProcessBeanFactories(beanDefinitions);
		instantiateAndConfigureBeans(beanDefinitions, BEAN_FACTORY_POST_PROCESSOR_PREDICATE.negate());
		postProcessBeansBeforeInitialization();
		initializeBeans();
		postProcessBeansAfterInitialization();
	}

	private void initializeBeans() {
		// left empty intentionally as primary goal of the exercise is development of
		// post processor construction skills
	}

	private void postProcessBeansBeforeInitialization() {
		beans.values().stream().map(Bean::getValue).filter(BEAN_POST_PROCESSOR_PREDICATE)
				.map(BeanPostProcessor.class::cast).forEach(this::runBeanPostProcessorBeforeInitialization);
	}

	private void runBeanPostProcessorBeforeInitialization(BeanPostProcessor postProcessor) {
		beans.values().stream().filter(this::isNotBeanOrFactoryPostProcessor)
				.forEach(bean -> runPostProcessorBeforeInitialization(postProcessor, bean));
	}

	private void runPostProcessorBeforeInitialization(BeanPostProcessor postProcessor, Bean bean) {
		postProcessor.postProcessBeforeInitialization(bean.getValue(), bean.getId());
	}

	private boolean isNotBeanOrFactoryPostProcessor(Bean bean) {
		return BEAN_POST_PROCESSOR_PREDICATE.negate().test(bean.getValue())
				&& BEAN_FACTORY_POST_PROCESSOR_PREDICATE.negate().test(bean.getValue().getClass());
	}

	private void postProcessBeansAfterInitialization() {
		beans.values().stream().map(Bean::getValue).filter(BEAN_POST_PROCESSOR_PREDICATE)
				.map(BeanPostProcessor.class::cast).forEach(this::runBeanPostProcessorAfterInitialization);
	}

	private void runBeanPostProcessorAfterInitialization(BeanPostProcessor postProcessor) {
		beans.values().stream().filter(this::isNotBeanOrFactoryPostProcessor)
				.forEach(bean -> runPostProcessorAfterInitialization(postProcessor, bean));
	}

	private void runPostProcessorAfterInitialization(BeanPostProcessor postProcessor, Bean bean) {
		Object newBean = postProcessor.postProcessAfterInitialization(bean.getValue(), bean.getId());
		bean.setValue(newBean);
	}

	private void postProcessBeanFactories(Set<BeanDefinition> beanDefinitions) {
		beans.values().stream().map(Bean::getValue).map(BeanFactoryPostProcessor.class::cast)
				.forEach(obj -> runBeanFactoryPostProcessor(obj, beanDefinitions));
	}

	private void runBeanFactoryPostProcessor(BeanFactoryPostProcessor beanFactoryPostProcessor,
			Set<BeanDefinition> beanDefinitions) {
		beanDefinitions.stream().filter(BEAN_DEFINITION_POST_PROCESSOR_PREDICATE.negate())
				.forEach(beanFactoryPostProcessor::postProcessBeanFactory);
	}

	private void instantiateAndConfigureBeans(Set<BeanDefinition> beanDefinitions, Predicate<Class<?>> classCheck) {
		for (var beanDefinition : beanDefinitions) {
			String id = beanDefinition.getId();
			try {
				Class<?> cl = Class.forName(beanDefinition.getClassName());
				beanDefinition.setClassReference(cl);
				if (classCheck.test(cl)) {
					Object obj = createObject(cl);
					setValues(obj, beanDefinition);
					setReferences(obj, beanDefinition);
					beans.put(id, new Bean(id, obj));
				}
			} catch (ClassNotFoundException e) {
				throw new NoClassForBeanException(
						String.format("can't find class %s for bean", beanDefinition.getClassName()), e);
			}
		}
	}

	private void setValues(Object bean, BeanDefinition beanDefinition) {
		for (var dependency : beanDefinition.getDependencies().entrySet()) {
			String property = dependency.getKey();
			String value = dependency.getValue();
			setPropertyValue(bean, property, value);
		}
	}

	private void setReferences(Object bean, BeanDefinition beanDefinition) {
		for (var dependency : beanDefinition.getRefDependencies().entrySet()) {
			String property = dependency.getKey();
			String referencedBeanName = dependency.getValue();
			Object referencedBean = getBean(referencedBeanName);
			if (referencedBean == null) {
				throw new NoBeanInContextException(String.format("can't find bean %s in context", referencedBeanName));
			}
			setPropertyReference(bean, property, referencedBean);
		}
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

	private void checkCandidateBeans(List<Bean> candidates, String tooManyBeansMessage, String noBeanInContextMessage) {
		if (candidates.size() > 1) {
			throw new MultipleBeansForClassException(tooManyBeansMessage);
		}
		if (candidates.isEmpty()) {
			throw new NoBeanInContextException(noBeanInContextMessage);
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
		if (name == null || name.isBlank()) {
			throw new IllegalArgumentException("non-blank bean name should be provided");
		}
		List<Bean> candidates = beans.values().stream().filter(bean -> bean.getId().equals(name)).toList();
		checkCandidateBeans(candidates, String.format("more than 1 candidate bean for id %s", name),
				String.format("no suitable bean found for id %s", name));
		return (T) candidates.get(0).getValue();
	}

	@Override
	public void setBeanDefinitionReader(BeanDefinitionReader beanDefinitionReader) {
		this.beanDefinitionReader = beanDefinitionReader;
	}

}
