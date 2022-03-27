package org.training.spring.ioc.tester;

import org.training.spring.ioc.bean.TaskA;
import org.training.spring.ioc.bean.TaskB;
import org.training.spring.ioc.context.ApplicationContext;
import org.training.spring.ioc.context.ClassPathApplicationContext;

public class Tester {

	public static void main(String[] args) {
		ApplicationContext context = new ClassPathApplicationContext("/context.xml");
		TaskA taskABean = context.getBean(TaskA.class);
		taskABean.taskA();
		TaskB taskBBean = context.getBean(TaskB.class);
		taskBBean.taskB();
	}

}
