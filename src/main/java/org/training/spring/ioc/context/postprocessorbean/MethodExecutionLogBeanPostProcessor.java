package org.training.spring.ioc.context.postprocessorbean;

import static org.training.spring.ioc.context.utility.Inspector.callMethod;
import static org.training.spring.ioc.context.utility.Inspector.getAnnotatedMethods;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

import org.training.spring.ioc.annotation.Log;
import org.training.spring.ioc.context.postprocessor.BeanPostProcessor;

public class MethodExecutionLogBeanPostProcessor implements BeanPostProcessor {

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) {
		Class<?> beanType = bean.getClass();
		return Proxy.newProxyInstance(beanType.getClassLoader(), beanType.getInterfaces(),
				new LogInvocationHandler(bean));
	}

	private static class LogInvocationHandler implements InvocationHandler {

		private final Object bean;
		private final Map<String, Log> annotationsMap;

		private LogInvocationHandler(Object bean) {
			this.bean = bean;
			this.annotationsMap = getAnnotatedMethods(bean.getClass(), Log.class);
		}

		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			Object value;
			Log annotation = annotationsMap.get(method.getName());
			if (annotation == null) {
				value = callMethod(bean, method, args);
			} else {
				long start = System.currentTimeMillis();
				value = callMethod(bean, method, args);
				long time = System.currentTimeMillis() - start;
				System.out.println(annotation.message() + time);
			}
			return value;
		}

	}

}
