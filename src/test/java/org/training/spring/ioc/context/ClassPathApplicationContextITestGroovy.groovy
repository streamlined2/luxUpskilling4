package org.training.spring.ioc.context

import org.training.spring.ioc.exception.MultipleBeansForClassException
import org.training.spring.ioc.io.XMLBeanDefinitionReader
import org.training.spring.ioc.service.MailService
import org.training.spring.ioc.service.PaymentService
import org.training.spring.ioc.service.UserService

class ClassPathApplicationContextITestGroovy extends GroovyTestCase {

    private UserService userService
    private MailService mailService
    private PaymentService paymentService
    private PaymentService paymentServiceWithMaxAmount
    private ApplicationContext applicationContext;

    @Override
    void setUp() {
        mailService = new MailService(protocol: "POP3", port: 3000)
        userService = new UserService(mailService: mailService)
        paymentService = new PaymentService(mailService: mailService)
        paymentServiceWithMaxAmount = new PaymentService(mailService: mailService, maxAmount: 500)
        applicationContext = new ClassPathApplicationContext("src/test/resources/context.xml")
    }

    void testApplicationContextInstantiation() {
        ApplicationContext applicationContextSetReader = new ClassPathApplicationContext()
        applicationContextSetReader.setBeanDefinitionReader(new XMLBeanDefinitionReader("src/test/resources/context.xml"))
        applicationContextSetReader.start()
        assertTrue(applicationContext.getBean(UserService.class) == applicationContextSetReader.getBean(UserService.class))
        assertTrue(applicationContext.getBean("mailService", MailService.class) == applicationContextSetReader.getBean("mailService", MailService.class))
        assertTrue(applicationContext.getBean("paymentWithMaxService") == applicationContextSetReader.getBean("paymentWithMaxService"))
    }

    void testGetBeanByClass() {
        assertTrue(userService == applicationContext.getBean(UserService.class))
        assertTrue(mailService == applicationContext.getBean(MailService.class))
    }

    void testGetBeanByClassException() {
        shouldFail(MultipleBeansForClassException) { applicationContext.getBean(PaymentService.class) }
    }

    void testGetBeanByNameAndClass() {
        assertTrue(userService == applicationContext.getBean("userService", UserService.class))
        assertTrue(mailService == applicationContext.getBean("mailService", MailService.class))
        assertTrue(paymentService == applicationContext.getBean("paymentService", PaymentService.class))
        assertTrue(paymentServiceWithMaxAmount == applicationContext.getBean("paymentWithMaxService", PaymentService.class))
    }

    void testGetBeanByName() {
        assertTrue(userService == applicationContext.getBean("userService"))
        assertTrue(mailService == applicationContext.getBean("mailService"))
        assertTrue(paymentService == applicationContext.getBean("paymentService"))
        assertTrue(paymentServiceWithMaxAmount == applicationContext.getBean("paymentWithMaxService"))
    }
}
