package org.training.spring.ioc.io;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.training.spring.ioc.entity.BeanDefinition;
import org.training.spring.ioc.exception.SourceParseException;

class XMLBeanDefinitionReaderTest {

	private static List<BeanDefinition> expectedBeanDefinitions;

	@BeforeAll
	static void setup() {
		expectedBeanDefinitions = new ArrayList<>();
		expectedBeanDefinitions.add(BeanDefinition.builder().id("mailService").className("org.training.spring.ioc.service.MailService").build()
				.addDependency("protocol", "POP3").addDependency("port", "3000"));
		expectedBeanDefinitions.add(BeanDefinition.builder().id("userService").className("org.training.spring.ioc.service.UserService").build()
				.addRefDependency("mailService", "mailService"));
		expectedBeanDefinitions.add(BeanDefinition.builder().id("paymentService").className("org.training.spring.ioc.service.PaymentService")
				.build().addRefDependency("mailService", "mailService"));
		expectedBeanDefinitions.add(BeanDefinition.builder().id("paymentWithMaxService")
				.className("org.training.spring.ioc.service.PaymentService").build()
				.addDependency("maxAmount", "500").addRefDependency("mailService", "mailService"));
	}
	
	@Test
	@DisplayName("fetch and parse bean definitions succeeds")
	void testGetBeanDefinitions() {
		var xmlBeanDefinitionReader = new XMLBeanDefinitionReader("/context.xml");
		var actualBeanDefinitions = xmlBeanDefinitionReader.getBeanDefinitions();
		Collections.sort(expectedBeanDefinitions, BeanDefinition.ID_CLASSNAME_COMPARATOR);
		Collections.sort(actualBeanDefinitions, BeanDefinition.ID_CLASSNAME_COMPARATOR);
		Assertions.assertEquals(expectedBeanDefinitions, actualBeanDefinitions);
	}

	@Test
	@DisplayName("fetch and parse bean definitions fails")
	void testGetBeanDefinitionsException() {
		Assertions.assertThrowsExactly(SourceParseException.class, () -> {
			new XMLBeanDefinitionReader("/context-exception.xml").getBeanDefinitions();
		});
	}

}
