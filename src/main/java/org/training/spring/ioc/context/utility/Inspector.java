package org.training.spring.ioc.context.utility;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import org.training.spring.ioc.exception.NoDefaultConstructorException;
import org.training.spring.ioc.exception.NoSetterFoundException;
import org.training.spring.ioc.exception.SetterInvocationException;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Inspector {

	private final String SETTER_PREFIX = "set";
	private final Class<?>[] NO_ARGUMENT_TYPES = new Class[0];
	private final Object[] NO_ARGUMENTS = new Object[0];

	public <T> T createObject(Class<T> cl) {
		try {
			return cl.getDeclaredConstructor(NO_ARGUMENT_TYPES).newInstance(NO_ARGUMENTS);
		} catch (ReflectiveOperationException e) {
			throw new NoDefaultConstructorException(
					String.format("no default constructor found for class %s", cl.getName()));
		}
	}

	public void setPropertyValue(Object obj, String propertyName, Object value) {
		String setterName = getSetterName(propertyName);
		try {
			Class<?> parameterType = getPropertyType(obj, propertyName);
			Method setter = obj.getClass().getDeclaredMethod(setterName, parameterType);
			setter.setAccessible(true);
			setter.invoke(obj, value);
		} catch (NoSuchMethodException | SecurityException | InvocationTargetException | IllegalAccessException e) {
			throw new SetterInvocationException(String.format("call to setter %s failed", setterName));
		}
	}

	public void setPropertyValue(Object obj, String propertyName, String value) {
		String setterName = getSetterName(propertyName);
		try {
			Class<?> parameterType = getPropertyType(obj, propertyName);
			Method setter = obj.getClass().getDeclaredMethod(setterName, parameterType);
			setValue(obj, setter, parameterType, value);
		} catch (NoSuchMethodException | SecurityException e) {
			throw new SetterInvocationException(
					String.format("no such method %s in class %s", setterName, obj.getClass().getName()));
		}
	}

	public Class<?> getPropertyType(Object obj, String propertyName) {
		try {
			Field property = obj.getClass().getDeclaredField(propertyName);
			return property.getType();
		} catch (NoSuchFieldException e) {
			throw new NoSetterFoundException(
					String.format("can't find property %s in class %s", propertyName, obj.getClass().getName()));
		}
	}

	private void setValue(Object obj, Method setter, Class<?> type, String value) {
		setter.setAccessible(true);
		try {
			if (type == boolean.class) {
				setter.invoke(obj, Boolean.valueOf(value));
			} else if (type == byte.class) {
				setter.invoke(obj, Byte.valueOf(value));
			} else if (type == short.class) {
				setter.invoke(obj, Short.valueOf(value));
			} else if (type == int.class) {
				setter.invoke(obj, Integer.valueOf(value));
			} else if (type == long.class) {
				setter.invoke(obj, Long.valueOf(value));
			} else if (type == float.class) {
				setter.invoke(obj, Float.valueOf(value));
			} else if (type == double.class) {
				setter.invoke(obj, Double.valueOf(value));
			} else if (type == char.class) {
				setter.invoke(obj, Character.valueOf(value.charAt(0)));
			} else if (type == String.class) {
				setter.invoke(obj, value);
			} else {
				throw new SetterInvocationException(
						String.format("property %s of unknown type %s", setter.getName(), type.getName()));
			}
		} catch (InvocationTargetException | IllegalAccessException e) {
			throw new SetterInvocationException(String.format("call to setter %s failed", setter.getName()));
		}
	}

	public void setPropertyReference(Object obj, String propertyName, Object referencedObject) {
		String setterName = getSetterName(propertyName);
		try {
			Class<?> parameterType = getPropertyType(obj, propertyName);
			Method setter = obj.getClass().getDeclaredMethod(setterName, parameterType);
			setReference(obj, setter, referencedObject);
		} catch (NoSuchMethodException | SecurityException e) {
			throw new SetterInvocationException(
					String.format("no such method %s in class %s", setterName, obj.getClass().getName()));
		}
	}

	private void setReference(Object obj, Method setter, Object referencedBean) {
		try {
			setter.invoke(obj, referencedBean);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new SetterInvocationException(String.format("can't set reference via setter %s", setter.getName()));
		}
	}

	private String getSetterName(String propertyName) {
		return SETTER_PREFIX + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
	}

	public <T, A extends Annotation> Map<String, A> getAnnotatedProperties(Class<T> cl, Class<A> annotationClass) {
		Map<String, A> annotationMap = new HashMap<>();
		Field[] fields = cl.getDeclaredFields();
		for (Field field : fields) {
			A annotation = field.getAnnotation(annotationClass);
			if (annotation != null) {
				annotationMap.put(field.getName(), annotation);
			}
		}
		return annotationMap;
	}

}
