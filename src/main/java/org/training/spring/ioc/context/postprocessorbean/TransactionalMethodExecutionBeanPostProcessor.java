package org.training.spring.ioc.context.postprocessorbean;

import static org.training.spring.ioc.context.utility.Inspector.callMethod;
import static org.training.spring.ioc.context.utility.Inspector.getAnnotatedMethods;
import static org.training.spring.ioc.context.utility.Inspector.getProxiedBean;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

import org.training.spring.ioc.annotation.Transactional;
import org.training.spring.ioc.context.postprocessor.BeanPostProcessor;

public class TransactionalMethodExecutionBeanPostProcessor implements BeanPostProcessor {

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) {
		Class<?> beanType = bean.getClass();
		return Proxy.newProxyInstance(beanType.getClassLoader(), beanType.getInterfaces(),
				new TransactionInvocationHandler(bean));
	}

	private static class TransactionInvocationHandler implements InvocationHandler, BeanProxyHandler {

		private final Object bean;
		private final Map<String, Transactional> annotationsMap;

		private TransactionInvocationHandler(Object bean) {
			this.bean = bean;
			this.annotationsMap = getAnnotatedMethods(getProxiedBean(bean).getClass(), Transactional.class);
		}

		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			Object value;
			Transactional annotation = annotationsMap.get(method.getName());
			if (annotation == null) {
				value = callMethod(bean, method, args);
			} else {
				System.out.println(annotation.startMessage());
				value = callMethod(bean, method, args);
				System.out.println(annotation.finishMessage());
			}
			return value;
		}

		@Override
		public Object getBean() {
			return bean;
		}

	}

}
