package org.training.spring.ioc.context.beanfactorypostprocessor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Properties;

import org.training.spring.ioc.bean.Printer;
import org.training.spring.ioc.context.InitializingBean;
import org.training.spring.ioc.entity.BeanDefinition;
import org.training.spring.ioc.exception.PropertyReaderException;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PropertyPlaceholderBeanFactoryPostProcessor implements BeanFactoryPostProcessor, InitializingBean {

	private static final String KEY_PREFIX = "${";
	private static final String KEY_SUFFIX = "}";

	private String propertyFileName;
	private Properties properties;

	@Override
	public void afterPropertiesSet() {
		properties = new Properties();
		try (Reader reader = new BufferedReader(new FileReader(propertyFileName))) {
			properties.load(reader);
		} catch (IOException e) {
			e.printStackTrace();
			throw new PropertyReaderException(String.format("can't read properties file %s", propertyFileName), e);
		}
	}

	@Override
	public void postProcessBeanFactory(BeanDefinition beanDefinition) {
		if (Printer.class.isAssignableFrom(beanDefinition.getClassReference())) {
			var valueMap = beanDefinition.getDependencies();
			for (var entry : valueMap.entrySet()) {
				String property = entry.getKey();
				String placeholder = entry.getValue();
				if (placeholder.startsWith(KEY_PREFIX) && placeholder.endsWith(KEY_SUFFIX)) {
					String propertyKey = placeholder
							.substring(KEY_PREFIX.length(), placeholder.length() - KEY_SUFFIX.length()).trim();
					String value = properties.getProperty(propertyKey);
					if (value != null) {
						valueMap.put(property, value);
					}
				}
			}
		}
	}

}
