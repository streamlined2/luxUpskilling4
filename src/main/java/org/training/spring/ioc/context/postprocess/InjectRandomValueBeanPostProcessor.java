package org.training.spring.ioc.context.postprocess;

import static org.training.spring.ioc.context.utility.Inspector.getAnnotatedProperties;
import static org.training.spring.ioc.context.utility.Inspector.setPropertyValue;

import java.security.SecureRandom;
import java.util.random.RandomGenerator;

import org.training.spring.ioc.annotation.RandomValue;
import org.training.spring.ioc.context.utility.Inspector;

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
		Class<?> propertyType = Inspector.getPropertyType(bean, propertyName);
		if (Double.class.isAssignableFrom(propertyType) || double.class.isAssignableFrom(propertyType)) {
			double value = randomGenerator.nextDouble(annotation.min(), annotation.max());
			setPropertyValue(bean, propertyName, value);
		} else if (String.class.isAssignableFrom(propertyType)) {
			String value = generateName(annotation.length());
			setPropertyValue(bean, propertyName, value);
		}
	}

	private static String generateName(int nameLength) {
		var b = new StringBuilder();
		for (int k = 0; k < nameLength; k++) {
			b.append((char) ('A' + randomGenerator.nextInt('Z' - 'A' + 1)));
		}
		return b.toString();
	}

}
