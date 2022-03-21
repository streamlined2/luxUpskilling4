package org.training.spring.ioc.context;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.training.spring.ioc.exception.MultipleBeansForClassException;
import org.training.spring.ioc.io.XMLBeanDefinitionReader;
import org.training.spring.ioc.service.MailService;
import org.training.spring.ioc.service.PaymentService;
import org.training.spring.ioc.service.UserService;

class ClassPathApplicationContextITest {

	private static UserService userService;
	private static MailService mailService;
	private static PaymentService paymentService;
	private static PaymentService paymentServiceWithMaxAmount;
	private static ApplicationContext applicationContext;

	@BeforeAll
	static void setUp() {
		mailService = MailService.builder().protocol("POP3").port(3000).build();
		userService = UserService.builder().mailService(mailService).build();
		paymentService = PaymentService.builder().mailService(mailService).build();
		paymentServiceWithMaxAmount = PaymentService.builder().mailService(mailService).maxAmount(500).build();
		applicationContext = new ClassPathApplicationContext("/context.xml");
	}

	@Test
	@DisplayName("application context instantiation")
	void testApplicationContextInstantiation() {
		var applicationContextSetReader = new ClassPathApplicationContext("/context.xml");
		applicationContextSetReader.start();
		Assertions.assertSame(applicationContext.getBean(UserService.class),
				applicationContextSetReader.getBean(UserService.class));
		Assertions.assertNotNull(applicationContext.getBean(UserService.class));
		Assertions.assertNotNull(applicationContextSetReader.getBean(UserService.class));
		Assertions.assertSame(applicationContext.getBean("mailService", MailService.class),
				applicationContextSetReader.getBean("mailService", MailService.class));
		Assertions.assertNotNull(applicationContext.getBean("mailService", MailService.class));
		Assertions.assertNotNull(applicationContextSetReader.getBean("mailService", MailService.class));
		Assertions.assertSame(applicationContext.getBean("paymentWithMaxService"),
				applicationContextSetReader.getBean("paymentWithMaxService"));
		Assertions.assertNotNull(applicationContext.getBean("paymentWithMaxService"));
		Assertions.assertNotNull(applicationContextSetReader.getBean("paymentWithMaxService"));
	}

	@Test
	@DisplayName("bean fetching by class succeeds")
	void testGetBeanByClass() {
		Assertions.assertSame(userService, applicationContext.getBean(UserService.class));
		Assertions.assertSame(mailService, applicationContext.getBean(MailService.class));
	}

	@Test
	@DisplayName("bean fetching by class fails")
	void testGetBeanByClassException() {
		Assertions.assertThrows(MultipleBeansForClassException.class,
				() -> applicationContext.getBean(PaymentService.class));
	}

	@Test
	@DisplayName("bean fetching by name and class succeeds")
	void testGetBeanByNameAndClass() {
		Assertions.assertSame(userService, applicationContext.getBean("userService", UserService.class));
		Assertions.assertSame(mailService, applicationContext.getBean("mailService", MailService.class));
		Assertions.assertSame(paymentService, applicationContext.getBean("paymentService", PaymentService.class));
		Assertions.assertSame(paymentServiceWithMaxAmount, applicationContext.getBean("paymentWithMaxService", PaymentService.class));
    }

	@Test
	@DisplayName("bean fetching by name succeeds")
	void testGetBeanByName() {
		Assertions.assertSame(userService, applicationContext.getBean("userService"));
		Assertions.assertSame(mailService, applicationContext.getBean("mailService"));
		Assertions.assertSame(paymentService, applicationContext.getBean("paymentService"));
		Assertions.assertSame(paymentServiceWithMaxAmount, applicationContext.getBean("paymentWithMaxService"));
    }

}
