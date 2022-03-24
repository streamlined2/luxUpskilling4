package org.training.spring.ioc.context.postprocess;

import static org.training.spring.ioc.context.utility.Inspector.getAnnotatedProperties;
import static org.training.spring.ioc.context.utility.Inspector.setPropertyValue;

import java.security.SecureRandom;
import java.util.random.RandomGenerator;

import org.training.spring.ioc.annotation.RandomValue;

public class InjectRandomValueBeanPostProcessor implements BeanPostProcessor {

	private static final RandomGenerator randomGenerator = new SecureRandom();

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) {
		var beanAnnotationsMap = getAnnotatedProperties(bean.getClass(), RandomValue.class);
		beanAnnotationsMap.entrySet().forEach(entry -> {
			processAnnotatedProperties(bean, entry.getKey(), entry.getValue());
			System.out.println(bean);
		});
		return bean;
	}

	private void processAnnotatedProperties(Object bean, String propertyName, RandomValue annotation) {
		double value = randomGenerator.nextDouble(annotation.min(), annotation.max());
		setPropertyValue(bean, propertyName, value);
	}

}
