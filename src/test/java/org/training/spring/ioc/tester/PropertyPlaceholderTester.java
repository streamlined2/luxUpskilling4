package org.training.spring.ioc.tester;

import org.training.spring.ioc.bean.Printer;
import org.training.spring.ioc.context.ApplicationContext;
import org.training.spring.ioc.context.ClassPathApplicationContext;

public class PropertyPlaceholderTester {

	public static void main(String[] args) {
		ApplicationContext context = new ClassPathApplicationContext("/context.xml");
		Printer printer = context.getBean(Printer.class);
		printer.print();
	}

}
