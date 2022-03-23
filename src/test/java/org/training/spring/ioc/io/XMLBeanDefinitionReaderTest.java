package org.training.spring.ioc.io;

import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.training.spring.ioc.entity.BeanDefinition;
import org.training.spring.ioc.exception.SourceParseException;

class XMLBeanDefinitionReaderTest {

	private static Set<BeanDefinition> expectedBeanDefinitions;

	@BeforeAll
	static void setup() {
		expectedBeanDefinitions = Set.of(
				BeanDefinition.builder().id("postProcessor")
						.className("org.training.spring.ioc.context.postprocess.PrintBeanFactoryPostProcessor").build(),
				BeanDefinition.builder().id("mailService").className("org.training.spring.ioc.service.MailService")
						.build().addDependency("protocol", "POP3").addDependency("port", "3000"),
				BeanDefinition.builder().id("userService").className("org.training.spring.ioc.service.UserService")
						.build().addRefDependency("mailService", "mailService"),
				BeanDefinition.builder().id("paymentService")
						.className("org.training.spring.ioc.service.PaymentService").build()
						.addRefDependency("mailService", "mailService"),
				BeanDefinition.builder().id("paymentWithMaxService")
						.className("org.training.spring.ioc.service.PaymentService").build()
						.addDependency("maxAmount", "500").addRefDependency("mailService", "mailService"));
	}

	@Test
	@DisplayName("bean definitions parsing succeeds")
	void testGetBeanDefinitions() {
		var xmlBeanDefinitionReader = new XMLBeanDefinitionReader("/context.xml");
		var actualBeanDefinitions = xmlBeanDefinitionReader.getBeanDefinitions();
		Assertions.assertEquals(expectedBeanDefinitions, actualBeanDefinitions);
	}

	@Test
	@DisplayName("parse bean definitions parsing fails")
	void testGetBeanDefinitionsException() {
		Assertions.assertThrowsExactly(SourceParseException.class, () -> {
			new XMLBeanDefinitionReader("/context-exception.xml").getBeanDefinitions();
		});
	}

}
